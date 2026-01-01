package projet.exceptions;

/**
 * Levée lorsqu'on tente d'ajouter un animal dans un Box dont la capacité maximale est atteinte.
 */
public class BoxPleinException extends MetierException {
    
    public BoxPleinException(int idBox, int capacite) {
        super("Le box n°" + idBox + " est complet (Max: " + capacite + "). Ajout impossible.");
    }
}