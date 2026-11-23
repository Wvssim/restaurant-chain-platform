# Restaurant Chain Platform

Plateforme de gestion pour une **chaîne de restaurants multi-succursales**, bâtie en architecture microservices (Spring Boot / Spring Cloud).

Chaque succursale gère son propre menu et ses propres commandes. Une commande suit un **workflow d'états** représentant son cycle de vie réel en salle : reçue par le serveur, préparée en cuisine, prête, servie, puis payée — avec possibilité d'annulation tant qu'elle n'a pas été servie.

> **État du projet : jalon v2.0.** Les services métier sont enregistrés auprès d'un registre Eureka et accessibles via une passerelle API (routage statique et dynamique). La configuration centralisée et la communication interservices sont prévues aux jalons suivants (voir [Feuille de route](#feuille-de-route)). Ce README est mis à jour à chaque jalon livré.

## Sommaire

- [Description](#description)
- [Modèle de données (UML)](#modèle-de-données-uml)
- [Workflow d'une commande](#workflow-dune-commande)
- [Architecture — état actuel (v2.0)](#architecture--état-actuel-v20)
- [Démarrage](#démarrage)
- [Feuille de route](#feuille-de-route)
- [Historique des versions](#historique-des-versions)

## Description

Le projet est découpé en trois services métier autonomes, chacun avec sa propre base de données :

| Service | Rôle | Port | Endpoint principal |
|---|---|---|---|
| `succursales-service` | Référentiel des succursales de la chaîne (adresse, ville, responsable, statut) | 8081 | `/succursales` |
| `menu-service` | Plats proposés par chaque succursale, disponibilité et quantité restante du jour | 8082 | `/plats` |
| `commandes-service` | Commandes passées en salle, lignes de commande, workflow de statuts | 8083 | `/commandes` |

Chaque service expose une API REST (couches Controller / Service / Repository, DTO validés, MapStruct pour le mapping, gestion d'erreurs par exceptions dédiées avec codes HTTP appropriés) et persiste ses données dans une base H2 en mémoire, peuplée au démarrage via `data.sql`.

## Modèle de données (UML)

Les flèches pleines représentent les relations internes à un service (JPA). Les flèches pointillées représentent une **dépendance logique inter-services** (référence par identifiant uniquement ; aucun appel réseau n'est encore implémenté — prévu au jalon OpenFeign) :

```mermaid
classDiagram
    namespace SuccursalesService {
        class Succursale {
            +Long id
            +String nom
            +String adresse
            +String ville
            +String telephone
            +String responsable
            +StatutSuccursale statut
        }
        class StatutSuccursale {
            <<enumeration>>
            OUVERTE
            FERMEE
        }
    }

    namespace MenuService {
        class Plat {
            +Long id
            +String nom
            +String description
            +CategoriePlat categorie
            +BigDecimal prix
            +Long succursaleId
            +boolean disponible
            +int qteDisponible
        }
        class CategoriePlat {
            <<enumeration>>
            ENTREE
            PLAT_PRINCIPAL
            DESSERT
            BOISSON
        }
    }

    namespace CommandesService {
        class Commande {
            +Long id
            +String idempotencyKey
            +Long succursaleId
            +Integer numeroTable
            +Date dateCommande
            +BigDecimal prixTotal
            +StatutCommande statut
        }
        class LigneCommande {
            +Long id
            +Long platId
            +int quantite
            +BigDecimal prixUnitaire
            +BigDecimal sousTotal
        }
        class StatutCommande {
            <<enumeration>>
            RECUE
            EN_PREPARATION
            PRETE
            SERVIE
            PAYEE
            ANNULEE
        }
    }

    Succursale "1" --> "1" StatutSuccursale
    Plat "1" --> "1" CategoriePlat
    Commande "1" --> "1" StatutCommande
    Commande "1" *-- "many" LigneCommande : lignesCommande

    Plat ..> Succursale : succursaleId
    Commande ..> Succursale : succursaleId
    LigneCommande ..> Plat : platId
```

## Workflow d'une commande

C'est le cœur métier du projet : une commande ne peut pas changer de statut n'importe comment. Les transitions autorisées sont centralisées et validées côté serveur (`TransitionStatutValidator`), avec retour **409 Conflict** sur toute transition invalide (ex. impossible d'annuler une commande déjà servie, impossible de sauter une étape) :

```mermaid
stateDiagram-v2
    [*] --> RECUE : POST /commandes/new
    RECUE --> EN_PREPARATION : le serveur transmet en cuisine
    RECUE --> ANNULEE
    EN_PREPARATION --> PRETE : la cuisine termine
    EN_PREPARATION --> ANNULEE
    PRETE --> SERVIE : le serveur sert la table
    SERVIE --> PAYEE : encaissement
    PAYEE --> [*]
    ANNULEE --> [*]
```

Transition réalisée via `PATCH /commandes/{id}/statut`.

## Architecture — état actuel (v2.0)

Les trois services métier s'enregistrent désormais auprès d'un registre **Eureka**, et sont accessibles via une **API Gateway** unique (deux profils : routage statique déclaratif, ou routage dynamique généré depuis la découverte de service) :

```mermaid
flowchart LR
    Client(["Client / Postman"]) --> GW["gateway-service<br/>:8080"]
    GW --> A["succursales-service<br/>:8081"]
    GW --> B["menu-service<br/>:8082"]
    GW --> C["commandes-service<br/>:8083"]
    A -. s'enregistre .-> EU["eureka-server<br/>:8761"]
    B -. s'enregistre .-> EU
    C -. s'enregistre .-> EU
    GW -. consulte le registre .-> EU
```

| Application | Port |
|---|---|
| `eureka-server` | 8761 |
| `gateway-service` | 8080 |
| `succursales-service` | 8081 |
| `menu-service` | 8082 |
| `commandes-service` | 8083 |

### Routage statique vs dynamique

- **Statique** (`application-statique.properties`) : les routes `lb://SUCCURSALES-SERVICE`, `lb://MENU-SERVICE`, `lb://COMMANDES-SERVICE` sont déclarées explicitement, mais l'adresse/port de chaque instance reste résolu via Eureka.
- **Dynamique** (`application-dynamique.properties`) : les routes sont générées automatiquement pour chaque service présent dans le registre (`discovery.locator`), via `/succursales-service/succursales`, `/menu-service/plats`, `/commandes-service/commandes`.

Un seul profil est actif à la fois (même module, port 8080).

## Démarrage

Prérequis : JDK 17.

```powershell
.\mvnw.cmd clean verify

.\mvnw.cmd -pl eureka-server spring-boot:run
.\mvnw.cmd -pl succursales-service spring-boot:run
.\mvnw.cmd -pl menu-service spring-boot:run
.\mvnw.cmd -pl commandes-service spring-boot:run
.\mvnw.cmd -pl gateway-service spring-boot:run "-Dspring-boot.run.profiles=dynamique"
```

(remplacer `dynamique` par `statique` pour l'autre mode de routage)

Vérification rapide — directement sur les services métier :

```text
http://localhost:8081/succursales
http://localhost:8082/plats
http://localhost:8083/commandes
```

Via la Gateway (profil `statique`) :

```text
http://localhost:8080/succursales
http://localhost:8080/plats
http://localhost:8080/commandes
```

Via la Gateway (profil `dynamique`) :

```text
http://localhost:8080/succursales-service/succursales
http://localhost:8080/menu-service/plats
http://localhost:8080/commandes-service/commandes
```

Registre Eureka : `http://localhost:8761`. Routes de la Gateway : `http://localhost:8080/actuator/gateway/routes`.

Documentation interactive (Swagger UI) sur chaque service métier : `http://localhost:<port>/swagger-ui.html`.

## Feuille de route

Chaque jalon ajoute une brique d'infrastructure ou de communication, appliquée à ce projet.

| Jalon | Contenu | Statut |
|---|---|---|
| Init | Conception, UML, architecture | Fait |
| v1.0 | Services métier | Fait |
| v2.0 | Eureka + API Gateway (routage statique et dynamique) | Fait |
| v3.0 | Config Server | À venir |
| v4.0 | Communication synchrone (OpenFeign) | À venir |
| v5.0 | Communication asynchrone (RabbitMQ) | À venir |
| v6.0 | Résilience (Circuit Breaker, Retry, idempotence) | À venir |
| v7.0 | Migration RabbitMQ → Kafka | À venir |

## Historique des versions

### v2.0
- Ajout de `eureka-server` (8761) : registre de services, ne s'enregistre pas lui-même (`register-with-eureka=false`, `fetch-registry=false`).
- Ajout de `gateway-service` (8080, Spring Cloud Gateway sur WebFlux) avec deux profils :
  - `statique` : routes déclarées explicitement (`lb://SUCCURSALES-SERVICE`, `lb://MENU-SERVICE`, `lb://COMMANDES-SERVICE`).
  - `dynamique` : routes générées automatiquement par découverte de service (`discovery.locator`), préfixées par le nom du service.
- Les trois services métier s'enregistrent désormais auprès d'Eureka (`spring-cloud-starter-netflix-eureka-client`).
- Endpoint `/actuator/gateway/routes` exposé pour inspecter les routes actives.

### v1.0
- Trois services métier opérationnels : `succursales-service`, `menu-service`, `commandes-service`.
- Couches Controller / Service / Repository, DTO validés (Bean Validation), mapping MapStruct.
- Gestion d'erreurs par exceptions dédiées (404 ressource introuvable, 400 données invalides, 409 stock insuffisant / transition invalide).
- Réservation de stock sur `menu-service` avec verrou pessimiste pour éviter les doubles réservations concurrentes.
- Workflow de statuts de commande avec machine à états validée côté serveur et testée unitairement.
- Jeux de données de démonstration (`data.sql`) et documentation Swagger sur chaque service.
