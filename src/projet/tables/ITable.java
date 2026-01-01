package projet.tables;

import java.util.HashMap;

/**
 * Interface générique assurant la liaison entre les objets Java et les tables SQL.
 * <p>
 * Toutes les classes implémentant cette interface peuvent être manipulées dynamiquement
 * par le gestionnaire de base de données (insertion automatique, vérification de structure).
 * </p>
 */
public interface ITable {
    
    /**
     * Définit la structure de la table associée (noms des colonnes et types).
     * Cette méthode remplit la HashMap interne de l'objet.
     */
    public void getStruct();
    
    /**
     * Génère la chaîne de valeurs formatée pour une requête SQL INSERT.
     * <p>Exemple de retour : "'Rex', 'Chien', 12345"</p>
     * @return Une chaîne de caractères contenant les attributs de l'objet séparés par des virgules.
     */
    public String getValues();
    
    /**
     * Récupère la structure interne de l'objet (Mapping Colonne/Type).
     * @return La HashMap contenant les métadonnées de la table.
     */
    public HashMap<String, fieldType> getMap();
    
    /**
     * Vérifie la cohérence entre l'objet Java et la table SQL physique.
     * @param tableStruct La structure réelle récupérée depuis la base de données.
     * @return true si l'objet correspond parfaitement à la table, false sinon.
     */
    public boolean check(HashMap<String, fieldType> tableStruct);
}