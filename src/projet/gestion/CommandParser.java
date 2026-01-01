package projet.gestion;

import java.sql.Date;
import java.util.Arrays;
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
        this.controller = new Controller(); 
        this.running = true;
    }

    public void run() {
        System.out.println("-------------------------------------");
        System.out.println("|===== APPLICATION GESTION SPA =====|");
        System.out.println("-------------------------------------\n");

        if (!loginPhase()) {
            return; 
        }

        while (running) {
            afficherMenuPrincipal();
            System.out.print(currentUser.getUser() + "@spa> ");
            String input = scanner.nextLine().trim();
            traiterMenuPrincipal(input);
        }
        
        System.out.println("Fermeture de l'application.");
        scanner.close();
    }

    // ==================================================================================
    // Helpers
    // ==================================================================================

    private static Integer parseIntOrNull(String raw, String label) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : " + label + " doit être un entier. Reçu : '" + raw + "'.");
            return null;
        }
    }

    private static String joinFrom(String[] parts, int startIndex) {
        if (parts == null || parts.length <= startIndex) return "";
        return String.join(" ", Arrays.copyOfRange(parts, startIndex, parts.length)).trim();
    }

    private static void printUsage(String usage) {
        System.out.println("Usage : " + usage);
    }

    private boolean loginPhase() {
        Login loginAuth = new Login();
        System.out.println("Veuillez vous identifier.\n");

        while (true) {
            System.out.print("Utilisateur : ");
            String user = scanner.nextLine();
            System.out.print("Mot de passe : ");
            String pwd = scanner.nextLine();

            this.currentUser = loginAuth.authentifier(user, pwd);

            if (this.currentUser != null) {
                System.out.println("\nBienvenue " + currentUser.getPrenom() + " !");
                controller.setCurrentUser(currentUser); 
                return true;
            }
            System.out.println("Erreur : Identifiants incorrects. Réessayez.");
        }
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n==== Menu principal ====");
        System.out.println("  1 Animaux");
        System.out.println("  2 Box");
        System.out.println("  3 Familles");
        System.out.println("  4 Planning & Bénévoles");
        System.out.println("  5 Activités & Incidents & Soins");
        System.out.println("  6 Générer un rapport");
        System.out.println("  7 Rechercher");
        System.out.println("======================");
        System.out.println("[>] Choisissez un chiffre ou tapez 'exit' :");
    }

    private void traiterMenuPrincipal(String choix) {
        switch (choix) {
            case "1" -> menuAnimaux();
            case "2" -> menuBox();
            case "3" -> menuFamilles();
            case "4" -> menuPlanning();
            case "5" -> menuActivites();
            case "6" -> menuRapport();
            case "7" -> menuRecherche();
            case "exit" -> this.running = false;
            default -> System.out.println("Choix invalide.");
        }
    }

    // --- SOUS-MENUS ---

    private void menuAnimaux() {
        System.out.println("\n--- [1] ANIMAUX ---");
        System.out.println("Commandes:");
        System.out.println("  animal list");
        System.out.println("  animal add");
        System.out.println("  animal history <idAnimal|nom>");
        System.out.println("  animal update <idAnimal>");
        System.out.println("  animal delete <idAnimal>");
        System.out.println("  animal filter <statut>");
        System.out.println("(Tapez 'help' pour ré-afficher les commandes, 'exit' pour revenir.)");
        
        boolean back = false;
        while (!back) {
            System.out.print("[Animaux] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  animal list");
                System.out.println("  animal add");
                System.out.println("  animal history <idAnimal|nom>");
                System.out.println("  animal update <idAnimal>");
                System.out.println("  animal delete <idAnimal>");
                System.out.println("  animal filter <statut>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0) continue;

            if (!parts[0].equalsIgnoreCase("animal")) {
                System.out.println("Commande inconnue (préfixe attendu : 'animal').");
                continue;
            }

            if (parts.length == 1) {
                printUsage("animal [list|add|history|update|delete|filter] ...");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "list" -> controller.listerAnimaux();
                case "add" -> controller.ajouterAnimal(scanner);
                case "delete" -> {
                    if (parts.length < 3) { printUsage("animal delete <idAnimal>"); break; }
                    Integer id = parseIntOrNull(parts[2], "idAnimal");
                    if (id != null) controller.supprimerAnimal(id);
                }
                case "update" -> {
                    if (parts.length < 3) { printUsage("animal update <idAnimal>"); break; }
                    Integer id = parseIntOrNull(parts[2], "idAnimal");
                    if (id != null) controller.updateAnimal(id, scanner);
                }
                case "history" -> {
                    if (parts.length < 3) { printUsage("animal history <idAnimal|nom>"); break; }
                    controller.chercherAnimal(joinFrom(parts, 2));
                }
                case "filter" -> {
                    if (parts.length < 3) { printUsage("animal filter <statut>"); break; }
                    controller.filtrerAnimaux(joinFrom(parts, 2));
                }
                default -> controller.chercherAnimal(joinFrom(parts, 1));
            }
        }
    }

    private void menuBox() {
        System.out.println("\n--- [2] BOX ---");
        System.out.println("Commandes:");
        System.out.println("  box list");
        System.out.println("  box info <idBox>     (détails + animaux présents)");
        System.out.println("  box add-animal <idBox> <idAnimal>");
        System.out.println("  box clear <idBox>");
        
        boolean back = false;
        while (!back) {
            System.out.print("[Box] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  box list");
                System.out.println("  box info <idBox>     (détails + animaux présents)");
                System.out.println("  box add-animal <idBox> <idAnimal>");
                System.out.println("  box clear <idBox>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0) continue;
            if (!parts[0].equalsIgnoreCase("box")) {
                System.out.println("Commande inconnue (préfixe attendu : 'box').");
                continue;
            }
            if (parts.length == 1) {
                printUsage("box [list|info|add-animal|clear] ...");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "list" -> controller.listerBox();
                case "info" -> {
                    Integer idBox;
                    if (parts.length >= 3) {
                        idBox = parseIntOrNull(parts[2], "idBox");
                    } else {
                        System.out.print("ID du box : ");
                        String idStr = scanner.nextLine().trim();
                        idBox = parseIntOrNull(idStr, "idBox");
                    }
                    if (idBox != null) controller.infoBox(idBox);
                }
                case "add-animal" -> {
                    if (parts.length < 4) { printUsage("box add-animal <idBox> <idAnimal>"); break; }
                    Integer idBox = parseIntOrNull(parts[2], "idBox");
                    Integer idAnimal = parseIntOrNull(parts[3], "idAnimal");
                    if (idBox != null && idAnimal != null) controller.placerAnimalBox(idBox, idAnimal);
                }
                case "clear" -> {
                    if (parts.length < 3) { printUsage("box clear <idBox>"); break; }
                    Integer idBox = parseIntOrNull(parts[2], "idBox");
                    if (idBox != null) controller.viderBox(idBox);
                }
                default -> System.out.println("Commande box inconnue. Tapez 'help'.");
            }
        }
    }

    private void menuFamilles() {
        System.out.println("\n--- [3] FAMILLES ---");
        System.out.println("Commandes:");
        System.out.println("  family add");
        System.out.println("  family list");
        System.out.println("  family link <idAnimal> <idFamille> <Accueil|Adoption>");
        System.out.println("  family return <idAnimal>");
        System.out.println("  family history <idFamille>");

        boolean back = false;
        while (!back) {
            System.out.print("[Familles] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  family add");
                System.out.println("  family list");
                System.out.println("  family link <idAnimal> <idFamille> <Accueil|Adoption>");
                System.out.println("  family return <idAnimal>");
                System.out.println("  family history <idFamille>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0) continue;

            if (!parts[0].equalsIgnoreCase("family")) {
                System.out.println("Commande inconnue (préfixe attendu : 'family').");
                continue;
            }
            if (parts.length == 1) {
                printUsage("family [add|list|link|return|history] ...");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "add" -> controller.ajouterFamille(scanner);
                case "list" -> controller.listerFamilles();
                case "link" -> {
                    if (parts.length < 5) { printUsage("family link <idAnimal> <idFamille> <Accueil|Adoption>"); break; }
                    Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                    Integer idFamille = parseIntOrNull(parts[3], "idFamille");
                    String type = joinFrom(parts, 4);
                    if (idAnimal != null && idFamille != null) controller.lierFamille(idAnimal, idFamille, type);
                }
                case "return" -> {
                    if (parts.length < 3) { printUsage("family return <idAnimal>"); break; }
                    Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                    if (idAnimal != null) controller.retourDeFamille(idAnimal);
                }
                case "history" -> {
                    if (parts.length < 3) { printUsage("family history <idFamille>"); break; }
                    Integer idFamille = parseIntOrNull(parts[2], "idFamille");
                    if (idFamille != null) controller.historiqueFamille(idFamille);
                }
                default -> System.out.println("Commande famille inconnue. Tapez 'help'.");
            }
        }
    }

    private void menuPlanning() {
        System.out.println("\n--- [4] PLANNING ---");
        System.out.println("Commandes: benevole add, creneau [list | alert | assign], planning animal add");

        boolean back = false;
        while (!back) {
            System.out.print("[Planning] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  benevole add");
                System.out.println("  creneau list");
                System.out.println("  creneau alert");
                System.out.println("  creneau assign <idCreneau> <idBenevole>");
                System.out.println("  planning animal add <idAnimal> <idCreneau> <idPers> <YYYY-MM-DD>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0) continue;

            // benevole ...
            if (parts[0].equalsIgnoreCase("benevole")) {
                if (parts.length >= 2 && parts[1].equalsIgnoreCase("add")) {
                    controller.ajouterBenevole(scanner);
                } else {
                    printUsage("benevole add");
                }
                continue;
            }

            // creneau ...
            if (parts[0].equalsIgnoreCase("creneau")) {
                if (parts.length < 2) { printUsage("creneau [list|alert|assign] ..."); continue; }

                switch (parts[1].toLowerCase()) {
                    case "list" -> controller.afficherPlanning();
                    case "alert" -> controller.checkSousEffectif();
                    case "assign" -> {
                        if (parts.length < 4) { printUsage("creneau assign <idCreneau> <idBenevole>"); break; }
                        Integer idCreneau = parseIntOrNull(parts[2], "idCreneau");
                        Integer idBenevole = parseIntOrNull(parts[3], "idBenevole");
                        if (idCreneau != null && idBenevole != null) controller.assignerBenevole(idCreneau, idBenevole);
                    }
                    default -> System.out.println("Commande creneau inconnue. Tapez 'help'.");
                }
                continue;
            }

            // planning animal add ...
            if (parts[0].equalsIgnoreCase("planning")) {
                if (parts.length >= 3 && parts[1].equalsIgnoreCase("animal") && parts[2].equalsIgnoreCase("add")) {
                    if (parts.length < 7) {
                        printUsage("planning animal add <idAnimal> <idCreneau> <idPers> <YYYY-MM-DD>");
                        continue;
                    }
                    Integer idAnimal = parseIntOrNull(parts[3], "idAnimal");
                    Integer idCreneau = parseIntOrNull(parts[4], "idCreneau");
                    Integer idPers = parseIntOrNull(parts[5], "idPers");
                    if (idAnimal == null || idCreneau == null || idPers == null) continue;

                    try {
                        Date date = Date.valueOf(parts[6]);
                        controller.ajouterRdvAnimal(idAnimal, idCreneau, idPers, date);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erreur : date invalide. Format attendu : YYYY-MM-DD.");
                    }
                } else {
                    printUsage("planning animal add <idAnimal> <idCreneau> <idPers> <YYYY-MM-DD>");
                }
                continue;
            }

            System.out.println("Commande inconnue. Tapez 'help'.");
        }
    }

    private void menuActivites() {
        System.out.println("\n--- [5] ACTIVITÉS & SOINS ---");
        System.out.println("Commandes: activity add, incident [add | list], soin [add | list | delete]");

        boolean back = false;
        while (!back) {
            System.out.print("[Activités] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  activity add <idAnimal> <typeActivite>");
                System.out.println("  incident add <idAnimal>");
                System.out.println("  incident list <idAnimal>");
                System.out.println("  soin add <idAnimal> <idVeto>");
                System.out.println("  soin list");
                System.out.println("  soin delete <idSoin>");
                System.out.println("  soin sign <idSoin> <idVeto>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0) continue;

            if (parts[0].equalsIgnoreCase("activity")) {
                if (parts.length >= 3 && parts[1].equalsIgnoreCase("add")) {
                    if (parts.length < 4) { printUsage("activity add <idAnimal> <typeActivite>"); continue; }
                    Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                    String type = joinFrom(parts, 3);
                    if (idAnimal != null && !type.isEmpty()) controller.ajouterActivite(idAnimal, type);
                } else {
                    printUsage("activity add <idAnimal> <typeActivite>");
                }
                continue;
            }

            if (parts[0].equalsIgnoreCase("incident")) {
                if (parts.length < 2) { printUsage("incident [add|list] ..."); continue; }
                switch (parts[1].toLowerCase()) {
                    case "add" -> {
                        if (parts.length < 3) { printUsage("incident add <idAnimal>"); break; }
                        Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                        if (idAnimal != null) controller.declarerIncident(idAnimal, scanner);
                    }
                    case "list" -> {
                        if (parts.length < 3) { printUsage("incident list <idAnimal>"); break; }
                        Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                        if (idAnimal != null) controller.listerIncidents(idAnimal);
                    }
                    default -> System.out.println("Commande incident inconnue. Tapez 'help'.");
                }
                continue;
            }

            if (parts[0].equalsIgnoreCase("soin")) {
                if (parts.length < 2) { printUsage("soin [add|list|delete|sign] ..."); continue; }

                switch (parts[1].toLowerCase()) {
                    case "add" -> {
                        if (parts.length < 4) { printUsage("soin add <idAnimal> <idVeto>"); break; }
                        Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                        Integer idVeto = parseIntOrNull(parts[3], "idVeto");
                        if (idAnimal != null && idVeto != null) controller.ajouterSoinComplet(idAnimal, idVeto, scanner);
                    }
                    case "list" -> controller.listerTousLesSoins();
                    case "delete" -> {
                        if (parts.length < 3) { printUsage("soin delete <idSoin>"); break; }
                        Integer idSoin = parseIntOrNull(parts[2], "idSoin");
                        if (idSoin != null) controller.supprimerSoin(idSoin);
                    }
                    case "sign" -> {
                        if (parts.length < 4) { printUsage("soin sign <idSoin> <idVeto>"); break; }
                        Integer idSoin = parseIntOrNull(parts[2], "idSoin");
                        Integer idVeto = parseIntOrNull(parts[3], "idVeto");
                        if (idSoin != null && idVeto != null) controller.signerSoin(idSoin, idVeto);
                    }
                    default -> System.out.println("Commande soin inconnue. Tapez 'help'.");
                }
                continue;
            }
            System.out.println("Commande inconnue. Tapez 'help'.");
        }
    }

    private void menuRapport() {
        System.out.println("\n--- [6] RAPPORTS ---");
        System.out.println("Commandes: stats [benevoles | adoptables | box]");

        boolean back = false;
        while (!back) {
            System.out.print("[Rapport] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  stats benevoles");
                System.out.println("  stats adoptables");
                System.out.println("  stats box");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length < 2 || !parts[0].equalsIgnoreCase("stats")) {
                System.out.println("Commande inconnue. Tapez 'help'.");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "benevoles" -> controller.statsBenevoles();
                case "adoptables" -> controller.statsAdoptables();
                case "box" -> controller.rapportAvanceBox();
                default -> System.out.println("Rapport inconnu. Tapez 'help'.");
            }
        }
    }

    private void menuRecherche() {
        System.out.println("\n--- [7] RECHERCHE ---");
        System.out.println("Commandes: animal <id|nom>, famille <id|nom>, benevole <id|nom|prenom|user>, incident <id|intitule>");

        boolean back = false;
        while (!back) {
            System.out.print("[Recherche] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  animal <id|nom>");
                System.out.println("  famille <id|nom>");
                System.out.println("  benevole <id|nom|prenom|user>");
                System.out.println("  incident <id|intitule>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length < 2) {
                System.out.println("Commande invalide. Tapez 'help'.");
                continue;
            }

            String entity = parts[0].toLowerCase();
            String query = joinFrom(parts, 1);

            switch (entity) {
                case "animal" -> controller.chercherAnimal(query);
                case "famille" -> controller.chercherFamille(query);
                case "benevole" -> controller.chercherBenevole(query);
                case "incident" -> controller.chercherIncident(query);
                default -> System.out.println("Recherche inconnue : '" + parts[0] + "'. (Disponible: animal, famille, benevole, incident)");
            }
        }
    }
}