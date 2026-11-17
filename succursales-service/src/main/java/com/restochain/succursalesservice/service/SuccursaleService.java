package com.restochain.succursalesservice.service;

import com.restochain.succursalesservice.entite.Succursale;
import com.restochain.succursalesservice.exception.RessourceIntrouvableException;
import com.restochain.succursalesservice.repository.SuccursaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SuccursaleService {
    private final SuccursaleRepository succursaleRepository;

    public SuccursaleService(SuccursaleRepository succursaleRepository) {
        this.succursaleRepository = succursaleRepository;
    }

    public List<Succursale> getAllSuccursales() {
        return succursaleRepository.findAll();
    }

    public Succursale getSuccursaleById(Long id) {
        return succursaleRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Succursale introuvable"));
    }

    public Succursale ajouterSuccursale(Succursale succursale) {
        return succursaleRepository.save(succursale);
    }

    @Transactional
    public Succursale modifierSuccursale(Long id, Succursale nouvelleSuccursale) {
        Succursale succursale = getSuccursaleById(id);
        succursale.setNom(nouvelleSuccursale.getNom());
        succursale.setAdresse(nouvelleSuccursale.getAdresse());
        succursale.setVille(nouvelleSuccursale.getVille());
        succursale.setTelephone(nouvelleSuccursale.getTelephone());
        succursale.setResponsable(nouvelleSuccursale.getResponsable());
        succursale.setStatut(nouvelleSuccursale.getStatut());
        return succursaleRepository.save(succursale);
    }

    @Transactional
    public void supprimerSuccursale(Long id) {
        succursaleRepository.delete(getSuccursaleById(id));
    }
}
