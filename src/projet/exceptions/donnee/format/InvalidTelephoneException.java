package projet.exceptions.donnee.format;

/**
 * Levée lorsqu'un numéro de téléphone ne respecte pas le format attendu.
 * <p>
 * Le téléphone doit être au format français (10 chiffres commençant par 0).
 * </p>
 */
public class InvalidTelephoneException extends InvalidFormatException {

    public InvalidTelephoneException(String valeur) {
        super("Téléphone", valeur);
    }

    public InvalidTelephoneException(String valeur, String raison) {
        super("Téléphone", valeur + " - " + raison);
    }
}
