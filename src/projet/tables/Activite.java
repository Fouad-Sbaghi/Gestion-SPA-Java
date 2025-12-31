package projet.tables;

import java.util.HashMap;
import java.util.Map;

public class Activite implements ITable {

    private int id_activite;
    private String type_act;

    // Attributs pour l'interface ITable
    private String values;
    private HashMap<String, fieldType> map;

    public Activite() {
        getStruct();
    }

    public Activite(int id_activite, String type_act) {
        this.id_activite = id_activite;
        this.type_act = type_act;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_activite", fieldType.INT4);
        map.put("type_act", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        // Pour l'INSERT : on ignore l'ID (Auto-increment)
        // Format : 'TypeActivite'
        this.values = "'" + this.type_act + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return this.map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        if (this.map.size() != tableStruct.size()) return false;
        
        for (Map.Entry<String, fieldType> entry : this.map.entrySet()) {
            if (!tableStruct.containsKey(entry.getKey()) || tableStruct.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    // --- Getters & Setters ---

    public int getId_activite() { return id_activite; }
    public void setId_activite(int id_activite) { this.id_activite = id_activite; }

    public String getType_act() { return type_act; }
    public void setType_act(String type_act) { this.type_act = type_act; }

    @Override
    public String toString() {
        return "Activite [ID=" + id_activite + ", Type=" + type_act + "]";
    }
}