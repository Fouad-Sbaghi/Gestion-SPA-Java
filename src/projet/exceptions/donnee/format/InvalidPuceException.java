package projet.exceptions.donnee.format;

/**
 * Levée lorsqu'un numéro de puce ne respecte pas le format attendu.
 * <p>
 * La puce doit être composée de 10 chiffres.
 * </p>
 */
public class InvalidPuceException extends InvalidFormatException {

    /**
     * Constructeur simple.
     * 
     * @param valeur La valeur de puce invalide.
     */
    public InvalidPuceException(String valeur) {
        super("Puce", valeur);
    }

    /**
     * Constructeur avec raison.
     * 
     * @param valeur La valeur de puce invalide.
     * @param raison La raison de l'invalidité.
     */
    public InvalidPuceException(String valeur, String raison) {
        super("Puce", valeur + " - " + raison);
    }
}
