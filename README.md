# Skypay Technical Tests

Solutions Java pour les tests techniques Skypay. Deux implémentations complètes respectant les principes Clean Code et les bonnes pratiques de développement.

## 📋 Contenu

### Test 1 : Système Bancaire
Implémentation d'un système bancaire permettant de :
- Effectuer des dépôts sur un compte
- Effectuer des retraits avec validation du solde
- Afficher un relevé de compte en ordre chronologique inversé
- Gérer les transactions avec dates et montants

**Technologies** : Java, Maven  
**Documentation** : [Test1/README.md](Test1/README.md)

### Test 2 : Système de Réservation d'Hôtel
Implémentation d'un système de réservation permettant de :
- Gérer des chambres avec différents types (STANDARD, JUNIOR, SUITE)
- Gérer des utilisateurs avec soldes de compte
- Réserver des chambres avec validation de disponibilité et de solde
- Détecter les conflits de dates entre réservations
- Conserver un historique des prix (snapshot)

**Technologies** : Java, Maven, LocalDate  
**Documentation** : [Test2/README.md](Test2/README.md)

## 🚀 Exécution

### Test 1
```bash
cd Test1
mvn clean compile
mvn exec:java -Dexec.mainClass="org.skypay.Main"
mvn test
```

### Test 2
```bash
cd Test2
mvn clean compile
mvn exec:java -Dexec.mainClass="org.skypay.Main"
mvn test
```

## 📁 Structure

```
Skypay-Technical-Tests/
├── Test1/              # Système bancaire
│   ├── src/
│   ├── pom.xml
│   └── README.md
└── Test2/              # Système de réservation d'hôtel
    ├── src/
    ├── pom.xml
    └── README.md
```

## ✅ Conformité

Les deux tests respectent :
- Clean Code principles
- Validation robuste des entrées
- Gestion appropriée des erreurs
- Documentation complète
- Tests unitaires (si applicable)
- Architecture claire et maintenable

## 🛠️ Prérequis

- Java 8 ou supérieur
- Maven 3.x
- Git

---

**Auteur** : Solutions réalisées dans le cadre du processus de recrutement Skypay  
**Date** : Décembre 2025
