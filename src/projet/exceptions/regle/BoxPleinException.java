package projet.exceptions.regle;

/**
 * Levée lorsqu'on tente d'ajouter un animal dans un box dont la capacité
 * maximale est atteinte.
 */
public class BoxPleinException extends RegleException {

    /**
     * Constructeur.
     * 
     * @param idBox    L'identifiant du box plein.
     * @param capacite La capacité maximale du box.
     */
    public BoxPleinException(int idBox, int capacite) {
        super("BoxPleinException : Le box n°" + idBox + " est complet (Max: " + capacite + "). Ajout impossible.");
    }
}