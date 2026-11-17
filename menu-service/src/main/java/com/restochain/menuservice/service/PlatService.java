package com.restochain.menuservice.service;

import com.restochain.menuservice.entite.Plat;
import com.restochain.menuservice.exception.DonneesInvalidesException;
import com.restochain.menuservice.exception.RessourceIntrouvableException;
import com.restochain.menuservice.exception.StockInsuffisantException;
import com.restochain.menuservice.repository.PlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatService {
    private final PlatRepository platRepository;

    public PlatService(PlatRepository platRepository) {
        this.platRepository = platRepository;
    }

    public List<Plat> getAllPlats() {
        return platRepository.findAll();
    }

    public List<Plat> getPlatsBySuccursale(Long succursaleId) {
        return platRepository.findBySuccursaleId(succursaleId);
    }

    public Plat getPlatById(Long id) {
        return platRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Plat introuvable"));
    }

    public Plat ajouterPlat(Plat plat) {
        return platRepository.save(plat);
    }

    @Transactional
    public Plat modifierPlat(Long id, Plat nouveauPlat) {
        Plat plat = getPlatPourModification(id);
        plat.setNom(nouveauPlat.getNom());
        plat.setDescription(nouveauPlat.getDescription());
        plat.setCategorie(nouveauPlat.getCategorie());
        plat.setPrix(nouveauPlat.getPrix());
        plat.setSuccursaleId(nouveauPlat.getSuccursaleId());
        plat.setDisponible(nouveauPlat.isDisponible());
        plat.setQteDisponible(nouveauPlat.getQteDisponible());
        return platRepository.save(plat);
    }

    @Transactional
    public void supprimerPlat(Long id) {
        platRepository.delete(getPlatPourModification(id));
    }

    @Transactional
    public Plat reserverStock(Long id, int quantite) {
        if (quantite <= 0) {
            throw new DonneesInvalidesException("La quantite doit etre positive");
        }
        Plat plat = getPlatPourModification(id);
        if (!plat.isDisponible() || plat.getQteDisponible() < quantite) {
            throw new StockInsuffisantException();
        }
        plat.setQteDisponible(plat.getQteDisponible() - quantite);
        return platRepository.save(plat);
    }

    private Plat getPlatPourModification(Long id) {
        return platRepository.findPlatPourModification(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Plat introuvable"));
    }
}
