package projet.exceptions;

/**
 * Levée lorsqu'il y a incohérence entre le type de l'animal et le type d'hébergement.
 * <p>
 * Exemple : Tenter de mettre un animal de type "Chien" dans un Box de type "Chat".
 * Cette règle métier assure la sécurité et le bien-être des animaux.
 * </p>
 */
public class IncompatibiliteTypeException extends MetierException {

    /**
     * Constructeur de l'exception.
     * * @param typeAnimal Le type de l'animal (ex: "Chien").
     * @param typeLieu Le type requis par le lieu (ex: "Chat").
     */
    public IncompatibiliteTypeException(String typeAnimal, String typeLieu) {
        super("Incompatibilité d'espèce : Un animal de type [" + typeAnimal + 
              "] ne peut pas être placé dans un environnement réservé aux [" + typeLieu + "].");
    }
}