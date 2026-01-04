package projet.exceptions.donnee;

/**
 * Levée lors d'une tentative d'insertion d'une donnée censée être unique (ex:
 * Numéro de puce).
 * Indique un conflit d'intégrité.
 */
public class DuplicatedIdException extends DonneeException {

    public DuplicatedIdException(String valeur) {
        super("DuplicatedIdException : L'identifiant ou la puce '" + valeur + "' existe déjà dans la base.");
    }
}