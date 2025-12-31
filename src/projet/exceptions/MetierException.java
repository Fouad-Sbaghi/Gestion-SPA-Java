package projet.exceptions;

public class MetierException extends SpaException {
    public MetierException(String message) {
        super("Règle Métier : " + message);
    }
}