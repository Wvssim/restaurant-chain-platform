package com.restochain.commandesservice.service;

import com.restochain.commandesservice.entite.Commande;
import com.restochain.commandesservice.enums.StatutCommande;
import com.restochain.commandesservice.exception.CreationCommandeNonDisponibleException;
import com.restochain.commandesservice.exception.RessourceIntrouvableException;
import com.restochain.commandesservice.repository.CommandeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final TransitionStatutValidator transitionStatutValidator;

    public CommandeService(CommandeRepository commandeRepository, TransitionStatutValidator transitionStatutValidator) {
        this.commandeRepository = commandeRepository;
        this.transitionStatutValidator = transitionStatutValidator;
    }

    public List<Commande> listallCommande() {
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Commande introuvable"));
    }

    // TODO Jalon v4 (Feign) : verifier la succursale, verifier/reserver les plats du menu-service,
    // calculer le prixTotal a partir des prix reels du menu, puis persister la commande.
    public Commande createCommande(Commande commande, String idempotencyKey) {
        throw new CreationCommandeNonDisponibleException();
    }

    @Transactional
    public Commande changerStatut(Long id, StatutCommande nouveauStatut) {
        Commande commande = getCommandeById(id);
        transitionStatutValidator.verifierTransition(commande.getStatut(), nouveauStatut);
        commande.setStatut(nouveauStatut);
        return commandeRepository.save(commande);
    }
}
