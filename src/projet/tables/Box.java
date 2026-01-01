package projet.tables;

import java.util.HashMap;
import java.util.Map;

/**
 * Représente un espace d'hébergement (Box) au sein de la SPA.
 * <p>
 * Chaque box possède un type (Chien/Chat) et une capacité maximale 
 * qui ne doit pas être dépassée (Règle métier).
 * </p>
 */
public class Box implements ITable {

    private int id_box;
    private String type_box;
    private int capacite_max;
    private String values;
    private HashMap<String, fieldType> map;

    public Box() { getStruct(); }

    public Box(int id_box, String type_box, int capacite_max) {
        this.id_box = id_box;
        this.type_box = type_box;
        this.capacite_max = capacite_max;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_box", fieldType.INT4);
        map.put("type_box", fieldType.VARCHAR);
        map.put("capacite_max", fieldType.INT4);
    }

    @Override
    public String getValues() {
        this.values = "'" + this.type_box + "', " + this.capacite_max;
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        if (this.map.size() != tableStruct.size()) return false;
        for (Map.Entry<String, fieldType> entry : this.map.entrySet()) {
            if (!tableStruct.containsKey(entry.getKey()) || tableStruct.get(entry.getKey()) != entry.getValue()) return false;
        }
        return true;
    }

    // Getters & Setters
    public int getId_box() { return id_box; }
    public void setId_box(int id_box) { this.id_box = id_box; }
    public String getType_box() { return type_box; }
    public void setType_box(String type_box) { this.type_box = type_box; }
    public int getCapacite_max() { return capacite_max; }
    public void setCapacite_max(int capacite_max) { this.capacite_max = capacite_max; }
}