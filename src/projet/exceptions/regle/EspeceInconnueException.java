package projet.exceptions.regle;

/**
 * Levée lorsqu'une espèce animale n'est pas reconnue par le système.
 * <p>
 * Le refuge gère principalement les chats et chiens.
 * Toute autre espèce peut nécessiter une validation.
 * </p>
 */
public class EspeceInconnueException extends RegleException {

    public EspeceInconnueException(String espece) {
        super("Espèce non reconnue : '" + espece + "'. Espèces acceptées : Chat, Chien.");
    }

    public EspeceInconnueException(String espece, String[] especesAcceptees) {
        super("Espèce non reconnue : '" + espece + "'. Espèces acceptées : " +
                String.join(", ", especesAcceptees) + ".");
    }
}
