package projet.exceptions.regle;

/**
 * Levée lorsqu'une espèce animale n'est pas reconnue par le système.
 * <p>
 * Le refuge gère principalement les chats et chiens.
 * </p>
 */
public class EspeceInconnueException extends RegleException {

    /**
     * Constructeur simple.
     * 
     * @param espece L'espèce non reconnue.
     */
    public EspeceInconnueException(String espece) {
        super("Espèce non reconnue : '" + espece + "'. Espèces acceptées : Chat, Chien.");
    }

    /**
     * Constructeur avec liste des espèces acceptées.
     * 
     * @param espece           L'espèce non reconnue.
     * @param especesAcceptees Tableau des espèces valides.
     */
    public EspeceInconnueException(String espece, String[] especesAcceptees) {
        super("Espèce non reconnue : '" + espece + "'. Espèces acceptées : " +
                String.join(", ", especesAcceptees) + ".");
    }
}
