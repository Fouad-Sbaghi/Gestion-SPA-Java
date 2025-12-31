package projet.exceptions;

public class BoxPleinException extends MetierException {
    public BoxPleinException(int idBox, int capacite) {
        super("Le box n°" + idBox + " est complet (Max: " + capacite + "). Ajout impossible.");
    }
}