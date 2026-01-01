package projet.tables;

import java.util.HashMap;

/**
 * Table de liaison simple permettant d'associer un ou plusieurs incidents à un animal.
 */
public class AnimalIncident implements ITable {

    private int id_animal;
    private int id_incident;
    private String values;
    private HashMap<String, fieldType> map;

    public AnimalIncident() { getStruct(); }

    public AnimalIncident(int animal, int incident) {
        this.id_animal = animal;
        this.id_incident = incident;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_animal", fieldType.INT4);
        map.put("id_incident", fieldType.INT4);
    }

    @Override
    public String getValues() {
        this.values = this.id_animal + ", " + this.id_incident;
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
}