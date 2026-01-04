package projet.exceptions.donnee;

import projet.exceptions.SpaException;

/**
 * Signale un problème lié à l'accès ou à l'intégrité des données.
 * <p>
 * Exemple : Rechercher un ID qui n'existe pas, ou problème de connexion SQL.
 * </p>
 */
public class DonneeException extends SpaException {

    /**
     * Constructeur avec message.
     * 
     * @param message Description de l'erreur.
     */
    public DonneeException(String message) {
        super("DonneeException : " + message);
    }
}