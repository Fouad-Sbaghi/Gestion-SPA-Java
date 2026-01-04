package projet.exceptions.donnee.format;

import projet.exceptions.donnee.DonneeException;

/**
 * Levée lorsqu'une saisie utilisateur ne respecte pas le format attendu.
 * <p>
 * Exemple : Présence de chiffres dans un nom propre.
 * </p>
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