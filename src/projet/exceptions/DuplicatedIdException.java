package projet.exceptions;

public class DuplicatedIdException extends MetierException {

    public DuplicatedIdException(String valeur) {
        super("Conflit de données : L'identifiant ou la puce '" + valeur + "' existe déjà dans la base.");
    }
}