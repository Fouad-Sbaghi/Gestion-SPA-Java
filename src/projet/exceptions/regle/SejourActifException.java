package projet.exceptions.regle;

/**
 * Levée lorsqu'une opération est interdite car l'animal a un séjour en cours.
 * <p>
 * Exemple : Supprimer un animal qui est actuellement dans un box ou une
 * famille.
 * </p>
 */
public class SejourActifException extends RegleException {

    public SejourActifException(int idAnimal, String typeOperation) {
        super("Impossible de " + typeOperation + " l'animal #" + idAnimal +
                " : un séjour est actuellement en cours.");
    }

    public SejourActifException(String nomAnimal, String typeSejour) {
        super("L'animal '" + nomAnimal + "' a un séjour actif (" + typeSejour +
                "). Terminez-le avant de continuer.");
    }
}
