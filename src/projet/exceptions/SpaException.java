package projet.exceptions;

public abstract class SpaException extends Exception {
    // Constructeur qui force à avoir un message d'erreur
    public SpaException(String message) {
        super(message);
    }
}