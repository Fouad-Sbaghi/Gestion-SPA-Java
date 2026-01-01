package projet.exceptions;

/**
 * Levée lors d'une tentative d'insertion d'une donnée censée être unique (ex: Numéro de puce).
 * Indique un conflit d'intégrité.
 */
public class DuplicatedIdException extends MetierException {

    public DuplicatedIdException(String valeur) {
        super("Conflit de données : L'identifiant ou la puce '" + valeur + "' existe déjà dans la base.");
    }
}