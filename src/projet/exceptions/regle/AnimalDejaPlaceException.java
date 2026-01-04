package projet.exceptions.regle;

/**
 * Levée lorsqu'on tente de placer un animal qui est déjà hébergé ailleurs.
 * <p>
 * Un animal ne peut pas être dans deux endroits à la fois (box + famille).
 * </p>
 */
public class AnimalDejaPlaceException extends RegleException {

    public AnimalDejaPlaceException(int idAnimal, String lieuActuel) {
        super("L'animal #" + idAnimal + " est déjà placé : " + lieuActuel +
                ". Terminez le séjour actuel avant de le déplacer.");
    }

    public AnimalDejaPlaceException(String nomAnimal, String lieuActuel) {
        super("L'animal '" + nomAnimal + "' est déjà placé : " + lieuActuel +
                ". Terminez le séjour actuel avant de le déplacer.");
    }
}
