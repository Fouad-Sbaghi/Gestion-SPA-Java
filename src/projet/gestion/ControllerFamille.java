package projet.gestion;

import java.util.List;
import java.util.Scanner;

import projet.exceptions.regle.InadoptableException;
import projet.exceptions.regle.QuarantaineException;
import projet.requests.AnimalRequest;
import projet.requests.FamilleRequest;
import projet.requests.SejourFamilleRequest;
import projet.tables.Animal;
import projet.tables.Famille;

/**
 * Contrôleur gérant toutes les opérations liées aux familles d'accueil et
 * d'adoption.
 * <p>
 * Ce contrôleur permet de gérer les familles (ajout, liste, recherche),
 * de lier des animaux à des familles (accueil ou adoption), et de gérer
 * les retours d'animaux. Il vérifie les règles métier comme l'adoptabilité
 * et le statut de quarantaine.
 * </p>
 * 
 * @author Projet SPA
 * @version 1.0
 * @see projet.requests.FamilleRequest
 * @see projet.requests.SejourFamilleRequest
 */
public class ControllerFamille {

    private FamilleRequest familyReq;
    private SejourFamilleRequest sejourFamilleReq;
    private AnimalRequest animalReq;

    /**
     * Constructeur par défaut.
     * Initialise les requêtes pour les familles, séjours et animaux.
     */
    public ControllerFamille() {
        this.familyReq = new FamilleRequest();
        this.sejourFamilleReq = new SejourFamilleRequest();
        this.animalReq = new AnimalRequest();
    }

    /**
     * Ajoute une nouvelle famille via saisie interactive.
     * 
     * @param scanner Le scanner pour la saisie utilisateur.
     */
    public void ajouterFamille(Scanner scanner) {
        System.out.println(">> Creation Famille");
        Famille f = new Famille();

        System.out.print("Nom de famille : ");
        f.setNom(scanner.nextLine());
        System.out.print("Type (Accueil/Adoption) : ");
        f.setType_famille(scanner.nextLine());
        System.out.print("Adresse : ");
        f.setAdresse(scanner.nextLine());
        System.out.print("Contact : ");
        f.setContact(scanner.nextLine());

        familyReq.add(f);
    }

    /**
     * Affiche la liste de toutes les familles enregistrées.
     */
    public void listerFamilles() {
        System.out.println("--- Liste des Familles ---");
        List<Famille> liste = familyReq.getAll();
        for (Famille f : liste) {
            System.out.printf("[%d] %s (%s) - %s%n", f.getId_famille(), f.getNom(), f.getType_famille(),
                    f.getAdresse());
        }
    }

    /**
     * Lie un animal à une famille (accueil ou adoption).
     * Termine automatiquement tout séjour en box et met à jour le statut.
     * 
     * @param idAnimal  L'identifiant de l'animal.
     * @param idFamille L'identifiant de la famille.
     * @param type      Le type de séjour ("Accueil" ou "Adoption").
     */
    public void lierFamille(int idAnimal, int idFamille, String type) {
        try {
            projet.requests.SejourBoxRequest boxReq = new projet.requests.SejourBoxRequest();
            boxReq.sortirAnimal(idAnimal);

            if (sejourFamilleReq.commencerSejour(idAnimal, idFamille)) {
                Animal a = animalReq.getById(idAnimal);
                if (a != null) {
                    String nouveauStatut = "Famille";
                    if (type != null && type.equalsIgnoreCase("Adoption")) {
                        nouveauStatut = "Adopté";
                    }

                    a.setStatut(nouveauStatut);
                    animalReq.update(a);
                    System.out.println("Statut animal mis a jour : " + a.getStatut());
                }
            }
        } catch (InadoptableException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (QuarantaineException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Sejour cree, mais erreur maj statut: " + e.getMessage());
        }
    }

    /**
     * Gère le retour d'un animal depuis une famille.
     * Termine le séjour et repasse le statut à "Adoptable".
     * 
     * @param idAnimal L'identifiant de l'animal à retourner.
     */
    public void retourDeFamille(int idAnimal) {
        if (sejourFamilleReq.terminerSejour(idAnimal)) {
            try {
                Animal a = animalReq.getById(idAnimal);
                if (a != null) {
                    a.setStatut("Adoptable");
                    animalReq.update(a);
                    System.out.println("Statut animal repasse a : Adoptable");
                    System.out.println("Note : Pensez a replacer l'animal dans un box.");
                }
            } catch (Exception e) {
                System.out.println("Erreur maj statut.");
            }
        } else {
            System.out.println("Erreur : L'animal #" + idAnimal + " n'a pas de sejour actif en famille.");
        }
    }

    /**
     * Affiche l'historique des animaux accueillis par une famille.
     * 
     * @param idFamille L'identifiant de la famille.
     */
    public void historiqueFamille(int idFamille) {
        System.out.println("\n=== HISTORIQUE FAMILLE #" + idFamille + " ===");
        Famille f = familyReq.getById(idFamille);
        if (f != null) {
            System.out.println("Famille : " + f.getNom() + ", " + f.getAdresse());
            sejourFamilleReq.afficherHistoriqueParFamille(idFamille);
        } else {
            System.out.println("Famille introuvable.");
        }
    }

    /**
     * Recherche et affiche les informations d'une famille.
     * Accepte un ID numérique ou un nom de famille.
     * 
     * @param input L'ID ou le nom de la famille recherchée.
     */
    public void chercherFamille(String input) {
        String q = (input == null) ? "" : input.trim();
        if (q.isEmpty()) {
            System.out.println("Erreur : precisez un id ou un nom de famille.");
            return;
        }

        try {
            int id = Integer.parseInt(q);
            Famille f = familyReq.getById(id);
            if (f == null) {
                System.out.println("Famille introuvable : #" + id);
                return;
            }
            System.out.println("=== FAMILLE #" + id + " ===");
            System.out.println("Nom      : " + f.getNom());
            System.out.println("Type     : " + f.getType_famille());
            System.out.println("Adresse  : " + f.getAdresse());
            System.out.println("Contact  : " + f.getContact());
            return;
        } catch (NumberFormatException ignore) {
        }

        List<Famille> res = familyReq.getByName(q);
        System.out.println("--- Recherche famille : '" + q + "' ---");
        if (res.isEmpty()) {
            System.out.println("Aucune famille trouvee.");
            return;
        }

        System.out.printf("%-5s | %-20s | %-10s | %-25s | %s%n", "ID", "Nom", "Type", "Adresse", "Contact");
        System.out
                .println("------------------------------------------------------------------------------------------");
        for (Famille f : res) {
            System.out.printf("%-5d | %-20s | %-10s | %-25s | %s%n",
                    f.getId_famille(), safe(f.getNom()), safe(f.getType_famille()),
                    truncate(safe(f.getAdresse()), 25), safe(f.getContact()));
        }
    }

    /**
     * Retourne une valeur par défaut si la chaîne est null.
     */
    private String safe(String s) {
        return (s == null) ? "-" : s;
    }

    /**
     * Tronque une chaîne à la longueur maximale spécifiée.
     */
    private String truncate(String s, int max) {
        if (s == null)
            return "-";
        if (s.length() <= max)
            return s;
        return s.substring(0, Math.max(0, max - 1)) + "...";
    }
}
