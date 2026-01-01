package projet.exceptions;

public class ElementIntrouvableException extends DonneeException {
    public ElementIntrouvableException(String typeElement, int id) {
        super("ElementIntrouvableException : Impossible de trouver " + typeElement + " avec l'ID " + id);
    }
}