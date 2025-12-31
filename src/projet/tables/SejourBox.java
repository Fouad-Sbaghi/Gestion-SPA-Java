package projet.tables;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class SejourBox implements ITable {

    private int id_animal;
    private int id_box;
    private Date date_d;
    private Date date_f_box;

    private String values;
    private HashMap<String, fieldType> map;

    public SejourBox() { getStruct(); }

    public SejourBox(int id_animal, int id_box, Date date_d, Date date_f_box) {
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
        // Gestion de la date de fin qui peut être NULL (si l'animal est encore dans le box)
        String dateFin = (this.date_f_box != null) ? "'" + this.date_f_box + "'" : "NULL";

        this.values = this.id_animal + ", " +
                      this.id_box + ", " +
                      "'" + this.date_d + "', " +
                      dateFin;
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
    public int getId_animal() { return id_animal; }
    public void setId_animal(int id_animal) { this.id_animal = id_animal; }
    public int getId_box() { return id_box; }
    public void setId_box(int id_box) { this.id_box = id_box; }
    public Date getDate_d() { return date_d; }
    public void setDate_d(Date date_d) { this.date_d = date_d; }
    public Date getDate_f_box() { return date_f_box; }
    public void setDate_f_box(Date date_f_box) { this.date_f_box = date_f_box; }
}