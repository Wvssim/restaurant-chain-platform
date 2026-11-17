package com.restochain.succursalesservice.repository;

import com.restochain.succursalesservice.entite.Succursale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuccursaleRepository extends JpaRepository<Succursale, Long> {
}
