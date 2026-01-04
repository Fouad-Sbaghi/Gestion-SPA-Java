package projet.exceptions.regle;

import java.sql.Time;

/**
 * Levée lorsque les horaires d'un créneau sont incohérents.
 * <p>
 * Exemple : Heure de fin antérieure à l'heure de début.
 * </p>
 */
public class HorairesInvalidesException extends RegleException {

    public HorairesInvalidesException(Time heureDebut, Time heureFin) {
        super("Horaires invalides : " + heureDebut + " -> " + heureFin +
                ". L'heure de fin doit être postérieure à l'heure de début.");
    }

    public HorairesInvalidesException(String heureDebut, String heureFin) {
        super("Horaires invalides : " + heureDebut + " -> " + heureFin +
                ". L'heure de fin doit être postérieure à l'heure de début.");
    }

    public HorairesInvalidesException(String message) {
        super("Horaires invalides : " + message);
    }
}
