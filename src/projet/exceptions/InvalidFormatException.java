package projet.exceptions;

public class InvalidFormatException extends MetierException {

    public InvalidFormatException(String champ, String valeurFausse) {
        super("Format invalide pour le champ [" + champ + "]. La valeur '" + valeurFausse + "' contient des caractères interdits (chiffres ou spéciaux).");
    }
}