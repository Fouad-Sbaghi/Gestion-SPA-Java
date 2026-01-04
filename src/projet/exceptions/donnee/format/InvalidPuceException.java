package projet.exceptions.donnee.format;

/**
 * Levée lorsqu'un numéro de puce ne respecte pas le format attendu.
 * <p>
 * La puce doit être composée uniquement de chiffres et avoir une longueur
 * valide.
 * </p>
 */
public class InvalidPuceException extends InvalidFormatException {

    public InvalidPuceException(String valeur) {
        super("Puce", valeur);
    }

    public InvalidPuceException(String valeur, String raison) {
        super("Puce", valeur + " - " + raison);
    }
}
