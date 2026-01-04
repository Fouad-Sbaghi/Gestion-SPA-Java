package projet.exceptions.regle;

/**
 * Levée lorsqu'on tente d'affecter un animal inadoptable à une famille.
 * <p>
 * Un animal est considéré inadoptable si tous ses tests comportementaux
 * (humain, bébé, chien, chat) sont à FALSE.
 * </p>
 */
public class InadoptableException extends RegleException {

    /**
     * Constructeur avec ID et nom.
     * 
     * @param idAnimal  L'identifiant de l'animal.
     * @param nomAnimal Le nom de l'animal.
     */
    public InadoptableException(int idAnimal, String nomAnimal) {
        super("L'animal #" + idAnimal + " (" + nomAnimal + ") n'est pas adoptable. " +
                "Tous ses tests comportementaux sont négatifs.");
    }

    /**
     * Constructeur avec nom uniquement.
     * 
     * @param nomAnimal Le nom de l'animal.
     */
    public InadoptableException(String nomAnimal) {
        super("L'animal '" + nomAnimal + "' n'est pas adoptable. " +
                "Au moins un test comportemental doit être positif pour autoriser un placement en famille.");
    }
}
