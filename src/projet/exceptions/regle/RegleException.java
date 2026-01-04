package projet.exceptions.regle;

import projet.exceptions.SpaException;

/**
 * Signale une violation d'une règle métier de la SPA.
 * <p>
 * Exemple : Tenter de mettre un animal dans un box plein,
 * ou affecter un animal inadoptable à une famille.
 * </p>
 */
public class RegleException extends SpaException {

    /**
     * Constructeur.
     * 
     * @param message Description de la violation de règle.
     */
    public RegleException(String message) {
        super("RegleException : " + message);
    }
}
