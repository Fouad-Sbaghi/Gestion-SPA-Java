package projet.exceptions;

public class DonneeException extends SpaException {
    public DonneeException(String message) {
        super("Erreur Donnée : " + message);
    }
}