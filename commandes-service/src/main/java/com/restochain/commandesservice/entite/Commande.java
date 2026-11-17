package com.restochain.commandesservice.entite;

import com.restochain.commandesservice.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String idempotencyKey;

    @Column(nullable = false)
    private Long succursaleId;

    @Column(nullable = false)
    private Integer numeroTable;

    @Column(nullable = false)
    private Date dateCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<LigneCommande> lignesCommande = new ArrayList<>();

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal prixTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande statut = StatutCommande.RECUE;
}
