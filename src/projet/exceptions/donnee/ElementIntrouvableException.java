package projet.exceptions.donnee;

public class ElementIntrouvableException extends DonneeException {
    public ElementIntrouvableException(String typeElement, int id) {
        super("ElementIntrouvableException : Impossible de trouver " + typeElement + " avec l'ID " + id);
    }

    public ElementIntrouvableException(String typeElement, String nom) {
        super("ElementIntrouvableException : Impossible de trouver " + typeElement + " avec le nom " + nom);
    }
}