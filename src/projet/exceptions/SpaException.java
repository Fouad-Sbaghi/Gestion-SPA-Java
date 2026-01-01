package projet.exceptions;

/**
 * Exception racine abstraite pour l'application de gestion SPA.
 * <p>
 * Regroupe toutes les erreurs contrôlées spécifiques au projet,
 * qu'elles soient d'ordre technique (BDD) ou métier (Règles logiques).
 * </p>
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