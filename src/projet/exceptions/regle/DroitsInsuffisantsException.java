package projet.exceptions.regle;

/**
 * Levée lorsqu'un utilisateur tente une action pour laquelle il n'a pas les
 * droits.
 * <p>
 * Exemple : Un bénévole tente de supprimer un animal (action réservée aux
 * admins).
 * </p>
 */
public class DroitsInsuffisantsException extends RegleException {

    /**
     * Constructeur simple.
     * 
     * @param action     L'action tentée.
     * @param roleRequis Le rôle requis pour cette action.
     */
    public DroitsInsuffisantsException(String action, String roleRequis) {
        super("Droits insuffisants pour : " + action + ". Rôle requis : " + roleRequis + ".");
    }

    /**
     * Constructeur avec rôle actuel.
     * 
     * @param action     L'action tentée.
     * @param roleActuel Le rôle de l'utilisateur.
     * @param roleRequis Le rôle requis pour cette action.
     */
    public DroitsInsuffisantsException(String action, String roleActuel, String roleRequis) {
        super("Droits insuffisants pour : " + action + ". Vous êtes '" + roleActuel +
                "', rôle requis : '" + roleRequis + "'.");
    }
}
