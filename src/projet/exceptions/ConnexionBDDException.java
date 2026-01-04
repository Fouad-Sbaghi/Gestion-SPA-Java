package projet.exceptions;

/**
 * Levée lorsque la connexion à la base de données échoue.

 * Indique un problème technique (serveur inaccessible, timeout,
 * mauvaise configuration, etc.).

 */
public class ConnexionBDDException extends SpaException {

    /**
     * Constructeur par défaut.
     */
    public ConnexionBDDException() {
        super("Impossible de se connecter à la base de données.");
    }

    /**
     * Constructeur avec détails.
     * 
     * @param details Les détails de l'erreur.
     */
    public ConnexionBDDException(String details) {
        super("Erreur de connexion BDD : " + details);
    }

    /**
     * Constructeur avec cause.
     * 
     * @param cause L'exception d'origine.
     */
    public ConnexionBDDException(Throwable cause) {
        super("Erreur de connexion BDD : " + cause.getMessage());
    }
}
