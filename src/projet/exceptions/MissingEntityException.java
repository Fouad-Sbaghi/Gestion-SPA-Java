package projet.exceptions;

public class MissingEntityException extends DonneeException {

    // Constructeur pour dire "L'animal avec l'ID 5 n'existe pas"
    public MissingEntityException(String typeEntite, int id) {
        super("Impossible de trouver l'entité [" + typeEntite + "] avec l'identifiant " + id + ".");
    }
    
    // Constructeur alternatif si on cherche par nom ou autre
    public MissingEntityException(String message) {
        super(message);
    }
}