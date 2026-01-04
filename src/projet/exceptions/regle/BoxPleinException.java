package projet.exceptions.regle;

/**
 * Levée lorsqu'on tente d'ajouter un animal dans un Box dont la capacité
 * maximale est atteinte.
 *
 *
 *
 *
 */
public class BoxPleinException extends RegleException {

    public BoxPleinException(int idBox, int capacite) {
        super("BoxPleinException : Le box n°" + idBox + " est complet (Max: " + capacite + "). Ajout impossible.");
    }
}