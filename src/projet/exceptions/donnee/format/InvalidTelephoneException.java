package projet.exceptions.donnee.format;

/**
 * Levée quand le téléphone n'a pas le format français (10 chiffres, commence
 * par 0).
 */
public class InvalidTelephoneException extends InvalidFormatException {

    /**
     * Constructeur simple.
     * 
     * @param valeur Le numéro invalide.
     */
    public InvalidTelephoneException(String valeur) {
        super("Téléphone", valeur);
    }

    /**
     * Constructeur avec raison.
     * 
     * @param valeur Le numéro invalide.
     * @param raison La raison de l'invalidité.
     */
    public InvalidTelephoneException(String valeur, String raison) {
        super("Téléphone", valeur + " - " + raison);
    }
}
