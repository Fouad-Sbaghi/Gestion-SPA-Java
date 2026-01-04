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

    public DroitsInsuffisantsException(String action, String roleRequis) {
        super("Droits insuffisants pour : " + action + ". Rôle requis : " + roleRequis + ".");
    }

    public DroitsInsuffisantsException(String action, String roleActuel, String roleRequis) {
        super("Droits insuffisants pour : " + action + ". Vous êtes '" + roleActuel +
                "', rôle requis : '" + roleRequis + "'.");
    }
}
