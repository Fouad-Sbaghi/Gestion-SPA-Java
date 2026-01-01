package projet.tables;

import java.sql.Date;
import java.util.HashMap;

/**
 * Représente l'historique d'accueil d'un animal au sein d'une famille d'accueil.
 */
public class SejourFamille implements ITable {

    private int id_animal;
    private int id_famille;
    private Date date_d;
    private Date date_f_famille;
    private String values;
    private HashMap<String, fieldType> map;

    public SejourFamille() { getStruct(); }

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
        String fin = (date_f_famille != null) ? "'" + date_f_famille + "'" : "NULL";
        this.values = this.id_animal + ", " + this.id_famille + ", '" + this.date_d + "', " + fin;
        return this.values;
    }
    
    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
}