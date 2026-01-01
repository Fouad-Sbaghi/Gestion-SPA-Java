package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Représente un acte médical ou un soin effectué sur un animal.
 * <p>
 * Lié à un animal spécifique par son ID.
 * </p>
 */
public class Soin implements ITable {

    private int id_soin;
    private int id_animal;
    private String type_soin;
    private String libelle;
    private String commentaire;
    private Timestamp date_soin;
    private String values;
    private HashMap<String, fieldType> map;

    public Soin() { getStruct(); }

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
    
    // Getters & Setters simples
    public void setId_animal(int id) { this.id_animal = id; }
    public void setType_soin(String t) { this.type_soin = t; }
    public void setLibelle(String l) { this.libelle = l; }
    public void setCommentaire(String c) { this.commentaire = c; }
    public void setDate_soin(Timestamp d) { this.date_soin = d; }
}