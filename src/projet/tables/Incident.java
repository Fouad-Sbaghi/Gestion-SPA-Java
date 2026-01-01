package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Représente un événement notable lié à un animal ou au refuge (Maladie, Accident, Fuite).
 * <p>
 * Permet de garder une trace datée des problèmes survenus.
 * </p>
 */
public class Incident implements ITable {

    private int id_incident;
    private String type_incident;
    private String intitule;
    private String commentaire;
    private Timestamp date_incident;

    private String values;
    private HashMap<String, fieldType> map;

    public Incident() { getStruct(); }

    public Incident(int id, String type, String intitule, String comm, Timestamp date) {
        this.id_incident = id;
        this.type_incident = type;
        this.intitule = intitule;
        this.commentaire = comm;
        this.date_incident = date;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_incident", fieldType.INT4);
        map.put("type_incident", fieldType.VARCHAR);
        map.put("intitule", fieldType.VARCHAR);
        map.put("commentaire", fieldType.VARCHAR);
        map.put("date_incident", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        this.values = "'" + this.type_incident + "', '" + this.intitule + "', '" + 
                      this.commentaire + "', '" + this.date_incident + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
    
    // Getters & Setters
    public void setType_incident(String t) { this.type_incident = t; }
    public void setIntitule(String i) { this.intitule = i; }
    public void setCommentaire(String c) { this.commentaire = c; }
    public void setDate_incident(Timestamp d) { this.date_incident = d; }
}