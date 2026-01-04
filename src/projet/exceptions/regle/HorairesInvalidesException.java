package projet.exceptions.regle;

import java.sql.Time;

/**
 * Levée lorsque les horaires d'un créneau sont incohérents.

 * Exemple : Heure de fin antérieure à l'heure de début.

 */
public class HorairesInvalidesException extends RegleException {

    /**
     * Constructeur avec objets Time.
     * 
     * @param heureDebut L'heure de début.
     * @param heureFin   L'heure de fin (antérieure au début).
     */
    public HorairesInvalidesException(Time heureDebut, Time heureFin) {
        super("Horaires invalides : " + heureDebut + " -> " + heureFin +
                ". L'heure de fin doit être postérieure à l'heure de début.");
    }

    /**
     * Constructeur avec chaînes.
     * 
     * @param heureDebut L'heure de début.
     * @param heureFin   L'heure de fin.
     */
    public HorairesInvalidesException(String heureDebut, String heureFin) {
        super("Horaires invalides : " + heureDebut + " -> " + heureFin +
                ". L'heure de fin doit être postérieure à l'heure de début.");
    }

    /**
     * Constructeur avec message personnalisé.
     * 
     * @param message Description de l'erreur.
     */
    public HorairesInvalidesException(String message) {
        super("Horaires invalides : " + message);
    }
}
