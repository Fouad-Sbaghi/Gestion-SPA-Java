package projet.exceptions.regle;

import projet.exceptions.donnee.DonneeException;

/**
 * Levée lorsqu'une opération est demandée sur une entité (Animal, Box, etc.)
 * qui n'existe pas ou plus dans la base de données.
 */
public class MissingEntityException extends DonneeException {

    public MissingEntityException(String typeEntite, int id) {
        super("MissingEntityException : Impossible de trouver l'entité [" + typeEntite + "] avec l'identifiant " + id
                + ".");
    }

    public MissingEntityException(String message) {
        super("MissingEntityException : " + message);
    }
}