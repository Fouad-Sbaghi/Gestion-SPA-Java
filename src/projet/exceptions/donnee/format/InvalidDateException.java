package projet.exceptions.donnee.format;

/**
 * Levée lorsqu'une date saisie est invalide ou dans un format incorrect.
 * Hérite de InvalidFormatException pour garder la cohérence avec les erreurs de
 * format.
 */
public class InvalidDateException extends InvalidFormatException {

    public InvalidDateException(String valeurFausse) {
        super("Date", valeurFausse);
    }

    public InvalidDateException(String champ, String valeurFausse, String raison) {
        super(champ, valeurFausse + " (" + raison + ")");
    }
}
