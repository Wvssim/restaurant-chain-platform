package com.restochain.commandesservice.service;

import com.restochain.commandesservice.client.MenuClient;
import com.restochain.commandesservice.client.SuccursaleClient;
import com.restochain.commandesservice.client.dto.PlatClientDto;
import com.restochain.commandesservice.client.dto.ReservationStockRequest;
import com.restochain.commandesservice.client.dto.SuccursaleClientDto;
import com.restochain.commandesservice.dto.CommandeDtoDetail;
import com.restochain.commandesservice.dto.CreateCommandeDto;
import com.restochain.commandesservice.entite.Commande;
import com.restochain.commandesservice.entite.LigneCommande;
import com.restochain.commandesservice.enums.StatutCommande;
import com.restochain.commandesservice.event.CommandeEventPublisher;
import com.restochain.commandesservice.exception.*;
import com.restochain.commandesservice.repository.CommandeRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CommandeService {
    private static final Logger log = LoggerFactory.getLogger(CommandeService.class);

    private final CommandeRepository commandeRepository;
    private final TransitionStatutValidator transitionStatutValidator;
    private final SuccursaleClient succursaleClient;
    private final MenuClient menuClient;
    private final CommandeEventPublisher commandeEventPublisher;

    public CommandeService(CommandeRepository commandeRepository,
                            TransitionStatutValidator transitionStatutValidator,
                            SuccursaleClient succursaleClient,
                            MenuClient menuClient,
                            CommandeEventPublisher commandeEventPublisher) {
        this.commandeRepository = commandeRepository;
        this.transitionStatutValidator = transitionStatutValidator;
        this.succursaleClient = succursaleClient;
        this.menuClient = menuClient;
        this.commandeEventPublisher = commandeEventPublisher;
    }

    public List<Commande> listallCommande() {
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Commande introuvable"));
    }

    /**
     * Cree une commande en verifiant la succursale et les plats aupres des services concernes,
     * et en reservant le stock reel de chaque plat. Idempotente : un meme idempotencyKey
     * renvoie la commande deja creee au lieu d'en creer une seconde.
     * <p>
     * Limite connue : si la reservation echoue sur une ligne apres avoir reussi sur une
     * ligne precedente, le stock deja reserve n'est pas restitue (pas de compensation /
     * saga a ce jalon). A traiter si le besoin se confirme au-dela du jalon resilience.
     */
    @Transactional
    public Commande createCommande(CreateCommandeDto dto, String idempotencyKey) {
        return commandeRepository.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> construireEtEnregistrerCommande(dto, idempotencyKey));
    }

    private Commande construireEtEnregistrerCommande(CreateCommandeDto dto, String idempotencyKey) {
        SuccursaleClientDto succursale = recupererSuccursale(dto.getSuccursaleId());
        if (!"OUVERTE".equals(succursale.getStatut())) {
            throw new SuccursaleFermeeException();
        }

        Commande commande = new Commande();
        commande.setIdempotencyKey(idempotencyKey);
        commande.setSuccursaleId(dto.getSuccursaleId());
        commande.setNumeroTable(dto.getNumeroTable());
        commande.setDateCommande(new Date());
        commande.setStatut(StatutCommande.RECUE);

        BigDecimal prixTotal = BigDecimal.ZERO;
        for (CreateCommandeDto.LigneCommandeCreationDto ligneDto : dto.getLignes()) {
            PlatClientDto plat = recupererPlat(ligneDto.getPlatId());
            if (!plat.getSuccursaleId().equals(dto.getSuccursaleId())) {
                throw new DonneesInvalidesException("Le plat '" + plat.getNom() + "' n'est pas propose par cette succursale");
            }
            reserverStockPlat(plat, ligneDto.getQuantite());

            LigneCommande ligne = new LigneCommande();
            ligne.setCommande(commande);
            ligne.setPlatId(plat.getId());
            ligne.setQuantite(ligneDto.getQuantite());
            ligne.setPrixUnitaire(plat.getPrix());
            BigDecimal sousTotal = plat.getPrix().multiply(BigDecimal.valueOf(ligneDto.getQuantite()));
            ligne.setSousTotal(sousTotal);
            commande.getLignesCommande().add(ligne);

            prixTotal = prixTotal.add(sousTotal);
        }
        commande.setPrixTotal(prixTotal);

        Commande commandeEnregistree = commandeRepository.save(commande);
        commandeEventPublisher.publierCreation(commandeEnregistree);
        return commandeEnregistree;
    }

    private SuccursaleClientDto recupererSuccursale(Long succursaleId) {
        try {
            return succursaleClient.getSuccursaleById(succursaleId);
        } catch (FeignException.NotFound e) {
            throw new RessourceIntrouvableException("Succursale introuvable");
        } catch (FeignException e) {
            log.warn("Appel a succursales-service en echec pour l'id {}", succursaleId, e);
            throw new ServiceIndisponibleException("succursales-service", e);
        }
    }

    private PlatClientDto recupererPlat(Long platId) {
        try {
            return menuClient.getPlatById(platId);
        } catch (FeignException.NotFound e) {
            throw new RessourceIntrouvableException("Plat introuvable");
        } catch (FeignException e) {
            log.warn("Appel a menu-service en echec pour le plat {}", platId, e);
            throw new ServiceIndisponibleException("menu-service", e);
        }
    }

    private void reserverStockPlat(PlatClientDto plat, int quantite) {
        try {
            menuClient.reserverStock(plat.getId(), new ReservationStockRequest(quantite));
        } catch (FeignException.Conflict e) {
            throw new StockInsuffisantException(plat.getNom());
        } catch (FeignException.BadRequest e) {
            throw new DonneesInvalidesException("Quantite invalide pour le plat '" + plat.getNom() + "'");
        } catch (FeignException e) {
            log.warn("Appel de reservation de stock en echec pour le plat {}", plat.getId(), e);
            throw new ServiceIndisponibleException("menu-service", e);
        }
    }

    @Transactional
    public Commande changerStatut(Long id, StatutCommande nouveauStatut) {
        Commande commande = getCommandeById(id);
        transitionStatutValidator.verifierTransition(commande.getStatut(), nouveauStatut);
        commande.setStatut(nouveauStatut);
        Commande commandeMiseAJour = commandeRepository.save(commande);
        commandeEventPublisher.publierChangementStatut(commandeMiseAJour);
        return commandeMiseAJour;
    }

    /**
     * Enrichit un DTO de consultation avec des informations recuperees aupres des services
     * concernes (nom/ville de la succursale, nom de chaque plat). Degrade gracieusement
     * (champs laisses a null) si un service distant est indisponible : une consultation ne
     * doit pas echouer entierement a cause d'une dependance en panne.
     */
    public void enrichir(CommandeDtoDetail dto) {
        try {
            SuccursaleClientDto succursale = succursaleClient.getSuccursaleById(dto.getSuccursaleId());
            dto.setSuccursaleNom(succursale.getNom());
            dto.setSuccursaleVille(succursale.getVille());
        } catch (Exception e) {
            log.warn("Enrichissement de la succursale {} impossible : {}", dto.getSuccursaleId(), e.getMessage());
        }

        dto.getLignesCommande().forEach(ligne -> {
            try {
                PlatClientDto plat = menuClient.getPlatById(ligne.getPlatId());
                ligne.setPlatNom(plat.getNom());
            } catch (Exception e) {
                log.warn("Enrichissement du plat {} impossible : {}", ligne.getPlatId(), e.getMessage());
            }
        });
    }
}
