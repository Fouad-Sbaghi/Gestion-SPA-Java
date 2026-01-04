package projet.exceptions.donnee;

/**
 * Levée lorsqu'un élément recherché n'existe pas en base de données.

 * Utilisée pour les recherches par ID ou par nom qui ne retournent aucun
 * résultat.

 */
public class ElementIntrouvableException extends DonneeException {

    /**
     * Constructeur avec type et ID.
     * 
     * @param typeElement Le type d'élément (Animal, Box, Famille...).
     * @param id          L'identifiant recherché.
     */
    public ElementIntrouvableException(String typeElement, int id) {
        super("ElementIntrouvableException : Impossible de trouver " + typeElement + " avec l'ID " + id);
    }

    /**
     * Constructeur avec type et nom.
     * 
     * @param typeElement Le type d'élément.
     * @param nom         Le nom recherché.
     */
    public ElementIntrouvableException(String typeElement, String nom) {
        super("ElementIntrouvableException : Impossible de trouver " + typeElement + " avec le nom " + nom);
    }

    /**
     * Constructeur avec message personnalisé.
     * 
     * @param typeElement Description de l'élément introuvable.
     */
    public ElementIntrouvableException(String typeElement) {
        super("ElementIntrouvableException : Impossible de trouver " + typeElement);
    }
}