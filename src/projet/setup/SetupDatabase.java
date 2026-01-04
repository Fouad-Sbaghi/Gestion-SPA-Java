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
                            DATE_D TIMESTAMP NOT NULL,
                            DATE_F_FAMILLE TIMESTAMP,
                            PRIMARY KEY (id_animal, DATE_D),
                            FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
                            FOREIGN KEY (id_famille) REFERENCES Famille(id_famille)
                        )
                        """,
                """
                        CREATE TABLE Sejour_Box (
                            id_animal INT NOT NULL,
                            id_box INT NOT NULL,
                            DATE_D TIMESTAMP NOT NULL,
                            DATE_F_BOX TIMESTAMP,
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

        // 3. Insertion des données initiales (10 par table)
        String[] insertData = {
                // --- Tables Indépendantes ---

                // Box
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chien', 2)", // Box 1
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chat', 5)", // Box 2
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Quarantaine', 1)", // Box 3
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chien', 3)", // Box 4
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chat', 4)", // Box 5
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Infirmerie', 1)", // Box 6
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chien', 2)", // Box 7
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Quarantaine', 2)", // Box 8 (Changement Exotique ->
                                                                                      // Quarantaine pour test)
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chat', 3)", // Box 9
                "INSERT INTO Box (type_box, capacite_max) VALUES ('Chien', 1)", // Box 10

                // Famille
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Accueil', 'Dupont', '10 Rue des Fleurs', '0601010101')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Adoptante', 'Martin', '5 Av. Liberté', '0602020202')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Accueil', 'Durand', 'Impasse des Lilas', '0603030303')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Adoptante', 'Petit', 'Bd Gambetta', '0604040404')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Accueil', 'Leroy', 'Place Jean Jaurès', '0605050505')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Accueil', 'Moreau', 'Rue de la Paix', '0606060606')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Adoptante', 'Simon', 'Av. Victor Hugo', '0607070707')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Accueil', 'Michel', 'Chemin Vert', '0608080808')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Adoptante', 'Lefebvre', 'Route Nationale', '0609090909')",
                "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES ('Accueil', 'Roux', 'Allée des Pins', '0610101010')",

                // Creneau
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (3, '08:00', '10:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (2, '10:00', '12:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (4, '14:00', '16:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (2, '16:00', '18:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (1, '18:00', '20:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (3, '09:00', '11:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (2, '13:00', '15:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (4, '15:00', '17:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (2, '07:00', '09:00')",
                "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (1, '20:00', '22:00')",

                // Activite
                "INSERT INTO Activite (type_act) VALUES ('Nettoyage')",
                "INSERT INTO Activite (type_act) VALUES ('Promenade')",
                "INSERT INTO Activite (type_act) VALUES ('Soins basiques')",
                "INSERT INTO Activite (type_act) VALUES ('Accueil public')",
                "INSERT INTO Activite (type_act) VALUES ('Administration')",
                "INSERT INTO Activite (type_act) VALUES ('Nourrissage')",
                "INSERT INTO Activite (type_act) VALUES ('Brossage')",
                "INSERT INTO Activite (type_act) VALUES ('Socialisation')",
                "INSERT INTO Activite (type_act) VALUES ('Transport')",
                "INSERT INTO Activite (type_act) VALUES ('Maintenance')",

                // Personnel
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('System', 'Admin', 'Admin', '0600000000', 'admin', 'admin')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Dubois', 'Jean', 'Vétérinaire', '0611111111', 'jean.d', 'pass1')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Bernard', 'Marie', 'Bénévole', '0622222222', 'marie.b', 'pass2')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Thomas', 'Luc', 'Bénévole', '0633333333', 'luc.t', 'pass3')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Robert', 'Sophie', 'Bénévole', '0644444444', 'sophie.r', 'pass4')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Richard', 'Pierre', 'Vétérinaire', '0655555555', 'pierre.r', 'pass5')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Petit', 'Julie', 'Bénévole', '0666666666', 'julie.p', 'pass6')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Durand', 'Paul', 'Bénévole', '0677777777', 'paul.d', 'pass7')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Leroy', 'Claire', 'Bénévole', '0688888888', 'claire.l', 'pass8')",
                "INSERT INTO Personnel (nom, prenom, type_pers, tel, \"user\", password) VALUES ('Moreau', 'Jacques', 'Directeur', '0699999999', 'jacques.m', 'pass9')",

                // Incident
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Médical', 'Blessure patte', 'Coupure légère', '2023-01-10 10:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Comportement', 'Agressivité', 'Envers un autre chien', '2023-01-11 14:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Matériel', 'Box cassé', 'Porte bloquée', '2023-01-12 09:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Médical', 'Vomissements', 'Après repas', '2023-01-13 18:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Fuite', 'Tentative fuite', 'Lors promenade', '2023-01-14 11:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Médical', 'Toux', 'Toux de chenil suspectée', '2023-01-15 08:30:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Comportement', 'Peur', 'Ne sort pas du box', '2023-01-16 10:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Alimentation', 'Refus manger', 'Depuis 24h', '2023-01-17 19:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Médical', 'Boiterie', 'Postérieur droit', '2023-01-18 15:00:00')",
                "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES ('Autre', 'Perte collier', 'Retrouvé cassé', '2023-01-19 12:00:00')",

                // Animal
                // Animal
                // 1. Rex (Chien) -> Box 1 (Chien) -> Adoptable
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600001', 'Chien', 'Rex', '2020-05-20', 'Adoptable', TRUE, FALSE, TRUE, FALSE, '2023-01-01')",
                // 2. Mimi (Chat) -> Famille 2 (Adoptante) -> Adopté
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600002', 'Chat', 'Mimi', '2021-06-15', 'Adopté', TRUE, TRUE, FALSE, TRUE, '2023-01-02')",
                // 3. Max (Chien) -> Box 4 (Chien) -> Adoptable
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600003', 'Chien', 'Max', '2019-01-10', 'Adoptable', TRUE, TRUE, TRUE, TRUE, '2023-01-03')",
                // 4. Luna (Chat) -> Box 3 (Quarantaine) -> Quarantaine
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600004', 'Chat', 'Luna', '2022-02-20', 'Quarantaine', FALSE, FALSE, FALSE, FALSE, '2023-01-04')",
                // 5. Rocky (Chien) -> Box 7 (Chien) -> Adoptable
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600005', 'Chien', 'Rocky', '2018-11-30', 'Adoptable', TRUE, FALSE, FALSE, FALSE, '2023-01-05')",
                // 6. Simba (Chat) -> Famille 6 (Accueil) -> Famille
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600006', 'Chat', 'Simba', '2020-08-05', 'Famille', TRUE, TRUE, TRUE, TRUE, '2023-01-06')",
                // 7. Bella (Chien) -> Box 6 (Infirmerie) -> Soins
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600007', 'Chien', 'Bella', '2021-12-12', 'Soins', TRUE, TRUE, TRUE, FALSE, '2023-01-07')",
                // 8. Nala (Chat) -> Box 2 (Chat) -> Adoptable
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600008', 'Chat', 'Nala', '2019-03-25', 'Adoptable', TRUE, FALSE, FALSE, TRUE, '2023-01-08')",
                // 9. Tyson (Chien) -> Box 10 (Chien) -> Adoptable
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600009', 'Chien', 'Tyson', '2017-07-14', 'Adoptable', FALSE, FALSE, TRUE, FALSE, '2023-01-09')",
                // 10. Oreo (Chat) -> Box 8 (Quarantaine) -> Quarantaine
                "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, tests_humain, tests_bebe, tests_chien, tests_chat, date_arrivee) VALUES ('2502600010', 'Chat', 'Oreo', '2022-09-01', 'Quarantaine', TRUE, TRUE, TRUE, TRUE, '2023-01-10')",

                // --- Tables Dépendantes ---

                // Soin (FK: id_animal)
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (1, 'Vaccin', 'Rage', 'Rappel annuel', '2023-02-01 10:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (2, 'Vermifuge', 'Milbemax', 'RAS', '2023-02-01 11:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (3, 'Opération', 'Stérilisation', 'Bien passé', '2023-02-02 09:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (4, 'Examen', 'Checkup', 'Arrivée', '2023-02-02 14:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (5, 'Toilettage', 'Bain', 'Sale', '2023-02-03 10:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (6, 'Vaccin', 'Typhus', 'Premier', '2023-02-03 15:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (7, 'Plaie', 'Désinfection', 'Suite incident', '2023-02-04 08:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (8, 'Antipuce', 'Pipette', 'RAS', '2023-02-04 16:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (9, 'Examen', 'Yeux', 'Conjonctivite', '2023-02-05 11:00:00')",
                "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (10, 'Poids', 'Pesée', 'Suivi croissance', '2023-02-05 13:00:00')",

                // Affectation_Creneau_Activite (FK: id_creneau, id_personne, id_activite)
                "INSERT INTO Affectation_Creneau_Activite VALUES (1, 1, 1)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (2, 2, 2)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (3, 3, 3)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (4, 4, 4)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (5, 5, 5)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (6, 6, 6)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (7, 7, 7)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (8, 8, 8)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (9, 9, 9)",
                "INSERT INTO Affectation_Creneau_Activite VALUES (10, 10, 10)",

                // ================================================================
                // SEJOUR_FAMILLE - Historiques complets
                // ================================================================

                // 1. Rex: Essai famille échoué puis retour au refuge
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (1, 1, '2023-02-15 10:00:00', '2023-02-20 14:00:00')",

                // 2. Mimi: Passage en famille d'accueil puis adoption
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (2, 3, '2023-02-01 09:00:00', '2023-02-28 17:00:00')", // Accueil
                                                                                                                                                          // Durand
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (2, 2, '2023-03-02 11:00:00', NULL)", // Adoptée
                                                                                                                                         // par
                                                                                                                                         // Martin

                // 3. Max: Multiple essais en famille
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (3, 1, '2023-02-10 08:00:00', '2023-02-25 18:00:00')", // Accueil
                                                                                                                                                          // Dupont
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (3, 3, '2023-03-03 09:00:00', '2023-03-20 18:00:00')", // Accueil
                                                                                                                                                          // Durand

                // 4. Luna: Un court séjour en famille d'accueil
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (4, 5, '2023-02-05 10:00:00', '2023-02-10 16:00:00')", // Accueil
                                                                                                                                                          // Leroy

                // 5. Rocky: Essai d'adoption raté puis famille d'accueil
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (5, 4, '2023-02-20 09:00:00', '2023-02-25 15:00:00')", // Adoptante
                                                                                                                                                          // Petit
                                                                                                                                                          // (échec)
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (5, 8, '2023-03-01 10:00:00', '2023-03-15 14:00:00')", // Accueil
                                                                                                                                                          // Michel

                // 6. Simba: Famille d'accueil active
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (6, 10, '2023-02-01 09:00:00', '2023-02-28 16:00:00')", // Accueil
                                                                                                                                                           // Roux
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (6, 6, '2023-03-06 08:30:00', NULL)", // Accueil
                                                                                                                                         // Moreau
                                                                                                                                         // (actif)

                // 7. Bella: Parcours complet avec soins
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (7, 7, '2023-03-07 10:00:00', '2023-04-01 10:00:00')", // Adoptante
                                                                                                                                                          // Simon
                                                                                                                                                          // (retour)

                // 8. Nala: Un essai en famille d'accueil
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (8, 3, '2023-03-10 09:00:00', '2023-03-20 18:00:00')", // Accueil
                                                                                                                                                          // Durand

                // 9. Tyson: Multiple passages
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (9, 5, '2023-02-01 11:00:00', '2023-02-15 10:00:00')", // Accueil
                                                                                                                                                          // Leroy
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (9, 9, '2023-03-09 11:00:00', '2023-03-12 09:00:00')", // Adoptante
                                                                                                                                                          // Lefebvre
                                                                                                                                                          // (échec)

                // 10. Oreo: Un court passage en accueil
                "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D, DATE_F_FAMILLE) VALUES (10, 10, '2023-02-15 10:00:00', '2023-02-20 15:00:00')", // Accueil
                                                                                                                                                            // Roux

                // ================================================================
                // SEJOUR_BOX - Historiques complets avec transitions
                // ================================================================

                // 1. Rex: Arrivée -> Essai famille -> Retour box actif
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (1, 3, '2023-01-01 08:00:00', '2023-01-05 10:00:00')", // Quarantaine
                                                                                                                                              // initiale
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (1, 1, '2023-01-05 11:00:00', '2023-02-15 09:00:00')", // Box
                                                                                                                                              // chien
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (1, 1, '2023-02-20 15:00:00', NULL)", // Retour
                                                                                                                             // de
                                                                                                                             // famille,
                                                                                                                             // actif

                // 2. Mimi: Arrivée -> Box -> Famille accueil -> Adoption
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (2, 3, '2023-01-02 09:00:00', '2023-01-07 10:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (2, 2, '2023-01-07 11:00:00', '2023-02-01 08:00:00')", // Box
                                                                                                                                              // chat
                                                                                                                                              // ->
                                                                                                                                              // famille

                // 3. Max: Parcours complet Box -> Famille -> Box actif
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (3, 3, '2023-01-03 10:00:00', '2023-01-08 09:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (3, 4, '2023-01-08 10:00:00', '2023-02-10 07:00:00')", // Box
                                                                                                                                              // chien
                                                                                                                                              // ->
                                                                                                                                              // famille
                                                                                                                                              // 1
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (3, 4, '2023-02-25 19:00:00', '2023-03-03 08:00:00')", // Retour
                                                                                                                                              // famille
                                                                                                                                              // 1
                                                                                                                                              // ->
                                                                                                                                              // famille
                                                                                                                                              // 2
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (3, 4, '2023-03-20 19:00:00', NULL)", // Retour
                                                                                                                             // famille
                                                                                                                             // 2,
                                                                                                                             // actif

                // 4. Luna: Quarantaine -> Essai famille -> Box quarantaine actif
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (4, 3, '2023-01-04 11:00:00', '2023-02-05 09:00:00')", // Quarantaine
                                                                                                                                              // initiale
                                                                                                                                              // ->
                                                                                                                                              // famille
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (4, 3, '2023-02-10 17:00:00', NULL)", // Retour
                                                                                                                             // de
                                                                                                                             // famille,
                                                                                                                             // quarantaine
                                                                                                                             // active

                // 5. Rocky: Parcours avec infirmerie
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (5, 3, '2023-01-05 12:00:00', '2023-01-10 14:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (5, 6, '2023-01-10 15:00:00', '2023-01-20 10:00:00')", // Infirmerie
                                                                                                                                              // (soins)
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (5, 7, '2023-01-20 11:00:00', '2023-02-20 08:00:00')", // Box
                                                                                                                                              // chien
                                                                                                                                              // ->
                                                                                                                                              // famille
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (5, 7, '2023-02-25 16:00:00', '2023-03-01 09:00:00')", // Retour
                                                                                                                                              // ->
                                                                                                                                              // famille
                                                                                                                                              // 2
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (5, 7, '2023-03-15 15:00:00', NULL)", // Retour,
                                                                                                                             // actif

                // 6. Simba: Arrivée -> Box -> Famille accueil active
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (6, 3, '2023-01-06 13:00:00', '2023-01-12 10:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (6, 5, '2023-01-12 11:00:00', '2023-02-01 08:00:00')", // Box
                                                                                                                                              // chat
                                                                                                                                              // ->
                                                                                                                                              // famille
                                                                                                                                              // 1
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (6, 5, '2023-02-28 17:00:00', '2023-03-06 08:00:00')", // Retour
                                                                                                                                              // ->
                                                                                                                                              // famille
                                                                                                                                              // 2
                                                                                                                                              // (active)

                // 7. Bella: Parcours complet avec infirmerie active
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (7, 3, '2023-01-07 14:00:00', '2023-01-12 09:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (7, 4, '2023-01-12 10:00:00', '2023-03-07 09:00:00')", // Box
                                                                                                                                              // chien
                                                                                                                                              // ->
                                                                                                                                              // famille
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (7, 6, '2023-04-01 11:00:00', NULL)", // Retour
                                                                                                                             // famille,
                                                                                                                             // infirmerie
                                                                                                                             // active

                // 8. Nala: Parcours simple
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (8, 3, '2023-01-08 15:00:00', '2023-01-13 10:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (8, 2, '2023-01-13 11:00:00', '2023-03-10 08:00:00')", // Box
                                                                                                                                              // chat
                                                                                                                                              // ->
                                                                                                                                              // famille
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (8, 2, '2023-03-20 19:00:00', NULL)", // Retour
                                                                                                                             // famille,
                                                                                                                             // actif

                // 9. Tyson: Multiple passages box et famille
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (9, 3, '2023-01-09 16:00:00', '2023-01-14 10:00:00')", // Quarantaine
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (9, 9, '2023-01-14 11:00:00', '2023-02-01 10:00:00')", // Box
                                                                                                                                              // chat
                                                                                                                                              // ->
                                                                                                                                              // famille
                                                                                                                                              // 1
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (9, 10, '2023-02-15 11:00:00', '2023-03-09 10:00:00')", // Retour
                                                                                                                                               // ->
                                                                                                                                               // famille
                                                                                                                                               // 2
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (9, 10, '2023-03-12 10:00:00', NULL)", // Retour,
                                                                                                                              // actif

                // 10. Oreo: Quarantaine prolongée
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (10, 3, '2023-01-10 17:00:00', '2023-02-01 10:00:00')", // Quarantaine
                                                                                                                                               // initiale
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (10, 9, '2023-02-01 11:00:00', '2023-02-15 09:00:00')", // Box
                                                                                                                                               // chat
                                                                                                                                               // ->
                                                                                                                                               // famille
                "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D, DATE_F_BOX) VALUES (10, 8, '2023-02-20 16:00:00', NULL)", // Retour,
                                                                                                                              // quarantaine
                                                                                                                              // active

                // Animal_Incident (FK: id_animal, id_incident)
                "INSERT INTO Animal_Incident VALUES (1, 1)",
                "INSERT INTO Animal_Incident VALUES (2, 2)",
                "INSERT INTO Animal_Incident VALUES (3, 3)",
                "INSERT INTO Animal_Incident VALUES (4, 4)",
                "INSERT INTO Animal_Incident VALUES (5, 5)",
                "INSERT INTO Animal_Incident VALUES (6, 6)",
                "INSERT INTO Animal_Incident VALUES (7, 7)",
                "INSERT INTO Animal_Incident VALUES (8, 8)",
                "INSERT INTO Animal_Incident VALUES (9, 9)",
                "INSERT INTO Animal_Incident VALUES (10, 10)",

                // Veterinaire (FK: id_personne (Vétos), id_soin) - Utilise IDs perso 2 et 6 qui
                // sont Vétos
                "INSERT INTO Veterinaire VALUES (2, 1)",
                "INSERT INTO Veterinaire VALUES (6, 2)",
                "INSERT INTO Veterinaire VALUES (2, 3)",
                "INSERT INTO Veterinaire VALUES (6, 4)",
                "INSERT INTO Veterinaire VALUES (2, 5)",
                "INSERT INTO Veterinaire VALUES (6, 6)",
                "INSERT INTO Veterinaire VALUES (2, 7)",
                "INSERT INTO Veterinaire VALUES (6, 8)",
                "INSERT INTO Veterinaire VALUES (2, 9)",
                "INSERT INTO Veterinaire VALUES (6, 10)",

                // Planning_Animal (FK: id_animal, id_creneau, id_pers)
                "INSERT INTO Planning_Animal VALUES (1, 1, 3, '2023-04-01')",
                "INSERT INTO Planning_Animal VALUES (2, 2, 4, '2023-04-01')",
                "INSERT INTO Planning_Animal VALUES (3, 3, 5, '2023-04-02')",
                "INSERT INTO Planning_Animal VALUES (4, 4, 3, '2023-04-02')",
                "INSERT INTO Planning_Animal VALUES (5, 5, 8, '2023-04-03')",
                "INSERT INTO Planning_Animal VALUES (6, 6, 9, '2023-04-03')",
                "INSERT INTO Planning_Animal VALUES (7, 7, 3, '2023-04-04')",
                "INSERT INTO Planning_Animal VALUES (8, 8, 4, '2023-04-04')",
                "INSERT INTO Planning_Animal VALUES (9, 9, 5, '2023-04-05')",
                "INSERT INTO Planning_Animal VALUES (10, 10, 8, '2023-04-05')"
        };

        // --- EXÉCUTION ---

        try (Connection conn = Connexion.connectR()) {

            if (conn == null) {
                System.err.println("Erreur : Impossible de se connecter à la BDD.");
                return;
            }

            try (Statement stmt = conn.createStatement()) {

                // On force PostgreSQL à travailler UNIQUEMENT dans mon dossier personnel sinon
                // ça cherche dans les bases de tout le monde et j'ai pas les droits.
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