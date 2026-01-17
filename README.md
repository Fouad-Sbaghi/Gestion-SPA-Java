# 🐾 Gestion SPA - Système de Gestion de Refuge Animalier

Application console (CLI) développée en Java pour faciliter la gestion quotidienne d'un refuge animalier (SPA). Ce projet utilise **JDBC** pour une interaction robuste avec une base de données **PostgreSQL**.

## 🚀 Fonctionnalités Principales

### 🐕 Gestion des Animaux
* **Suivi complet** : Ajout, modification et suppression de fiches animaux.
* **Historique** : Vue détaillée du parcours de l'animal (séjours en box, familles d'accueil, soins, incidents).
* **Santé & Comportement** : Suivi des tests (compatibilité chats/chiens/enfants) et des soins vétérinaires.

### 📅 Planning & Bénévoles
* **Gestion d'équipe** : Inscription des bénévoles et authentification sécurisée.
* **Planification** : Création de créneaux horaires et assignation des tâches.
* **Alertes** : Détection automatique des créneaux en sous-effectif.
* **Planning Perso** : Visualisation de l'emploi du temps individuel par bénévole.

### 📦 Logistique & Hébergement
* **Gestion des Box** : Suivi des capacités et de l'occupation en temps réel.
* **Contrôles Métier** :
    * Exception si un box est plein (`BoxPleinException`).
    * Vérification de compatibilité (ex: Chien dans box Chat interdit).
* **Familles d'Accueil** : Suivi des placements externes (Accueil temporaire ou Adoption).

### 📊 Rapports & Statistiques
* Génération de rapports sur les animaux adoptables.
* Statistiques d'engagement des bénévoles.
* Taux d'occupation du refuge.

## 🛠️ Stack Technique

* **Langage** : Java (JDK 17+)
* **Base de Données** : PostgreSQL
* **Accès aux Données** : JDBC (Java Database Connectivity)
* **Architecture** :
    * `tables/` : Modèles de données (POJO).
    * `requests/` : Couche DAO (Requêtes SQL).
    * `gestion/` : Logique métier (Controller) et Vue (CommandParser).
    * `exceptions/` : Gestion fine des erreurs (Exceptions personnalisées).

## ⚙️ Installation et Configuration

### 1. Prérequis
* Java installé sur la machine.
* Accès à une base de données PostgreSQL.
* Le driver JDBC PostgreSQL (fichier `.jar`) ajouté au *Classpath*.

### 2. Configuration de la Base de Données
Ouvrez le fichier `src/projet/connexion/Connexion.java` et modifiez les identifiants :

```java
String url = "jdbc:postgresql://VOTRE_HOTE:5432/VOTRE_BDD";
String user = "VOTRE_USER";
String pswd = "VOTRE_PASSWORD";
