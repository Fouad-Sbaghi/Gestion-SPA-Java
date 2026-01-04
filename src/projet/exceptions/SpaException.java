package projet.exceptions;

/**
 * Exception mère abstraite pour toutes les erreurs de l'application.
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