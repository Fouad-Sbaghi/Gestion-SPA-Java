package projet.exceptions.donnee.format;

import java.util.Date;

/**
 * Levée quand deux dates sont incohérentes.
 * Ex: date de fin avant la date de début.
 */
public class DateIncoherenteException extends InvalidDateException {

    /**
     * Constructeur avec objets Date.
     * 
     * @param dateDebut La date de début.
     * @param dateFin   La date de fin (antérieure au début).
     */
    public DateIncoherenteException(Date dateDebut, Date dateFin) {
        super("Dates", dateDebut + " -> " + dateFin,
                "La date de fin ne peut pas être antérieure à la date de début");
    }

    /**
     * Constructeur avec noms de champs et raison.
     * 
     * @param champDebut Le nom du champ date de début.
     * @param champFin   Le nom du champ date de fin.
     * @param raison     La raison de l'incohérence.
     */
    public DateIncoherenteException(String champDebut, String champFin, String raison) {
        super("Plage de dates", champDebut + " / " + champFin, raison);
    }
}
