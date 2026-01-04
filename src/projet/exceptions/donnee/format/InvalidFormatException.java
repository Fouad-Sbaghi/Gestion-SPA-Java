package projet.exceptions.donnee.format;

import projet.exceptions.donnee.DonneeException;

/**
 * Levée lorsqu'une saisie utilisateur ne respecte pas le format attendu.
 * (Ex: Présence de chiffres dans un nom propre).
 */
public class InvalidFormatException extends DonneeException {

    public InvalidFormatException(String champ, String valeurFausse) {
        super("InvalidFormatException : Format invalide pour le champ [" + champ + "]. La valeur '" + valeurFausse
                + "' contient des caractères interdits.");
    }
}