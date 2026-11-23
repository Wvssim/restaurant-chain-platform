package com.restochain.commandesservice.repository;

import com.restochain.commandesservice.entite.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Optional<Commande> findByIdempotencyKey(String idempotencyKey);
}
