package projet.exceptions.donnee;

/**
 * Levée lors d'une tentative d'insertion d'une donnée censée être unique.
 * <p>
 * Exemple : Numéro de puce déjà existant dans la base.
 * </p>
 */
public class DuplicatedIdException extends DonneeException {

    /**
     * Constructeur.
     * 
     * @param valeur La valeur dupliquée (ID ou puce).
     */
    public DuplicatedIdException(String valeur) {
        super("DuplicatedIdException : L'identifiant ou la puce '" + valeur + "' existe déjà dans la base.");
    }
}