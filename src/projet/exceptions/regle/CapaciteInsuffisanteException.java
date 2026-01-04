package projet.exceptions.regle;

/**
 * Levée lorsqu'une capacité maximale est atteinte (version générique).
 * <p>
 * Utilisable pour les créneaux, activités, ou tout autre élément avec capacité
 * limitée.
 * Pour les box, préférer BoxPleinException.
 * </p>
 */
public class CapaciteInsuffisanteException extends RegleException {

    public CapaciteInsuffisanteException(String element, int capaciteMax) {
        super("Capacité maximale atteinte pour " + element + " (max: " + capaciteMax + ").");
    }

    public CapaciteInsuffisanteException(String element, int actuel, int max) {
        super("Capacité insuffisante pour " + element + " : " + actuel + "/" + max + " places occupées.");
    }
}
