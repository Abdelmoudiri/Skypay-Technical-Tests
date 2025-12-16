# Hotel Booking System - Technical Test 2

Un système de réservation d'hôtel robuste en Java qui permet de gérer les chambres, les utilisateurs et les réservations avec validation complète et gestion des conflits.

## Vue d'ensemble

Ce projet implémente un système de réservation d'hôtel avec les fonctionnalités suivantes :
- **Gestion des chambres** avec différents types (STANDARD, JUNIOR, SUITE)
- **Gestion des utilisateurs** avec soldes de comptes
- **Réservation de chambres** avec validation de disponibilité
- **Calcul automatique des prix** selon le type de chambre et la durée
- **Vérification de conflits de dates** pour éviter les doubles réservations
- **Validation du solde** avant confirmation de réservation
- **Snapshot des prix** : les prix des réservations passées ne changent pas même si le prix de la chambre est modifié
- **Affichage complet** des chambres, réservations et utilisateurs

## Structure du projet

```
Test2/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           └── skypay/
│   │               ├── Main.java                    # Application de démonstration
│   │               ├── entity/
│   │               │   ├── Room.java                # Entité Chambre
│   │               │   ├── User.java                # Entité Utilisateur
│   │               │   ├── Booking.java             # Entité Réservation
│   │               │   └── enums/
│   │               │       └── RoomType.java        # Types de chambres
│   │               └── service/
│   │                   └── Service.java             # Service de gestion des réservations
│   └── test/
│       └── java/
│           └── org/
│               └── skypay/
└── pom.xml
```

## Fonctionnalités

### 1. Entité Room (Chambre)

Représente une chambre d'hôtel avec :
- **Numéro de chambre** (roomNumber)
- **Type de chambre** (RoomType : STANDARD, JUNIOR, SUITE)
- **Prix par nuit** (pricePerNight)
- **Mise à jour dynamique** : possibilité de modifier le type et le prix

```java
public Room(int roomNumber, RoomType type, int pricePerNight)
```

### 2. Entité User (Utilisateur)

Gère les informations utilisateur :
- **ID utilisateur** unique
- **Solde du compte** (balance)
- **Déduction automatique** lors de la réservation

```java
public User(int id, int balance)
```

### 3. Entité Booking (Réservation)

Représente une réservation avec :
- **Référence utilisateur** et **numéro de chambre**
- **Dates de check-in et check-out** (LocalDate)
- **Snapshot du prix et du type** au moment de la réservation
- **Immutabilité des prix passés** : les modifications futures de prix n'affectent pas les réservations existantes

```java
public Booking(User user, Room room, LocalDate checkIn, LocalDate checkOut)
```

### 4. Enum RoomType

Définit les types de chambres disponibles :
- **STANDARD** : Chambre standard
- **JUNIOR** : Suite junior
- **SUITE** : Suite de luxe

### 5. Service de gestion

La classe `Service` fournit l'interface principale pour :

#### Gestion des chambres
```java
public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight)
```
- Crée une nouvelle chambre si elle n'existe pas
- Met à jour une chambre existante (type et/ou prix)

#### Gestion des utilisateurs
```java
public void setUser(int userId, int balance)
```
- Crée un nouvel utilisateur si il n'existe pas
- Met à jour le solde d'un utilisateur existant

#### Réservation de chambre
```java
public void bookRoom(int userId, int roomNumber, Date checkinDate, Date checkOutDate)
```

**Validations effectuées** :
1. ✅ La date de check-out doit être après la date de check-in
2. ✅ L'utilisateur et la chambre doivent exister
3. ✅ La chambre doit être disponible (pas de chevauchement de dates)
4. ✅ Le solde de l'utilisateur doit être suffisant pour couvrir le coût total
5. ✅ Calcul automatique du prix : (nombre de nuits) × (prix par nuit)

**En cas de succès** :
- Le solde de l'utilisateur est débité
- La réservation est enregistrée avec un snapshot du prix et du type de chambre
- Un message de confirmation est affiché

**En cas d'échec** :
- Un message d'erreur explicite est affiché
- Aucune modification n'est effectuée

#### Affichage des données
```java
public void printAll()         // Affiche chambres et réservations (du plus récent au plus ancien)
public void printAllUsers()    // Affiche les utilisateurs (du plus récent au plus ancien)
```

## Logique de validation

### Détection de chevauchement de dates

Le système utilise un algorithme de détection de conflit pour vérifier la disponibilité :

```
Conflit détecté si : (NouvelleDate_Début < Réservation_Fin) ET (NouvelleDate_Fin > Réservation_Début)
```

Cela empêche les situations suivantes :
- Réservations qui se chevauchent partiellement
- Réservations complètement incluses dans une autre
- Réservations qui englobent une autre réservation

### Snapshot des prix

Lorsqu'une réservation est créée, le prix et le type de chambre sont "figés" :
```java
this.snapshotPrice = room.getPricePerNight();
this.snapshotType = room.getType();
```

**Avantage** : Si le prix de la chambre est modifié ultérieurement, les réservations passées conservent leur prix d'origine.

## Exemple d'utilisation

```java
Service service = new Service();

// Initialisation des chambres
service.setRoom(1, RoomType.STANDARD, 1000);
service.setRoom(2, RoomType.JUNIOR, 2000);
service.setRoom(3, RoomType.SUITE, 3000);

// Initialisation des utilisateurs
service.setUser(1, 5000);  // User 1 avec 5000 de balance
service.setUser(2, 10000); // User 2 avec 10000 de balance

// Création de dates
Date checkIn = makeDate(2026, 7, 7);
Date checkOut = makeDate(2026, 7, 8);

// Réservation (1 nuit, chambre 1 à 1000/nuit)
service.bookRoom(1, 1, checkIn, checkOut);
// Résultat: Succès, User 1 débité de 1000, balance restante: 4000

// Tentative de réservation sur la même période (conflit)
service.bookRoom(2, 1, checkIn, checkOut);
// Résultat: Erreur - Chambre non disponible

// Mise à jour du prix de la chambre
service.setRoom(1, RoomType.SUITE, 10000);
// Les réservations passées conservent le prix de 1000

// Affichage
service.printAll();
service.printAllUsers();
```

## Scénarios de test

Le `Main.java` démontre plusieurs scénarios :

1. ❌ **Solde insuffisant** : User 1 tente de réserver Room 2 (7 nuits × 2000 = 14000) mais n'a que 5000
2. ❌ **Dates invalides** : Tentative de réservation avec date de fin avant date de début
3. ✅ **Réservation réussie** : User 1 réserve Room 1 (1 nuit × 1000 = 1000) → Solde: 4000
4. ❌ **Conflit de dates** : User 2 tente de réserver Room 1 déjà occupée sur la même période
5. ✅ **Réservation réussie** : User 2 réserve Room 3 (1 nuit × 3000 = 3000) → Solde: 7000

## Sortie attendue

```
=== INITIALISATION ===

=== SCENARIOS ===
1. User 1 book Room 2 (cher): Erreur: Solde insuffisant pour l'utilisateur 1. (Requis: 14000, Dispo: 5000)
2. User 1 book Room 2 (dates invalides): Erreur: La date de fin doit être après la date de début.
3. User 1 book Room 1 (ok): Succès: Chambre 1 réservée par User 1 pour 1 nuits.
4. User 2 book Room 1 (overlap): Erreur: La chambre 1 n'est pas disponible sur cette période.
5. User 2 book Room 3 (ok): Succès: Chambre 3 réservée par User 2 pour 1 nuits.

=== MISE A JOUR CHAMBRE ===
Room 1 mise à jour (Type SUITE, Prix 10000)

=== RESULTATS FINAUX ===
--- LISTE DES CHAMBRES (Récent -> Ancien) ---
Room 3 [SUITE] - 3000/night
Room 2 [JUNIOR] - 2000/night
Room 1 [SUITE] - 10000/night
--- HISTORIQUE RESERVATIONS (Récent -> Ancien) ---
Booking: User 2 -> Room 3 (SUITE at 3000) from 2026-07-07 to 2026-07-08
Booking: User 1 -> Room 1 (STANDARD at 1000) from 2026-07-07 to 2026-07-08

--- LISTE DES USERS (Récent -> Ancien) ---
User ID: 2, Balance: 7000
User ID: 1, Balance: 4000
```

## Exigences techniques satisfaites

✅ **Gestion complète des entités** : Room, User, Booking avec relations appropriées
✅ **Validation robuste** : Dates, disponibilité, soldes
✅ **Gestion des erreurs** : Messages explicites sans exceptions non gérées
✅ **Immutabilité des données historiques** : Snapshot des prix lors de la réservation
✅ **Logique métier correcte** : Calcul des prix, détection de conflits
✅ **Code propre** : Séparation claire des responsabilités (entités, services, main)
✅ **Utilisation de Java moderne** : LocalDate pour les dates, Stream API pour les recherches
✅ **Ordre chronologique inversé** : Affichage du plus récent au plus ancien

## Comment exécuter

### Compilation et exécution
```bash
cd Test2
mvn clean compile
mvn exec:java -Dexec.mainClass="org.skypay.Main"
```

### Tests
```bash
mvn test
```

## Technologies utilisées

- **Java 8+** (LocalDate, Stream API)
- **Maven** pour la gestion des dépendances
- **java.time** pour la gestion moderne des dates
- **Collections framework** (ArrayList) pour le stockage en mémoire

## Points d'attention

### Gestion des dates
- Le système utilise `java.util.Date` en entrée (comme spécifié) mais convertit en `LocalDate` pour les manipulations
- Conversion automatique via `ZoneId.systemDefault()`

### Persistance
- Les données sont stockées en mémoire (ArrayList)
- Aucune persistance en base de données (hors scope du test)

### Concurrence
- Le système n'est pas thread-safe dans sa version actuelle
- Pour une utilisation en production, il faudrait ajouter des mécanismes de synchronisation

## Architecture et principes

- **Séparation des responsabilités** : Entités, Services, Main
- **Encapsulation** : Méthodes privées pour les helpers
- **Validation précoce** : Toutes les validations avant modification de l'état
- **Messages clairs** : Retours utilisateur explicites pour chaque opération
- **Immutabilité des historiques** : Les snapshots protègent l'intégrité des données passées

## Auteur

Implémentation réalisée dans le cadre du test technique Skypay - Test 2.

---

*Ce projet démontre une maîtrise des concepts Java, de la gestion des dates, de la logique métier complexe et des bonnes pratiques de développement.*
