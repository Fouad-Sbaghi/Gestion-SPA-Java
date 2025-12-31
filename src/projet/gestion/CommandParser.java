package projet.gestion;

import java.util.Scanner;
import projet.auth.Login;
import projet.tables.Personnel;

public class CommandParser {

    private Scanner scanner;
    private Controller controller;
    private Personnel currentUser;
    private boolean running;

    public CommandParser() {
        this.scanner = new Scanner(System.in);
        this.controller = new Controller(); // On connectera le Controller à l'étape suivante
        this.running = true;
    }

    public void run() {
        System.out.println("=== APPLICATION GESTION SPA ===");

        // 1. Phase de Login obligatoire
        if (!loginPhase()) {
            return; 
        }

        // 2. Boucle principale
        System.out.println("Tapez 'help' pour voir les commandes disponibles.");
        while (running) {
            System.out.print(currentUser.getUser() + "@spa> "); // Prompt style "admin@spa>"
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                traiterCommande(input);
            }
        }
        
        System.out.println("Fermeture de l'application.");
        scanner.close();
    }

    private boolean loginPhase() {
        Login loginAuth = new Login();
        System.out.println("Veuillez vous identifier.");

        while (true) {
            System.out.print("Utilisateur : ");
            String user = scanner.nextLine();
            System.out.print("Mot de passe : ");
            String pwd = scanner.nextLine();

            this.currentUser = loginAuth.authentifier(user, pwd);

            if (this.currentUser != null) {
                System.out.println("Bienvenue " + currentUser.getPrenom() + " !");
                // On transmet l'utilisateur au Controller pour gérer les droits si besoin
                controller.setCurrentUser(currentUser); 
                return true;
            }
            System.out.println("Erreur : Identifiants incorrects. Réessayez.");
        }
    }

    private void traiterCommande(String input) {
        String[] parts = input.split("\\s+"); // Découpe par espace
        String command = parts[0].toLowerCase();

        try {
            switch (command) {
                case "exit", "quit" -> this.running = false;
                
                case "help" -> afficherAide();

                // --- GESTION ANIMAUX ---
                case "animal" -> {
                    if (parts.length < 2) System.out.println("Précisez une action : list, add, search, delete");
                    else switch (parts[1].toLowerCase()) {
                        case "list" -> controller.listerAnimaux();
                        case "add" -> controller.ajouterAnimal(scanner);
                        case "delete" -> {
                            if(parts.length < 3) System.out.println("ID manquant : animal delete [ID]");
                            else controller.supprimerAnimal(Integer.parseInt(parts[2]));
                        }
                        // Ajouter search, update...
                        default -> System.out.println("Action animal inconnue.");
                    }
                }

                // --- GESTION BOX ---
                case "box" -> {
                    if (parts.length < 2) System.out.println("Précisez : list, info");
                    else switch(parts[1].toLowerCase()) {
                        case "list" -> controller.listerBox();
                        default -> System.out.println("Action box inconnue.");
                    }
                }

                // --- GESTION BENEVOLES & CRENEAUX ---
                case "planning" -> controller.afficherPlanning();
                
                case "alert" -> controller.checkSousEffectif();

                default -> System.out.println("Commande inconnue : " + command);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Un identifiant numérique est attendu.");
        } catch (Exception e) {
            System.out.println("Erreur d'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherAide() {
        System.out.println("--- Commandes Disponibles ---");
        System.out.println(" animal list / add / delete [id]");
        System.out.println(" box list");
        System.out.println(" planning");
        System.out.println(" alert (voir créneaux en manque de personnel)");
        System.out.println(" exit");
    }
}