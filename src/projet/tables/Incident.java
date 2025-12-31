package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Incident implements ITable {

    private int id_incident;
    private String type_incident;
    private String intitule;
    private String commentaire;
    private Timestamp date_incident; // DATETIME en SQL = Timestamp en Java

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
        map.put("date_incident", fieldType.VARCHAR); // Géré en string pour l'insert
    }

    @Override
    public String getValues() {
        this.values = "'" + this.type_incident + "', " +
                      "'" + this.intitule + "', " +
                      "'" + this.commentaire + "', " +
                      "'" + this.date_incident + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }

    @Override
    public boolean check(HashMap<String, fieldType> ts) {
        if (this.map.size() != ts.size()) return false;
        for (Map.Entry<String, fieldType> e : this.map.entrySet()) {
            if (!ts.containsKey(e.getKey()) || ts.get(e.getKey()) != e.getValue()) return false;
        }
        return true;
    }
    
}