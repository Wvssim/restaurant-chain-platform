INSERT INTO commande (idempotency_key, succursale_id, numero_table, date_commande, prix_total, statut)
VALUES ('commande-demo-1', 1, 5, CURRENT_TIMESTAMP, 185.00, 'EN_PREPARATION');

INSERT INTO lignecommande (idcommande, plat_id, quantite, prix_unitaire, sous_total)
SELECT id, 3, 1, 95.00, 95.00 FROM commande WHERE idempotency_key = 'commande-demo-1';

INSERT INTO lignecommande (idcommande, plat_id, quantite, prix_unitaire, sous_total)
SELECT id, 1, 2, 45.00, 90.00 FROM commande WHERE idempotency_key = 'commande-demo-1';

INSERT INTO commande (idempotency_key, succursale_id, numero_table, date_commande, prix_total, statut)
VALUES ('commande-demo-2', 1, 2, CURRENT_TIMESTAMP, 135.00, 'RECUE');

INSERT INTO lignecommande (idcommande, plat_id, quantite, prix_unitaire, sous_total)
SELECT id, 4, 1, 135.00, 135.00 FROM commande WHERE idempotency_key = 'commande-demo-2';

INSERT INTO commande (idempotency_key, succursale_id, numero_table, date_commande, prix_total, statut)
VALUES ('commande-demo-3', 2, 8, CURRENT_TIMESTAMP, 110.00, 'SERVIE');

INSERT INTO lignecommande (idcommande, plat_id, quantite, prix_unitaire, sous_total)
SELECT id, 8, 1, 110.00, 110.00 FROM commande WHERE idempotency_key = 'commande-demo-3';
