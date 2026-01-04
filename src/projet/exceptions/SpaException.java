package projet.exceptions;

/**
 * Exception mère pour toutes les erreurs métier de l'application.
 */
public abstract class SpaException extends Exception {

    /**
     * Constructeur forçant la présence d'un message d'erreur.
     * 
     * @param message Description de l'erreur.
     */
    public SpaException(String message) {
        super("Exception : SpaException : " + message);
    }
}