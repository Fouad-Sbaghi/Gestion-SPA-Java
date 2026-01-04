package projet.exceptions;

/**
 * Levée lorsque la connexion à la base de données échoue.
 * <p>
 * Indique un problème technique (serveur inaccessible, timeout,
 * mauvaise configuration, etc.).
 * </p>
 */
public class ConnexionBDDException extends SpaException {

    public ConnexionBDDException() {
        super("Impossible de se connecter à la base de données.");
    }

    public ConnexionBDDException(String details) {
        super("Erreur de connexion BDD : " + details);
    }

    public ConnexionBDDException(Throwable cause) {
        super("Erreur de connexion BDD : " + cause.getMessage());
    }
}
