package projet.exceptions.regle;

/**
 * Levée lorsqu'il y a incohérence entre le type de l'animal et le type
 * d'hébergement.

 * Exemple : Tenter de mettre un animal de type "Chien" dans un Box de type
 * "Chat".

 */
public class IncompatibiliteTypeException extends RegleException {

    /**
     * Constructeur.
     * 
     * @param typeAnimal Le type de l'animal (ex: "Chien").
     * @param typeLieu   Le type requis par le lieu (ex: "Chat").
     */
    public IncompatibiliteTypeException(String typeAnimal, String typeLieu) {
        super("IncompatibiliteTypeException : Un animal de type [" + typeAnimal +
                "] ne peut pas être placé dans un environnement réservé aux [" + typeLieu + "].");
    }
}