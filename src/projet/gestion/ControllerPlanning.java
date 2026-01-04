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

public class ControllerPlanning {

    private CreneauRequest creneauReq;
    private PersonnelRequest personnelReq;
    private PlanningAnimalRequest planningAnimalReq;
    private AffectationCreneauActiviteRequest affectationReq;
    private RapportPlanningRequest rapportPlanning;

    public ControllerPlanning() {
        this.creneauReq = new CreneauRequest();
        this.personnelReq = new PersonnelRequest();
        this.planningAnimalReq = new PlanningAnimalRequest();
        this.affectationReq = new AffectationCreneauActiviteRequest();
        this.rapportPlanning = new RapportPlanningRequest();
    }

    /**
     * Valide le format du numéro de téléphone (10 chiffres, peut commencer par 0).
     */
    private void validerTelephone(String tel) throws InvalidTelephoneException {
        if (tel != null && !tel.isEmpty() && !tel.matches("^0[1-9][0-9]{8}$")) {
            throw new InvalidTelephoneException(tel, "Le format attendu est 10 chiffres commençant par 0");
        }
    }

    public void ajouterBenevole(Scanner scanner) {
        try {
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
        } catch (InvalidTelephoneException e) {
            System.out.println(e.getMessage());
        }
    }

    public void afficherPlanning() {
        System.out.println("--- Planning General ---");
        List<?> liste = creneauReq.getAll();
        System.out.println("Creneaux configures : " + liste.size());
        affectationReq.listerAffectations();
    }

    public void checkSousEffectif() {
        System.out.println("--- ALERTE : Creneaux en sous-effectif ---");
        List<String> alertes = rapportPlanning.getCreneauxManquants();
        if (alertes.isEmpty())
            System.out.println("R.A.S. Planning complet.");
        else
            for (String alerte : alertes)
                System.out.println("! " + alerte);
    }

    public void assignerBenevole(int idCreneau, int idBenevole) {
        try {
            int idActiviteDefaut = 1;
            affectationReq.assigner(idCreneau, idBenevole, idActiviteDefaut);
        } catch (projet.exceptions.regle.CapaciteInsuffisanteException e) {
            System.out.println(e.getMessage());
        }
    }

    public void planningDuBenevole(int idBenevole) {
        Personnel p = personnelReq.getById(idBenevole);

        if (p == null) {
            System.out.println("Benevole introuvable (ID " + idBenevole + ").");
            return;
        }

        System.out.println("\nPlanning de : " + p.getPrenom() + " " + p.getNom() + " (ID " + idBenevole + ")");
        affectationReq.afficherPlanningPersonne(idBenevole);
    }

    public void ajouterRdvAnimal(int idAnimal, int idCreneau, int idPers, Date date) {
        planningAnimalReq.assigner(idAnimal, idCreneau, idPers, date);
    }

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

    private static String normalizeNoAccentLower(String s) {
        if (s == null)
            return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        n = n.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return n.trim().toLowerCase();
    }

    private static boolean isBenevoleType(String type) {
        return "benevole".equals(normalizeNoAccentLower(type));
    }

    private String safe(String s) {
        return (s == null) ? "-" : s;
    }

    public void modifierBenevole(int id, Scanner scanner) {
        Personnel p = personnelReq.getById(id);
        if (p == null) {
            System.out.println("Bénévole #" + id + " introuvable.");
            return;
        }
        if (!isBenevoleType(p.getType_pers())) {
            System.out
                    .println("Attention : L'ID " + id + " correspond à un " + p.getType_pers() + ", pas un bénévole.");
            // On continue quand même ou on bloque ? Modifions-le quand même.
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

    public void supprimerBenevole(int id) {
        Personnel p = personnelReq.getById(id);
        if (p == null) {
            System.out.println("Bénévole #" + id + " introuvable.");
            return;
        }
        // Vérification optionnelle du type
        // if (!isBenevoleType(p.getType_pers())) { ... }

        System.out.println("Suppression de " + p.getPrenom() + " " + p.getNom() + "...");
        personnelReq.delete(id);
    }
}
