package projet.exceptions.regle;

/**
 * Levée lorsqu'une opération est interdite car l'animal a un séjour en cours.

 * Exemple : Supprimer un animal qui est actuellement dans un box ou une
 * famille.

 */
public class SejourActifException extends RegleException {

    /**
     * Constructeur avec ID et type d'opération.
     * 
     * @param idAnimal      L'identifiant de l'animal.
     * @param typeOperation L'opération interdite.
     */
    public SejourActifException(int idAnimal, String typeOperation) {
        super("Impossible de " + typeOperation + " l'animal #" + idAnimal +
                " : un séjour est actuellement en cours.");
    }

    /**
     * Constructeur avec nom et type de séjour.
     * 
     * @param nomAnimal  Le nom de l'animal.
     * @param typeSejour Le type de séjour actif.
     */
    public SejourActifException(String nomAnimal, String typeSejour) {
        super("L'animal '" + nomAnimal + "' a un séjour actif (" + typeSejour +
                "). Terminez-le avant de continuer.");
    }
}
