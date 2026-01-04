package projet.exceptions;

/**
 * Levée lorsqu'une saisie utilisateur ne respecte pas le format attendu.
 * (Ex: Présence de chiffres dans un nom propre).
 */
public class InvalidFormatException extends MetierException {

    public InvalidFormatException(String champ, String valeurFausse) {
        super("InvalidFormatException : Format invalide pour le champ [" + champ + "]. La valeur '" + valeurFausse
                + "' contient des caractères interdits.");
    }
}