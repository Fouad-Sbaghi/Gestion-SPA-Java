package projet.gestion;

import projet.requests.BoxRequest;
import projet.requests.SejourBoxRequest;
import projet.requests.rapports.RapportBoxRequest;
import projet.exceptions.BoxPleinException;
import projet.exceptions.IncompatibiliteTypeException;
import projet.exceptions.MissingEntityException;

public class ControllerBox {

    private BoxRequest boxReq;
    private SejourBoxRequest sejourBoxReq;
    private RapportBoxRequest rapportBox;

    public ControllerBox() {
        this.boxReq = new BoxRequest();
        this.sejourBoxReq = new SejourBoxRequest();
        this.rapportBox = new RapportBoxRequest();
    }

    public void listerBox() {
        System.out.println("--- Etat actuel des Box ---");
        boxReq.afficherOccupation();
    }

    public void infoBox(int idBox) {
        boxReq.afficherInfoBox(idBox);
    }

    public void rapportAvanceBox() {
        System.out.println("--- Statistiques detaillees des Box ---");
        rapportBox.afficherStatistiques();
    }

    public void placerAnimalBox(int idBox, int idAnimal) {
        try {
            sejourBoxReq.placerAnimal(idAnimal, idBox);
        } catch (BoxPleinException e) {
            System.out.println(e.getMessage());
        } catch (IncompatibiliteTypeException e) {
            System.out.println(e.getMessage());
        } catch (MissingEntityException e) {
            System.out.println(e.getMessage());
        }
    }

    public void viderBox(int idBox) {
        int rows = sejourBoxReq.viderBox(idBox);
        if (rows > 0) {
            System.out.println("Succes : " + rows + " sejour(s) cloture(s) pour le box #" + idBox + ".");
        } else {
            System.out.println("Info : Aucun sejour actif trouve pour le box #" + idBox + ".");
        }
    }

    public void ajouterBox(java.util.Scanner scanner) {
        System.out.println(">> Ajout d'un nouveau Box");
        System.out.print("Type (Chien/Chat) : ");
        String type = scanner.nextLine().trim();
        System.out.print("Capacité max : ");
        String capStr = scanner.nextLine().trim();

        try {
            int cap = Integer.parseInt(capStr);
            projet.tables.Box box = new projet.tables.Box(0, type, cap);
            boxReq.add(box);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : La capacité doit être un nombre entier.");
        }
    }

    public void updateBox(int idBox, java.util.Scanner scanner) {
        System.out.println(">> Modification du Box #" + idBox);
        System.out.print("Nouveau Type (Chien/Chat) : ");
        String type = scanner.nextLine().trim();
        System.out.print("Nouvelle Capacité max : ");
        String capStr = scanner.nextLine().trim();

        try {
            int cap = Integer.parseInt(capStr);
            projet.tables.Box box = new projet.tables.Box(idBox, type, cap);
            boxReq.update(box);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : La capacité doit être un nombre entier.");
        }
    }

    public void supprimerBox(int idBox) {
        // 1. Verifier si le box est vide (occupants actuels)
        int occupants = sejourBoxReq.getNbOccupants(idBox);
        if (occupants > 0) {
            System.out.println("Erreur : Le box #" + idBox + " n'est pas vide (" + occupants + " animaux présents).");
            System.out.println("Veuillez vider le box avant de le supprimer.");
            return;
        }

        // 2. Supprimer l'historique des séjours (FK constraint)
        sejourBoxReq.supprimerHistoriqueBox(idBox);

        // 3. Supprimer le box
        if (boxReq.delete(idBox)) {
            System.out.println("Box #" + idBox + " supprimé avec succès.");
        } else {
            System.out.println("Échec de la suppression du Box #" + idBox + ".");
        }
    }
}
