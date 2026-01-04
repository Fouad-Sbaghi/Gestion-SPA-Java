package projet.gestion;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import java.text.Normalizer;

import projet.exceptions.donnee.format.InvalidTelephoneException;
import projet.requests.AffectationCreneauActiviteRequest;
import projet.requests.CreneauRequest;
import projet.requests.PersonnelRequest;
import projet.requests.PlanningAnimalRequest;
import projet.requests.rapports.RapportPlanningRequest;
import projet.tables.Personnel;

/**
 * Contrôleur gérant le planning, les créneaux et les bénévoles.
 * <p>
 * Ce contrôleur permet de gérer les bénévoles (ajout, modification,
 * suppression),
 * les créneaux horaires, les affectations et le planning des animaux.
 * Il vérifie les formats de données comme les numéros de téléphone.
 * </p>
 * 
 * @version 1.0
 * @see projet.requests.PersonnelRequest
 * @see projet.requests.CreneauRequest
 */
public class ControllerPlanning {

    private CreneauRequest creneauReq;
    private PersonnelRequest personnelReq;
    private PlanningAnimalRequest planningAnimalReq;
    private AffectationCreneauActiviteRequest affectationReq;
    private RapportPlanningRequest rapportPlanning;

    /**
     * Constructeur par défaut.
     * Initialise les requêtes pour les créneaux, le personnel et les affectations.
     */
    public ControllerPlanning() {
        this.creneauReq = new CreneauRequest();
        this.personnelReq = new PersonnelRequest();
        this.planningAnimalReq = new PlanningAnimalRequest();
        this.affectationReq = new AffectationCreneauActiviteRequest();
        this.rapportPlanning = new RapportPlanningRequest();
    }

    /**
     * Valide le format d'un numéro de téléphone français.
     * 
     * @param tel Le numéro à valider.
     * @throws InvalidTelephoneException Si le format est invalide.
     */
    private void validerTelephone(String tel) throws InvalidTelephoneException {
        if (tel != null && !tel.isEmpty() && !tel.matches("^0[1-9][0-9]{8}$")) {
            throw new InvalidTelephoneException(tel, "Le format attendu est 10 chiffres commençant par 0");
        }
    }

    /**
     * Ajoute un nouveau bénévole via saisie interactive.
     * 
     * @param scanner Le scanner pour la saisie utilisateur.
     * @throws InvalidTelephoneException Si le format du téléphone est invalide.
     */
    public void ajouterBenevole(Scanner scanner) throws InvalidTelephoneException {
        System.out.println(">> Nouveau Benevole");
        Personnel p = new Personnel();
        p.setType_pers("Benevole");

        System.out.print("Nom : ");
        p.setNom(scanner.nextLine());
        System.out.print("Prenom : ");
        p.setPrenom(scanner.nextLine());
        System.out.print("Tel : ");
        String tel = scanner.nextLine();
        validerTelephone(tel);
        p.setTel(tel);
        System.out.print("User : ");
        p.setUser(scanner.nextLine());
        System.out.print("Pass : ");
        p.setPassword(scanner.nextLine());

        personnelReq.add(p);
        System.out.println("Benevole ajoute.");
    }

    /**
     * Affiche le planning général avec tous les créneaux configurés.
     */
    public void afficherPlanning() {
        System.out.println("--- Planning General ---");
        List<?> liste = creneauReq.getAll();
        System.out.println("Creneaux configures : " + liste.size());
        affectationReq.listerAffectations();
    }

    /**
     * Vérifie et affiche les créneaux en sous-effectif.
     */
    public void checkSousEffectif() {
        System.out.println("--- ALERTE : Creneaux en sous-effectif ---");
        List<String> alertes = rapportPlanning.getCreneauxManquants();
        if (alertes.isEmpty())
            System.out.println("R.A.S. Planning complet.");
        else
            for (String alerte : alertes)
                System.out.println("! " + alerte);
    }

    /**
     * Assigne un bénévole à un créneau avec l'activité par défaut.
     * 
     * @param idCreneau  L'identifiant du créneau.
     * @param idBenevole L'identifiant du bénévole.
     */
    public void assignerBenevole(int idCreneau, int idBenevole) {
        try {
            int idActiviteDefaut = 1;
            affectationReq.assigner(idCreneau, idBenevole, idActiviteDefaut);
        } catch (projet.exceptions.regle.CapaciteInsuffisanteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Affiche le planning d'un bénévole spécifique.
     * 
     * @param idBenevole L'identifiant du bénévole.
     */
    public void planningDuBenevole(int idBenevole) {
        Personnel p = personnelReq.getById(idBenevole);

        if (p == null) {
            System.out.println("Benevole introuvable (ID " + idBenevole + ").");
            return;
        }

        System.out.println("\nPlanning de : " + p.getPrenom() + " " + p.getNom() + " (ID " + idBenevole + ")");
        affectationReq.afficherPlanningPersonne(idBenevole);
    }

    /**
     * Ajoute un rendez-vous pour un animal dans le planning.
     * 
     * @param idAnimal  L'identifiant de l'animal.
     * @param idCreneau L'identifiant du créneau.
     * @param idPers    L'identifiant de la personne responsable.
     * @param date      La date du rendez-vous.
     */
    public void ajouterRdvAnimal(int idAnimal, int idCreneau, int idPers, Date date) {
        planningAnimalReq.assigner(idAnimal, idCreneau, idPers, date);
    }

    /**
     * Recherche et affiche les informations d'un bénévole.
     * Accepte un ID numérique ou un nom/prénom/identifiant.
     * 
     * @param input L'ID ou le nom du bénévole recherché.
     */
    public void chercherBenevole(String input) {
        String q = (input == null) ? "" : input.trim();
        if (q.isEmpty()) {
            System.out.println("Erreur : precisez un id, un prenom, un nom ou un identifiant.");
            return;
        }

        try {
            int id = Integer.parseInt(q);
            Personnel p = personnelReq.getById(id);
            if (p == null) {
                System.out.println("Benevole introuvable : #" + id);
                return;
            }
            if (!isBenevoleType(p.getType_pers())) {
                System.out.println(
                        "Info : la personne #" + id + " n'est pas un benevole (type=" + p.getType_pers() + ").");
                return;
            }
            System.out.println("=== BENEVOLE #" + id + " ===");
            System.out.println("Nom      : " + p.getNom());
            System.out.println("Prenom   : " + p.getPrenom());
            System.out.println("Tel      : " + p.getTel());
            System.out.println("User     : " + p.getUser());
            return;
        } catch (NumberFormatException ignore) {
        }

        List<Personnel> res = personnelReq.searchBenevoles(q);
        System.out.println("--- Recherche benevole : '" + q + "' ---");
        if (res.isEmpty()) {
            System.out.println("Aucun benevole trouve.");
            return;
        }

        System.out.printf("%-5s | %-15s | %-15s | %-15s | %s%n", "ID", "Nom", "Prenom", "Tel", "User");
        System.out.println("--------------------------------------------------------------------------");
        for (Personnel p : res) {
            System.out.printf("%-5d | %-15s | %-15s | %-15s | %s%n",
                    p.getId_pers(), safe(p.getNom()), safe(p.getPrenom()), safe(p.getTel()), safe(p.getUser()));
        }
    }

    /**
     * Normalise une chaîne en retirant les accents et en la passant en minuscules.
     */
    private static String normalizeNoAccentLower(String s) {
        if (s == null)
            return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        n = n.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return n.trim().toLowerCase();
    }

    /**
     * Vérifie si le type correspond à "Bénévole".
     */
    private static boolean isBenevoleType(String type) {
        return "benevole".equals(normalizeNoAccentLower(type));
    }

    /**
     * Retourne une valeur par défaut si la chaîne est null.
     */
    private String safe(String s) {
        return (s == null) ? "-" : s;
    }

    /**
     * Modifie un bénévole existant via saisie interactive.
     * Les champs laissés vides conservent leur valeur actuelle.
     * 
     * @param id      L'identifiant du bénévole à modifier.
     * @param scanner Le scanner pour la saisie utilisateur.
     */
    public void modifierBenevole(int id, Scanner scanner) {
        Personnel p = personnelReq.getById(id);
        if (p == null) {
            System.out.println("Bénévole #" + id + " introuvable.");
            return;
        }
        if (!isBenevoleType(p.getType_pers())) {
            System.out
                    .println("Attention : L'ID " + id + " correspond à un " + p.getType_pers() + ", pas un bénévole.");
        }

        System.out.println("Modification du bénévole : " + p.getPrenom() + " " + p.getNom());
        System.out.println("(Laissez vide pour ne pas changer)");

        System.out.print("Nouveau Nom (" + p.getNom() + ") : ");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty())
            p.setNom(nom);

        System.out.print("Nouveau Prénom (" + p.getPrenom() + ") : ");
        String prenom = scanner.nextLine().trim();
        if (!prenom.isEmpty())
            p.setPrenom(prenom);

        System.out.print("Nouveau Tel (" + p.getTel() + ") : ");
        String tel = scanner.nextLine().trim();
        if (!tel.isEmpty())
            p.setTel(tel);

        System.out.print("Nouveau User (" + p.getUser() + ") : ");
        String user = scanner.nextLine().trim();
        if (!user.isEmpty())
            p.setUser(user);

        System.out.print("Nouveau Pass (?) : ");
        String pass = scanner.nextLine().trim();
        if (!pass.isEmpty())
            p.setPassword(pass);

        personnelReq.update(p);
    }

    /**
     * Supprime un bénévole du système.
     * 
     * @param id L'identifiant du bénévole à supprimer.
     */
    public void supprimerBenevole(int id) {
        Personnel p = personnelReq.getById(id);
        if (p == null) {
            System.out.println("Bénévole #" + id + " introuvable.");
            return;
        }

        System.out.println("Suppression de " + p.getPrenom() + " " + p.getNom() + "...");
        personnelReq.delete(id);
    }
}
