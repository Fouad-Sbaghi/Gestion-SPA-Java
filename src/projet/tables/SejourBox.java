package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Représente l'association historique entre un Animal et un Box (Séjour).
 * <p>
 * Cette classe permet de tracer l'historique des emplacements d'un animal
 * avec une date de début et une date de fin éventuelle.
 * </p>
 */
public class SejourBox implements ITable {

    private int id_animal;
    private int id_box;
    private Timestamp date_d;
    private Timestamp date_f_box;
    private String values;
    private HashMap<String, fieldType> map;

    public SejourBox() {
        getStruct();
    }

    public SejourBox(int id_animal, int id_box, Timestamp date_d, Timestamp date_f_box) {
        this.id_animal = id_animal;
        this.id_box = id_box;
        this.date_d = date_d;
        this.date_f_box = date_f_box;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_animal", fieldType.INT4);
        map.put("id_box", fieldType.INT4);
        map.put("date_d", fieldType.VARCHAR);
        map.put("date_f_box", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        String dateFin = (this.date_f_box != null) ? "'" + this.date_f_box + "'" : "NULL";
        this.values = this.id_animal + ", " + this.id_box + ", '" + this.date_d + "', " + dateFin;
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return this.map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> ts) {
        return true;
    }

    // Getters
    public int getId_animal() {
        return id_animal;
    }

    public void setId_animal(int id) {
        this.id_animal = id;
    }

    public int getId_box() {
        return id_box;
    }

    public void setId_box(int id) {
        this.id_box = id;
    }

    public Timestamp getDate_d() {
        return date_d;
    }

    public void setDate_d(Timestamp d) {
        this.date_d = d;
    }

    public Timestamp getDate_f_box() {
        return date_f_box;
    }

    public void setDate_f_box(Timestamp d) {
        this.date_f_box = d;
    }
}