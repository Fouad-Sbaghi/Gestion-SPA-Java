package projet.exceptions.regle;

import projet.exceptions.donnee.DonneeException;

/**
 * Levée lorsqu'une opération est demandée sur une entité qui n'existe pas.
 */
public class MissingEntityException extends DonneeException {

    /**
     * Constructeur avec type et ID.
     * 
     * @param typeEntite Le type d'entité (Animal, Box, etc.).
     * @param id         L'identifiant introuvable.
     */
    public MissingEntityException(String typeEntite, int id) {
        super("MissingEntityException : Impossible de trouver l'entité [" + typeEntite + "] avec l'identifiant " + id
                + ".");
    }

    /**
     * Constructeur avec message.
     * 
     * @param message Description de l'erreur.
     */
    public MissingEntityException(String message) {
        super("MissingEntityException : " + message);
    }
}