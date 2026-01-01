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
            System.out.println("Erreur : " + e.getMessage());
        } catch (IncompatibiliteTypeException e) {
            System.out.println("Erreur : " + e.getMessage());
        } catch (MissingEntityException e) {
            System.out.println("Erreur : " + e.getMessage());
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
}
