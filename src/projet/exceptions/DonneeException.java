package projet.exceptions;

/**
 * Signale un problème lié à l'accès ou à l'intégrité des données.
 * <p>
 * Exemple : Rechercher un ID qui n'existe pas, ou problème de connexion SQL.
 * </p>
 */
public class DonneeException extends SpaException {
    public DonneeException(String message) {
        super("Erreur Donnée : " + message);
    }
}