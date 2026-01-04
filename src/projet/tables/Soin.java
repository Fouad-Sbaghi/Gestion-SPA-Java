package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Représente un acte médical ou un soin effectué sur un animal.

 * Chaque soin est lié à un animal spécifique par son identifiant unique.
 * Cette classe permet de tracer l'historique médical (Vaccins, Opérations, Contrôles...).

 */
public class Soin implements ITable {

    private int id_soin;
    private int id_animal;
    private String type_soin;   // Ex: Vaccin, Opération, Vermifuge
    private String libelle;     // Ex: "Rappel Rage"
    private String commentaire; // Ex: "S'est bien passé"
    private Timestamp date_soin;

    // Attributs techniques pour l'interface ITable
    private String values;
    private HashMap<String, fieldType> map;

    /**
     * Constructeur vide.
     * Initialise la structure de la table pour les opérations génériques.
     */
    public Soin() { 
        getStruct(); 
    }

    /**
     * Constructeur complet.
     * @param id_soin Identifiant unique du soin.
     * @param id_animal Identifiant de l'animal concerné.
     * @param type_soin Catégorie du soin (Vaccin, Opération...).
     * @param libelle Nom court du soin.
     * @param commentaire Détails ou observations.
     * @param date_soin Date et heure de l'intervention.
     */
    public Soin(int id_soin, int id_animal, String type_soin, String libelle, String commentaire, Timestamp date_soin) {
        this.id_soin = id_soin;
        this.id_animal = id_animal;
        this.type_soin = type_soin;
        this.libelle = libelle;
        this.commentaire = commentaire;
        this.date_soin = date_soin;
        getStruct();
    }

    /**
     * Définit la structure de la table 'Soin' (Colonnes et Types).
     */
    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_soin", fieldType.INT4);
        map.put("id_animal", fieldType.INT4);
        map.put("type_soin", fieldType.VARCHAR);
        map.put("libelle", fieldType.VARCHAR);
        map.put("commentaire", fieldType.VARCHAR);
        map.put("date_soin", fieldType.VARCHAR);
    }

    /**
     * Génère la chaîne de valeurs pour l'insertion SQL.
     * @return Les attributs formatés pour un INSERT INTO.
     */
    @Override
    public String getValues() {
        this.values = this.id_animal + ", '" + this.type_soin + "', '" + this.libelle + "', '" + 
                      this.commentaire + "', '" + this.date_soin + "'";
        return this.values;
    }
    
    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }

    // --- Getters & Setters ---

    /**
     * @return L'identifiant unique du soin.
     */
    public int getId_soin() { return id_soin; }
    
    /**
     * @param id_soin Le nouvel identifiant.
     */
    public void setId_soin(int id_soin) { this.id_soin = id_soin; }

    /**
     * @return L'identifiant de l'animal soigné.
     */
    public int getId_animal() { return id_animal; }
    
    /**
     * @param id_animal L'identifiant de l'animal.
     */
    public void setId_animal(int id_animal) { this.id_animal = id_animal; }

    /**
     * @return Le type de soin (Catégorie).
     */
    public String getType_soin() { return type_soin; }
    
    /**
     * @param type_soin Le type de soin.
     */
    public void setType_soin(String type_soin) { this.type_soin = type_soin; }

    /**
     * @return Le libellé court.
     */
    public String getLibelle() { return libelle; }
    
    /**
     * @param libelle Le libellé.
     */
    public void setLibelle(String libelle) { this.libelle = libelle; }

    /**
     * @return Les commentaires ou observations.
     */
    public String getCommentaire() { return commentaire; }
    
    /**
     * @param commentaire Le commentaire.
     */
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    /**
     * @return La date et l'heure du soin.
     */
    public Timestamp getDate_soin() { return date_soin; }
    
    /**
     * @param date_soin La date et l'heure.
     */
    public void setDate_soin(Timestamp date_soin) { this.date_soin = date_soin; }
}