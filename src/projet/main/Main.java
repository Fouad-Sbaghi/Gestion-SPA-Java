package projet.main;

import java.sql.SQLException;
import projet.connexion.Connexion;
import projet.gestion.CommandParser;

/**
 * Point d'entrée principal de l'application SPA.
 * <p>
 * Initialise la connexion à la base de données et lance l'interface CLI.
 * </p>
 * 
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Chargement de l'application SPA...\n");

        if (Connexion.connectR() == null) {
            System.err.println("ERREUR : La base de données est inaccessible.");
            System.err.println("Vérifiez vos identifiants dans Connexion.java ou votre VPN.");
            return;
        }

        try {
            Connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CommandParser app = new CommandParser();
        app.run();

        System.out.println("\nApplication fermée.");
    }
}