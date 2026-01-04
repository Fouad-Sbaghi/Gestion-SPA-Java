package projet.exceptions.donnee.format;

import java.util.Date;

/**
 * Levée lorsqu'une date fournie est dans le futur alors que ce n'est pas
 * autorisé.
 * <p>
 * Exemple : Date de naissance d'un animal supérieure à la date du jour.
 * </p>
 */
public class DateFutureException extends InvalidDateException {

    public DateFutureException(String champ, Date dateFournie) {
        super(champ, dateFournie.toString(), "La date ne peut pas être dans le futur");
    }

    public DateFutureException(String champ, String dateFournie) {
        super(champ, dateFournie, "La date ne peut pas être dans le futur");
    }
}
