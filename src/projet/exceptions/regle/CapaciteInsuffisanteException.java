package projet.exceptions.regle;

/**
 * Levée lorsqu'une capacité maximale est atteinte (version générique).

 * Utilisable pour les créneaux, activités, ou tout autre élément avec capacité
 * limitée.
 * Pour les box, préférer BoxPleinException.

 */
public class CapaciteInsuffisanteException extends RegleException {

    /**
     * Constructeur simple.
     * 
     * @param element     L'élément concerné.
     * @param capaciteMax La capacité maximale.
     */
    public CapaciteInsuffisanteException(String element, int capaciteMax) {
        super("Capacité maximale atteinte pour " + element + " (max: " + capaciteMax + ").");
    }

    /**
     * Constructeur avec occupation actuelle.
     * 
     * @param element L'élément concerné.
     * @param actuel  Le nombre actuel d'occupants.
     * @param max     La capacité maximale.
     */
    public CapaciteInsuffisanteException(String element, int actuel, int max) {
        super("Capacité insuffisante pour " + element + " : " + actuel + "/" + max + " places occupées.");
    }
}
