package projet.tables;

import java.util.HashMap;

public class AffectationCreneauActivite implements ITable {

    private int id_creneau;
    private int id_personne;
    private int id_activite;

    private String values;
    private HashMap<String, fieldType> map;

    public AffectationCreneauActivite() { getStruct(); }

    public AffectationCreneauActivite(int c, int p, int a) {
        this.id_creneau = c;
        this.id_personne = p;
        this.id_activite = a;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_creneau", fieldType.INT4);
        map.put("id_personne", fieldType.INT4);
        map.put("id_activite", fieldType.INT4);
    }

    @Override
    public String getValues() {
        this.values = this.id_creneau + ", " + this.id_personne + ", " + this.id_activite;
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
}