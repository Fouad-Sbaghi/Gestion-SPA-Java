package projet.exceptions;

/**
 * Levée lors d'un échec d'authentification utilisateur.
 * <p>
 * Peut être dû à un identifiant ou mot de passe incorrect,
 * ou à un compte inexistant/désactivé.
 * </p>
 */
public class AuthentificationException extends SpaException {

    /**
     * Constructeur par défaut.
     */
    public AuthentificationException() {
        super("Échec d'authentification : identifiant ou mot de passe incorrect.");
    }

    /**
     * Constructeur avec nom d'utilisateur.
     * 
     * @param username Le nom d'utilisateur ayant échoué.
     */
    public AuthentificationException(String username) {
        super("Échec d'authentification pour l'utilisateur '" + username + "'.");
    }

    /**
     * Constructeur avec nom d'utilisateur et raison.
     * 
     * @param username Le nom d'utilisateur ayant échoué.
     * @param raison   La raison de l'échec.
     */
    public AuthentificationException(String username, String raison) {
        super("Échec d'authentification pour '" + username + "' : " + raison);
    }
}
