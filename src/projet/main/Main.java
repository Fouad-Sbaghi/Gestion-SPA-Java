package projet.main;

import java.sql.SQLException;
import projet.connexion.Connexion;
import projet.gestion.CommandParser;

public class Main {

    public static void main(String[] args) {
        System.out.println("Chargement de l'application SPA...\n");

        // 1. Petit test de connexion au démarrage
        if (Connexion.connectR() == null) {
            System.err.println("ERREUR CRITIQUE : La base de données est inaccessible.");
            System.err.println("Vérifiez vos identifiants dans Connexion.java ou votre VPN.");
            return; // On arrête le programme
        }

        // On ferme la connexion de test proprement
        try {
            Connexion.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Lancement du gestionnaire de commandes
        CommandParser app = new CommandParser();
        app.run();

        System.out.println("Application fermée.");
    }
}