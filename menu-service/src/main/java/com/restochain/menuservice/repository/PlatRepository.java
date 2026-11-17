package com.restochain.menuservice.repository;

import com.restochain.menuservice.entite.Plat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Long> {
    List<Plat> findBySuccursaleId(Long succursaleId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Plat p where p.id = :id")
    Optional<Plat> findPlatPourModification(@Param("id") Long id);
}
