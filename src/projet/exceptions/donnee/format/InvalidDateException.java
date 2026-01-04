package projet.exceptions.donnee.format;

/**
 * Levée lorsqu'une date saisie est invalide ou dans un format incorrect.
 */
public class InvalidDateException extends InvalidFormatException {

    /**
     * Constructeur avec valeur incorrecte.
     * 
     * @param valeurFausse La date invalide saisie.
     */
    public InvalidDateException(String valeurFausse) {
        super("Date", valeurFausse);
    }

    /**
     * Constructeur avec champ, valeur et raison.
     * 
     * @param champ        Le nom du champ date.
     * @param valeurFausse La valeur invalide.
     * @param raison       La raison de l'invalidité.
     */
    public InvalidDateException(String champ, String valeurFausse, String raison) {
        super(champ, valeurFausse + " (" + raison + ")");
    }
}
