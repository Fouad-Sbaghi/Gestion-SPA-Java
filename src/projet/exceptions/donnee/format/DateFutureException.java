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

    /**
     * Constructeur avec objet Date.
     * 
     * @param champ       Le nom du champ date.
     * @param dateFournie La date future fournie.
     */
    public DateFutureException(String champ, Date dateFournie) {
        super("DateFutureException : Le champ [" + champ + "] contient une date future (" + dateFournie.toString()
                + "), ce qui n'est pas autorisé.");
    }

    /**
     * Constructeur avec chaîne de date.
     * 
     * @param champ       Le nom du champ date.
     * @param dateFournie La date future sous forme de chaîne.
     */
    public DateFutureException(String champ, String dateFournie) {
        super("DateFutureException : Le champ [" + champ + "] contient une date future (" + dateFournie
                + "), ce qui n'est pas autorisé.");
    }
}
