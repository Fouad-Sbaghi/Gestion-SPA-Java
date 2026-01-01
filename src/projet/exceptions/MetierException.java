package projet.exceptions;

/**
 * Signale une violation d'une règle métier de la SPA.
 * <p>
 * Exemple : Tenter de mettre un animal dans un box plein,
 * ou donner un nom contenant des chiffres.
 * </p>
 */
public class MetierException extends SpaException {
    public MetierException(String message) {
        super("MetierException : " + message);
    }
}