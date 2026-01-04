package projet.exceptions.donnee.format;

import java.util.Date;
import projet.exceptions.donnee.DonneeException;

/**
 * Levée lorsqu'une date fournie est dans le futur alors que ce n'est pas
 * autorisé.
 * <p>
 * Exemple : Date de naissance d'un animal supérieure à la date du jour.
 * </p>
 */
public class DateFutureException extends DonneeException {

    public DateFutureException(String champ, Date dateFournie) {
        super("DateFutureException : Le champ [" + champ + "] contient une date future (" + dateFournie.toString()
                + "), ce qui n'est pas autorisé.");
    }

    public DateFutureException(String champ, String dateFournie) {
        super("DateFutureException : Le champ [" + champ + "] contient une date future (" + dateFournie
                + "), ce qui n'est pas autorisé.");
    }
}
