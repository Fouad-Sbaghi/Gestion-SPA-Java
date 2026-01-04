package projet.exceptions.donnee.format;

/**
 * Levée quand la puce n'a pas le bon format (10 chiffres attendus).
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
