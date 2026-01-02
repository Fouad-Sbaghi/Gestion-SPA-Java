package projet.gestion;

import java.util.List;
import java.util.Scanner;

import projet.requests.AnimalRequest;
import projet.requests.FamilleRequest;
import projet.requests.SejourFamilleRequest;
import projet.tables.Animal;
import projet.tables.Famille;

public class ControllerFamille {

    private FamilleRequest familyReq;
    private SejourFamilleRequest sejourFamilleReq;
    private AnimalRequest animalReq;

    public ControllerFamille() {
        this.familyReq = new FamilleRequest();
        this.sejourFamilleReq = new SejourFamilleRequest();
        this.animalReq = new AnimalRequest();
    }

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

    public void listerFamilles() {
        System.out.println("--- Liste des Familles ---");
        List<Famille> liste = familyReq.getAll();
        for (Famille f : liste) {
            System.out.printf("[%d] %s (%s) - %s%n", f.getId_famille(), f.getNom(), f.getType_famille(),
                    f.getAdresse());
        }
    }

    public void lierFamille(int idAnimal, int idFamille, String type) {
        // AJOUT : Sortir l'animal du box s'il y est
        projet.requests.SejourBoxRequest boxReq = new projet.requests.SejourBoxRequest();
        boxReq.sortirAnimal(idAnimal);

        if (sejourFamilleReq.commencerSejour(idAnimal, idFamille)) {
            try {
                Animal a = animalReq.getById(idAnimal);
                if (a != null) {
                    // Normalisation des statuts
                    String nouveauStatut = "Famille";
                    if (type != null && type.equalsIgnoreCase("Adoption")) {
                        nouveauStatut = "Adopté";
                    }

                    a.setStatut(nouveauStatut);
                    animalReq.update(a);
                    System.out.println("Statut animal mis a jour : " + a.getStatut());
                }
            } catch (Exception e) {
                System.out.println("Sejour cree, mais erreur maj statut.");
            }
        }
    }

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
        }
    }

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

    private String safe(String s) {
        return (s == null) ? "-" : s;
    }

    private String truncate(String s, int max) {
        if (s == null)
            return "-";
        if (s.length() <= max)
            return s;
        return s.substring(0, Math.max(0, max - 1)) + "...";
    }
}
