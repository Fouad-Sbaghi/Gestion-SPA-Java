package projet.exceptions.donnee.format;

import java.util.Date;

/**
 * Levée lorsque deux dates sont incohérentes entre elles.
 * <p>
 * Exemple : Date de fin antérieure à la date de début d'un séjour.
 * </p>
 */
public class DateIncoherenteException extends InvalidDateException {

    public DateIncoherenteException(Date dateDebut, Date dateFin) {
        super("Dates", dateDebut + " -> " + dateFin,
                "La date de fin ne peut pas être antérieure à la date de début");
    }

    public DateIncoherenteException(String champDebut, String champFin, String raison) {
        super("Plage de dates", champDebut + " / " + champFin, raison);
    }
}
