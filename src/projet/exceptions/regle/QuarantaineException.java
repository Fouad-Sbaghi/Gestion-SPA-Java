package projet.exceptions.regle;

/**
 * Levée lorsqu'on tente une opération interdite sur un animal en quarantaine.
 * <p>
 * Un animal en quarantaine ne peut pas être placé en famille ou adopté
 * tant que la quarantaine n'est pas terminée.
 * </p>
 */
public class QuarantaineException extends RegleException {

    public QuarantaineException(int idAnimal) {
        super("L'animal #" + idAnimal + " est en quarantaine. " +
                "Opération non autorisée tant que la quarantaine n'est pas levée.");
    }

    public QuarantaineException(String nomAnimal, String operation) {
        super("Impossible de " + operation + " l'animal '" + nomAnimal +
                "' : il est actuellement en quarantaine.");
    }
}
