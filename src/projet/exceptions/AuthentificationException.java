package projet.exceptions;

/**
 * Levée lors d'un échec d'authentification utilisateur.
 * <p>
 * Peut être dû à un identifiant ou mot de passe incorrect,
 * ou à un compte inexistant/désactivé.
 * </p>
 */
public class AuthentificationException extends SpaException {

    public AuthentificationException() {
        super("Échec d'authentification : identifiant ou mot de passe incorrect.");
    }

    public AuthentificationException(String username) {
        super("Échec d'authentification pour l'utilisateur '" + username + "'.");
    }

    public AuthentificationException(String username, String raison) {
        super("Échec d'authentification pour '" + username + "' : " + raison);
    }
}
