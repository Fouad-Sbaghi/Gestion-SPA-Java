package projet.tables;

import java.util.HashMap;

/**
 * Définit un type d'activité possible au sein du refuge (Promenade, Nettoyage, Accueil public).
 */
public class Activite implements ITable {

    private int id_activite;
    private String type_act;
    private String values;
    private HashMap<String, fieldType> map;

    public Activite() { getStruct(); }

    public Activite(int id, String type) {
        this.id_activite = id;
        this.type_act = type;
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
        this.values = "'" + this.type_act + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
    
    public int getId_activite() { return id_activite; }
    public String getType_act() { return type_act; }
    public void setType_act(String t) { this.type_act = t; }
}