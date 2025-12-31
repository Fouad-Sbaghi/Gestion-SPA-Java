package projet.tables;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class SejourFamille implements ITable {

    private int id_animal;
    private int id_famille;
    private Date date_d;
    private Date date_f_famille;

    private String values;
    private HashMap<String, fieldType> map;

    public SejourFamille() { getStruct(); }

    public SejourFamille(int id_animal, int id_famille, Date date_d, Date date_f_famille) {
        this.id_animal = id_animal;
        this.id_famille = id_famille;
        this.date_d = date_d;
        this.date_f_famille = date_f_famille;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_animal", fieldType.INT4);
        map.put("id_famille", fieldType.INT4);
        map.put("date_d", fieldType.VARCHAR);
        map.put("date_f_famille", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        String dateFin = (this.date_f_famille != null) ? "'" + this.date_f_famille + "'" : "NULL";
        
        this.values = this.id_animal + ", " +
                      this.id_famille + ", " +
                      "'" + this.date_d + "', " +
                      dateFin;
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; } // Simplifié

    // Getters/Setters à générer...
    public int getId_animal() { return id_animal; }
    public void setId_animal(int id) { this.id_animal = id; }
    // ... etc
}