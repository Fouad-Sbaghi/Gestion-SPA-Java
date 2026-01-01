package projet.tables;

import java.util.HashMap;

/**
 * Table de liaison associant un membre du personnel (Vétérinaire) à un acte de soin réalisé.
 */
public class Veterinaire implements ITable {

    private int id_personne;
    private int id_soin;
    private String values;
    private HashMap<String, fieldType> map;

    public Veterinaire() { getStruct(); }

    public Veterinaire(int p, int s) {
        this.id_personne = p;
        this.id_soin = s;
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