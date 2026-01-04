package projet.exceptions.donnee.format;

import projet.exceptions.donnee.DonneeException;

/**
 * Levée quand une saisie ne respecte pas le format attendu.
 * Ex: chiffres dans un nom propre.
 */
public class InvalidFormatException extends DonneeException {

    /**
     * Constructeur.
     * 
     * @param champ        Le nom du champ invalide.
     * @param valeurFausse La valeur saisie invalide.
     */
    public InvalidFormatException(String champ, String valeurFausse) {
        super("InvalidFormatException : Format invalide pour le champ [" + champ + "]. La valeur '" + valeurFausse
                + "' contient des caractères interdits.");
    }
}