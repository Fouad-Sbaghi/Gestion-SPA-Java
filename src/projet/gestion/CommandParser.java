package projet.gestion;

import java.sql.Date;
import java.util.Arrays;
import java.util.Scanner;
import projet.auth.Login;
import projet.tables.Personnel;
import projet.exceptions.donnee.ElementIntrouvableException;
import projet.exceptions.donnee.format.InvalidPuceException;
import projet.exceptions.AuthentificationException;
import projet.exceptions.SpaException;
import projet.exceptions.regle.DroitsInsuffisantsException;
import projet.exceptions.regle.SejourActifException;
import projet.exceptions.donnee.format.InvalidTelephoneException;

/**
 * Analyseur de commandes et point d'entrée de l'interface CLI.
 * <p>
 * Cette classe gère l'interface en ligne de commande de l'application.
 * Elle authentifie l'utilisateur, affiche les menus et route les commandes
 * vers les contrôleurs appropriés. L'application est organisée en 7 modules :
 * </p>
 * <ul>
 * <li>Animaux - Gestion du registre des animaux</li>
 * <li>Box - Gestion des box d'hébergement</li>
 * <li>Familles - Gestion des familles d'accueil/adoption</li>
 * <li>Planning - Gestion des bénévoles et créneaux</li>
 * <li>Activités - Gestion des soins et incidents</li>
 * <li>Rapports - Génération de statistiques</li>
 * <li>Recherche - Recherche multi-entités</li>
 * </ul>
 * 
 * @version 1.0
 * @see ControllerAnimal
 * @see ControllerBox
 * @see ControllerFamille
 * @see ControllerPlanning
 * @see ControllerActivite
 * @see ControllerRapport
 */
public class CommandParser {

    private Scanner scanner;
    private ControllerAnimal controllerAnimal;
    private ControllerFamille controllerFamille;
    private ControllerBox controllerBox;
    private ControllerPlanning controllerPlanning;
    private ControllerActivite controllerActivite;
    private ControllerRapport controllerRapport;
    private Personnel currentUser;
    private boolean running;

    /**
     * Constructeur par défaut.
     * Initialise le scanner et tous les contrôleurs.
     */
    public CommandParser() {
        this.scanner = new Scanner(System.in);
        this.controllerAnimal = new ControllerAnimal();
        this.controllerFamille = new ControllerFamille();
        this.controllerBox = new ControllerBox();
        this.controllerPlanning = new ControllerPlanning();
        this.controllerActivite = new ControllerActivite();
        this.controllerRapport = new ControllerRapport();
        this.running = true;
    }

    /**
     * Lance la boucle principale de l'application.
     * Authentifie l'utilisateur puis affiche le menu principal en boucle.
     */
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

    /**
     * Parse une chaîne en entier, affiche une erreur si invalide.
     * 
     * @param raw   La chaîne à parser.
     * @param label Le libellé du champ pour le message d'erreur.
     * @return L'entier parsé ou null si invalide.
     */
    private static Integer parseIntOrNull(String raw, String label) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : " + label + " doit etre un entier. Recu : '" + raw + "'.");
            return null;
        }
    }

    /**
     * Joint les éléments d'un tableau à partir d'un index.
     * 
     * @param parts      Le tableau de chaînes.
     * @param startIndex L'index de départ.
     * @return La chaîne jointe avec des espaces.
     */
    private static String joinFrom(String[] parts, int startIndex) {
        if (parts == null || parts.length <= startIndex)
            return "";
        return String.join(" ", Arrays.copyOfRange(parts, startIndex, parts.length)).trim();
    }

    /**
     * Affiche un message d'usage pour une commande.
     * 
     * @param usage La syntaxe attendue.
     */
    private static void printUsage(String usage) {
        System.out.println("Usage : " + usage);
    }

    /**
     * Gère la phase d'authentification.
     * Boucle jusqu'à ce que l'utilisateur soit authentifié.
     * 
     * @return true si authentifié, false sinon.
     */
    private boolean loginPhase() {
        Login loginAuth = new Login();
        System.out.println("Veuillez vous identifier.\n");

        while (true) {
            System.out.print("Utilisateur : ");
            String user = scanner.nextLine();
            System.out.print("Mot de passe : ");
            String pwd = scanner.nextLine();

            try {
                this.currentUser = loginAuth.authentifier(user, pwd);
                System.out.println("\nBienvenue " + currentUser.getPrenom() + " !");
                controllerAnimal.setCurrentUser(currentUser);
                return true;
            } catch (AuthentificationException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    /**
     * Affiche le menu principal avec les 7 options.
     */
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

    /**
     * Route le choix du menu principal vers le sous-menu approprié.
     * 
     * @param choix Le numéro du menu choisi.
     */
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

    /**
     * Affiche et gère le sous-menu Animaux.
     * Commandes : list, add, history, update, delete, filter.
     */
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
            if (parts.length == 0)
                continue;

            if (!parts[0].equalsIgnoreCase("animal")) {
                System.out.println("Commande inconnue (préfixe attendu : 'animal').");
                continue;
            }

            if (parts.length == 1) {
                printUsage("animal [list|add|history|update|delete|filter] ...");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "list" -> controllerAnimal.listerAnimaux();
                case "add" -> {
                    try {
                        controllerAnimal.ajouterAnimal(scanner);
                    } catch (SpaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "delete" -> {
                    if (parts.length < 3) {
                        printUsage("animal delete <idAnimal>");
                        break;
                    }
                    Integer id = parseIntOrNull(parts[2], "idAnimal");
                    if (id != null) {
                        try {
                            controllerAnimal.supprimerAnimal(id);
                        } catch (DroitsInsuffisantsException e) {
                            System.out.println(e.getMessage());
                        } catch (SejourActifException e) {
                            System.out.println(e.getMessage());
                        } catch (ElementIntrouvableException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                case "update" -> {
                    if (parts.length < 3) {
                        printUsage("animal update <idAnimal>");
                        break;
                    }
                    Integer id = parseIntOrNull(parts[2], "idAnimal");
                    if (id != null)
                        try {
                            controllerAnimal.updateAnimal(id, scanner);
                        } catch (ElementIntrouvableException e) {
                            System.out.println(e.getMessage());
                        } catch (InvalidPuceException e) {
                            System.out.println(e.getMessage());
                        }
                }
                case "history" -> {
                    if (parts.length < 3) {
                        printUsage("animal history <idAnimal|nom>");
                        break;
                    }
                    try {
                        controllerAnimal.chercherAnimal(joinFrom(parts, 2));
                    } catch (ElementIntrouvableException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "filter" -> {
                    if (parts.length < 3) {
                        printUsage("animal filter <statut>");
                        break;
                    }
                    controllerAnimal.filtrerAnimaux(joinFrom(parts, 2));
                }
                default -> {
                    try {
                        controllerAnimal.chercherAnimal(joinFrom(parts, 1));
                    } catch (ElementIntrouvableException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Affiche et gère le sous-menu Box.
     * Commandes : list, add, update, delete, info, add-animal, clear.
     */
    private void menuBox() {
        System.out.println("\n--- [2] BOX ---");
        System.out.println("Commandes:");
        System.out.println("  box list");
        System.out.println("  box add");
        System.out.println("  box update <idBox>");
        System.out.println("  box delete <idBox>");
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
                System.out.println("  box add");
                System.out.println("  box update <idBox>");
                System.out.println("  box delete <idBox>");
                System.out.println("  box info <idBox>     (détails + animaux présents)");
                System.out.println("  box add-animal <idBox> <idAnimal>");
                System.out.println("  box clear <idBox>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0)
                continue;
            if (!parts[0].equalsIgnoreCase("box")) {
                System.out.println("Commande inconnue (préfixe attendu : 'box').");
                continue;
            }
            if (parts.length == 1) {
                printUsage("box [list|info|add-animal|clear] ...");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "list" -> controllerBox.listerBox();
                case "add" -> controllerBox.ajouterBox(scanner);
                case "update" -> {
                    if (parts.length < 3) {
                        printUsage("box update <idBox>");
                        break;
                    }
                    Integer idBox = parseIntOrNull(parts[2], "idBox");
                    if (idBox != null)
                        controllerBox.updateBox(idBox, scanner);
                }
                case "delete" -> {
                    if (parts.length < 3) {
                        printUsage("box delete <idBox>");
                        break;
                    }
                    Integer idBox = parseIntOrNull(parts[2], "idBox");
                    if (idBox != null)
                        controllerBox.supprimerBox(idBox);
                }
                case "info" -> {
                    Integer idBox;
                    if (parts.length >= 3) {
                        idBox = parseIntOrNull(parts[2], "idBox");
                    } else {
                        System.out.print("ID du box : ");
                        String idStr = scanner.nextLine().trim();
                        idBox = parseIntOrNull(idStr, "idBox");
                    }
                    if (idBox != null) {
                        try {
                            controllerBox.infoBox(idBox);
                        } catch (ElementIntrouvableException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                case "add-animal" -> {
                    if (parts.length < 4) {
                        printUsage("box add-animal <idBox> <idAnimal>");
                        break;
                    }
                    Integer idBox = parseIntOrNull(parts[2], "idBox");
                    Integer idAnimal = parseIntOrNull(parts[3], "idAnimal");
                    if (idBox != null && idAnimal != null)
                        controllerBox.placerAnimalBox(idBox, idAnimal);
                }
                case "clear" -> {
                    if (parts.length < 3) {
                        printUsage("box clear <idBox>");
                        break;
                    }
                    Integer idBox = parseIntOrNull(parts[2], "idBox");
                    if (idBox != null)
                        controllerBox.viderBox(idBox);
                }
                default -> System.out.println("Commande box inconnue. Tapez 'help'.");
            }
        }
    }

    /**
     * Affiche et gère le sous-menu Familles.
     * Commandes : add, list, link, return, history.
     */
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
            if (parts.length == 0)
                continue;

            if (!parts[0].equalsIgnoreCase("family")) {
                System.out.println("Commande inconnue (préfixe attendu : 'family').");
                continue;
            }
            if (parts.length == 1) {
                printUsage("family [add|list|link|return|history] ...");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "add" -> controllerFamille.ajouterFamille(scanner);
                case "list" -> controllerFamille.listerFamilles();
                case "link" -> {
                    if (parts.length < 5) {
                        printUsage("family link <idAnimal> <idFamille> <Accueil|Adoption>");
                        break;
                    }
                    Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                    Integer idFamille = parseIntOrNull(parts[3], "idFamille");
                    String type = joinFrom(parts, 4);
                    if (idAnimal != null && idFamille != null)
                        controllerFamille.lierFamille(idAnimal, idFamille, type);
                }
                case "return" -> {
                    if (parts.length < 3) {
                        printUsage("family return <idAnimal>");
                        break;
                    }
                    Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                    if (idAnimal != null)
                        controllerFamille.retourDeFamille(idAnimal);
                }
                case "history" -> {
                    if (parts.length < 3) {
                        printUsage("family history <idFamille>");
                        break;
                    }
                    Integer idFamille = parseIntOrNull(parts[2], "idFamille");
                    if (idFamille != null)
                        controllerFamille.historiqueFamille(idFamille);
                }
                default -> System.out.println("Commande famille inconnue. Tapez 'help'.");
            }
        }
    }

    /**
     * Affiche et gère le sous-menu Planning.
     * Commandes : benevole, creneau, planning animal.
     */
    private void menuPlanning() {
        System.out.println("\n--- [4] PLANNING ---");
        System.out.println("Commandes: benevole [add | update | delete | planning], creneau [list | alert | assign]");

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
                System.out.println("  benevole planning <idBenevole>");
                System.out.println("  benevole update <idBenevole>");
                System.out.println("  benevole delete <idBenevole>");
                System.out.println("  creneau list");
                System.out.println("  creneau alert");
                System.out.println("  creneau assign <idCreneau> <idBenevole>");
                System.out.println("  planning animal add <idAnimal> <idCreneau> <idPers> <YYYY-MM-DD>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0)
                continue;

            if (parts[0].equalsIgnoreCase("benevole")) {
                if (parts.length >= 2 && parts[1].equalsIgnoreCase("add")) {
                    try {
                        controllerPlanning.ajouterBenevole(scanner);
                    } catch (InvalidTelephoneException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("planning")) {
                    Integer id = parseIntOrNull(parts[2], "idBenevole");
                    if (id != null)
                        controllerPlanning.planningDuBenevole(id);
                } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("update")) {
                    Integer id = parseIntOrNull(parts[2], "idBenevole");
                    if (id != null)
                        controllerPlanning.modifierBenevole(id, scanner);
                } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("delete")) {
                    Integer id = parseIntOrNull(parts[2], "idBenevole");
                    if (id != null)
                        controllerPlanning.supprimerBenevole(id);
                } else {
                    printUsage("benevole [add | planning <id> | update <id> | delete <id>]");
                }
                continue;
            }

            if (parts[0].equalsIgnoreCase("creneau")) {
                if (parts.length < 2) {
                    printUsage("creneau [list|alert|assign] ...");
                    continue;
                }

                switch (parts[1].toLowerCase()) {
                    case "list" -> controllerPlanning.afficherPlanning();
                    case "alert" -> controllerPlanning.checkSousEffectif();
                    case "assign" -> {
                        if (parts.length < 4) {
                            printUsage("creneau assign <idCreneau> <idBenevole>");
                            break;
                        }
                        Integer idCreneau = parseIntOrNull(parts[2], "idCreneau");
                        Integer idBenevole = parseIntOrNull(parts[3], "idBenevole");
                        if (idCreneau != null && idBenevole != null)
                            controllerPlanning.assignerBenevole(idCreneau, idBenevole);
                    }
                    default -> System.out.println("Commande creneau inconnue. Tapez 'help'.");
                }
                continue;
            }

            if (parts[0].equalsIgnoreCase("planning")) {
                if (parts.length >= 3 && parts[1].equalsIgnoreCase("animal") && parts[2].equalsIgnoreCase("add")) {
                    if (parts.length < 7) {
                        printUsage("planning animal add <idAnimal> <idCreneau> <idPers> <YYYY-MM-DD>");
                        continue;
                    }
                    Integer idAnimal = parseIntOrNull(parts[3], "idAnimal");
                    Integer idCreneau = parseIntOrNull(parts[4], "idCreneau");
                    Integer idPers = parseIntOrNull(parts[5], "idPers");
                    if (idAnimal == null || idCreneau == null || idPers == null)
                        continue;

                    try {
                        Date date = Date.valueOf(parts[6]);
                        controllerPlanning.ajouterRdvAnimal(idAnimal, idCreneau, idPers, date);
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

    /**
     * Affiche et gère le sous-menu Activités et Soins.
     * Commandes : activity, incident, soin.
     */
    private void menuActivites() {
        System.out.println("\n--- [5] ACTIVITES & SOINS ---");
        System.out.println(
                "Commandes: activity [list | add | delete], incident [add | list], soin [add | list | delete]");

        boolean back = false;
        while (!back) {
            System.out.print("[Activites] > ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("exit")) {
                back = true;
                continue;
            }
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("Commandes:");
                System.out.println("  activity list");
                System.out.println("  activity add <TypeActivite>");
                System.out.println("  activity delete <idActivite>");
                System.out.println("  incident add <idAnimal>");
                System.out.println("  incident list <idAnimal>");
                System.out.println("  soin add <idAnimal> <idVeto>");
                System.out.println("  soin list");
                System.out.println("  soin delete <idSoin>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length == 0)
                continue;

            if (parts[0].equalsIgnoreCase("activity")) {
                if (parts.length < 2) {
                    printUsage("activity [list | add | delete] ...");
                    continue;
                }

                switch (parts[1].toLowerCase()) {
                    case "list" -> controllerActivite.listerActivites();
                    case "add" -> {
                        if (parts.length < 3) {
                            printUsage("activity add <TypeActivite>");
                            break;
                        }
                        String type = joinFrom(parts, 2);
                        if (!type.isEmpty())
                            controllerActivite.ajouterTypeActivite(type);
                    }
                    case "delete" -> {
                        if (parts.length < 3) {
                            printUsage("activity delete <idActivite>");
                            break;
                        }
                        Integer idAct = parseIntOrNull(parts[2], "idActivite");
                        if (idAct != null)
                            controllerActivite.supprimerActivite(idAct);
                    }
                    default -> System.out.println("Commande activity inconnue. Tapez 'help'.");
                }
                continue;
            }

            if (parts[0].equalsIgnoreCase("incident")) {
                if (parts.length < 2) {
                    printUsage("incident [add|list] ...");
                    continue;
                }
                switch (parts[1].toLowerCase()) {
                    case "add" -> {
                        if (parts.length < 3) {
                            printUsage("incident add <idAnimal>");
                            break;
                        }
                        Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                        if (idAnimal != null)
                            controllerActivite.declarerIncident(idAnimal, scanner);
                    }
                    case "list" -> {
                        if (parts.length < 3) {
                            printUsage("incident list <idAnimal>");
                            break;
                        }
                        Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                        if (idAnimal != null)
                            controllerActivite.listerIncidents(idAnimal);
                    }
                    default -> System.out.println("Commande incident inconnue. Tapez 'help'.");
                }
                continue;
            }

            if (parts[0].equalsIgnoreCase("soin")) {
                if (parts.length < 2) {
                    printUsage("soin [add|list|delete] ...");
                    continue;
                }

                switch (parts[1].toLowerCase()) {
                    case "add" -> {
                        if (parts.length < 4) {
                            printUsage("soin add <idAnimal> <idVeto>");
                            break;
                        }
                        Integer idAnimal = parseIntOrNull(parts[2], "idAnimal");
                        Integer idVeto = parseIntOrNull(parts[3], "idVeto");
                        if (idAnimal != null && idVeto != null)
                            controllerActivite.ajouterSoinComplet(idAnimal, idVeto, scanner);
                    }
                    case "list" -> controllerActivite.listerTousLesSoins();
                    case "delete" -> {
                        if (parts.length < 3) {
                            printUsage("soin delete <idSoin>");
                            break;
                        }
                        Integer idSoin = parseIntOrNull(parts[2], "idSoin");
                        if (idSoin != null)
                            controllerActivite.supprimerSoin(idSoin);
                    }
                    default -> System.out.println("Commande soin inconnue. Tapez 'help'.");
                }
                continue;
            }

            System.out.println("Commande inconnue. Tapez 'help'.");
        }
    }

    /**
     * Affiche et gère le sous-menu Rapports.
     * Commandes : stats benevoles/adoptables/box, animal.
     */
    private void menuRapport() {
        System.out.println("\n--- [6] RAPPORTS ---");
        System.out.println("Commandes: stats [benevoles | adoptables | box], animal <id|nom>");

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
                System.out.println("  animal <id|nom>");
                continue;
            }

            String[] parts = cmd.split("\\s+");
            if (parts.length < 2) {
                System.out.println("Commande inconnue. Tapez 'help'.");
                continue;
            }

            if (parts[0].equalsIgnoreCase("animal")) {
                try {
                    controllerAnimal.chercherAnimal(joinFrom(parts, 1));
                } catch (ElementIntrouvableException e) {
                    System.out.println(e.getMessage());
                }
                continue;
            }

            if (!parts[0].equalsIgnoreCase("stats")) {
                System.out.println("Commande inconnue. Tapez 'help'.");
                continue;
            }

            switch (parts[1].toLowerCase()) {
                case "benevoles" -> controllerRapport.statsBenevoles();
                case "adoptables" -> controllerRapport.statsAdoptables();
                case "box" -> controllerBox.rapportAvanceBox();
                default -> System.out.println("Rapport inconnu. Tapez 'help'.");
            }
        }
    }

    /**
     * Affiche et gère le sous-menu Recherche.
     * Permet de rechercher : animal, famille, benevole, incident.
     */
    private void menuRecherche() {
        System.out.println("\n--- [7] RECHERCHE ---");
        System.out.println(
                "Commandes: animal <id|nom>, famille <id|nom>, benevole <id|nom|prenom|user>, incident <id|intitule>");

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
                case "animal" -> {
                    try {
                        controllerAnimal.chercherAnimal(query);
                    } catch (ElementIntrouvableException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "famille" -> controllerFamille.chercherFamille(query);
                case "benevole" -> controllerPlanning.chercherBenevole(query);
                case "incident" -> controllerActivite.chercherIncident(query);
                default -> System.out.println(
                        "Recherche inconnue : '" + parts[0] + "'. (Disponible: animal, famille, benevole, incident)");
            }
        }
    }
}