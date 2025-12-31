package projet.setup;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;

public class SetupDatabase {

    public static void main(String[] args) {
        System.out.println(">>> Démarrage de l'initialisation de la BDD...");

        // 1. Liste des tables à supprimer (Ordre inverse des dépendances)
        String[] dropTables = {
            "DROP TABLE IF EXISTS Planning_Animal CASCADE",
            "DROP TABLE IF EXISTS Veterinaire CASCADE",
            "DROP TABLE IF EXISTS Animal_Incident CASCADE",
            "DROP TABLE IF EXISTS Sejour_Box CASCADE",
            "DROP TABLE IF EXISTS Sejour_Famille CASCADE",
            "DROP TABLE IF EXISTS Affectation_Creneau_Activite CASCADE",
            "DROP TABLE IF EXISTS Soin CASCADE",
            "DROP TABLE IF EXISTS Animal CASCADE",
            "DROP TABLE IF EXISTS Incident CASCADE",
            "DROP TABLE IF EXISTS Personnel CASCADE",
            "DROP TABLE IF EXISTS Activite CASCADE",
            "DROP TABLE IF EXISTS Creneau CASCADE",
            "DROP TABLE IF EXISTS Famille CASCADE",
            "DROP TABLE IF EXISTS Box CASCADE"
        };

        // 2. Liste des tables à créer (Ordre des dépendances)
        String[] createTables = {
            // Tables indépendantes
            """
            CREATE TABLE Box (
                id_box SERIAL PRIMARY KEY,
                type_box VARCHAR(50) NOT NULL,
                capacite_max INT
            )
            """,
            """
            CREATE TABLE Famille (
                id_famille SERIAL PRIMARY KEY,
                type_famille VARCHAR(50),
                nom VARCHAR(100) NOT NULL,
                adresse VARCHAR(255),
                contact VARCHAR(100)
            )
            """,
            """
            CREATE TABLE Creneau (
                id_creneau SERIAL PRIMARY KEY,
                nb_benevole INT,
                heure_d TIME NOT NULL, 
                heure_f TIME NOT NULL  
            )
            """,
            """
            CREATE TABLE Activite (
                id_activite SERIAL PRIMARY KEY,
                type_act VARCHAR(100) NOT NULL
            )
            """,
            """
            CREATE TABLE Personnel (
                id_pers SERIAL PRIMARY KEY,
                nom VARCHAR(100) NOT NULL,
                prenom VARCHAR(100) NOT NULL,
                type_pers VARCHAR(50),
                tel VARCHAR(20),
                "user" VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL
            )
            """,
            """
            CREATE TABLE Incident (
                id_incident SERIAL PRIMARY KEY,
                type_incident VARCHAR(100),
                intitule VARCHAR(255) NOT NULL,
                commentaire TEXT,
                date_incident TIMESTAMP NOT NULL
            )
            """,
            """
            CREATE TABLE Animal (
                id_animal SERIAL PRIMARY KEY,
                puce VARCHAR(50) UNIQUE,
                espece VARCHAR(50) NOT NULL,
                nom VARCHAR(100),
                date_naissance DATE,
                statut VARCHAR(50),
                tests_humain BOOLEAN,
                tests_bebe BOOLEAN,
                tests_chien BOOLEAN,
                tests_chat BOOLEAN,
                date_arrivee DATE NOT NULL
            )
            """,
            // Tables dépendantes
            """
            CREATE TABLE Soin (
                id_soin SERIAL PRIMARY KEY,
                id_animal INT NOT NULL, 
                type_soin VARCHAR(100) NOT NULL,
                libelle VARCHAR(100),
                commentaire TEXT,
                date_soin TIMESTAMP NOT NULL,
                FOREIGN KEY (id_animal) REFERENCES Animal(id_animal)
            )
            """,
            """
            CREATE TABLE Affectation_Creneau_Activite (
                id_creneau INT NOT NULL,
                id_personne INT NOT NULL,
                id_activite INT NOT NULL,
                PRIMARY KEY (id_creneau, id_personne),
                FOREIGN KEY (id_creneau) REFERENCES Creneau(id_creneau),
                FOREIGN KEY (id_personne) REFERENCES Personnel(id_pers),
                FOREIGN KEY (id_activite) REFERENCES Activite(id_activite)
            )
            """,
            """
            CREATE TABLE Sejour_Famille (
                id_animal INT NOT NULL,
                id_famille INT NOT NULL,
                DATE_D DATE NOT NULL,
                DATE_F_FAMILLE DATE,
                PRIMARY KEY (id_animal, DATE_D),
                FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
                FOREIGN KEY (id_famille) REFERENCES Famille(id_famille)
            )
            """,
            """
            CREATE TABLE Sejour_Box (
                id_animal INT NOT NULL,
                id_box INT NOT NULL,
                DATE_D DATE NOT NULL,
                DATE_F_BOX DATE,
                PRIMARY KEY (id_animal, DATE_D),
                FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
                FOREIGN KEY (id_box) REFERENCES Box(id_box)
            )
            """,
            """
            CREATE TABLE Animal_Incident (
                id_animal INT NOT NULL,
                id_incident INT NOT NULL,
                PRIMARY KEY (id_animal, id_incident),
                FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
                FOREIGN KEY (id_incident) REFERENCES Incident(id_incident)
            )
            """,
            """
            CREATE TABLE Veterinaire (
                id_personne INT NOT NULL,
                id_soin INT NOT NULL,
                PRIMARY KEY (id_personne, id_soin),
                FOREIGN KEY (id_personne) REFERENCES Personnel(id_pers),
                FOREIGN KEY (id_soin) REFERENCES Soin(id_soin)
            )
            """,
            """
            CREATE TABLE Planning_Animal (
                id_animal INT NOT NULL,
                id_creneau INT NOT NULL,
                id_pers INT NOT NULL,
                DATE_D DATE NOT NULL,
                PRIMARY KEY (id_animal, id_creneau, id_pers, DATE_D),
                FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
                FOREIGN KEY (id_creneau) REFERENCES Creneau(id_creneau),
                FOREIGN KEY (id_pers) REFERENCES Personnel(id_pers)
            )
            """
        };

        // 3. Insertion des données initiales (Admin)
        String[] insertData = {
            """
            INSERT INTO Personnel (nom, prenom, type_pers, tel, "user", password) 
            VALUES ('System', 'Admin', 'Admin', '0600000000', 'admin', 'admin')
            """
        };

        // --- EXÉCUTION ---
        
        try (Connection conn = Connexion.connectR()) {

            if (conn == null) {
                System.err.println("Erreur : Impossible de se connecter à la BDD.");
                return;
            }

            try (Statement stmt = conn.createStatement()) {

                // On force PostgreSQL à travailler UNIQUEMENT dans ton dossier personnel.
                stmt.execute("SET search_path TO uapv2502163"); 

                // A. Suppression
                System.out.println("--- Nettoyage des tables existantes ---");
                for (String sql : dropTables) {
                    stmt.executeUpdate(sql);
                }
                System.out.println("Tables supprimées.");

                // B. Création
                System.out.println("--- Création des tables ---");
                for (String sql : createTables) {
                    stmt.executeUpdate(sql);
                    String tableName = sql.trim().split(" ")[2]; 
                    System.out.println("Table créée : " + tableName);
                }

                // C. Insertion
                System.out.println("--- Insertion des données de base ---");
                for (String sql : insertData) {
                    stmt.executeUpdate(sql);
                }
                System.out.println("Admin inséré.");
                
                System.out.println(">>> Initialisation terminée avec succès !");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur SQL lors de l'initialisation : " + e.getMessage());
        }
    }
}