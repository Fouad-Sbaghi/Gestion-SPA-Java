package projet.tables;

import java.util.HashMap;

public class Veterinaire implements ITable {

    private int id_personne;
    private int id_soin;

    private String values;
    private HashMap<String, fieldType> map;

    public Veterinaire() { getStruct(); }

    public Veterinaire(int id_personne, int id_soin) {
        this.id_personne = id_personne;
        this.id_soin = id_soin;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_personne", fieldType.INT4);
        map.put("id_soin", fieldType.INT4);
    }

    @Override
    public String getValues() {
        this.values = this.id_personne + ", " + this.id_soin;
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
}