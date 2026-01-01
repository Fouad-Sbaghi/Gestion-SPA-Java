package projet.tables;

import java.sql.Date;
import java.util.HashMap;

/**
 * Gère l'emploi du temps spécifique d'un animal.
 * <p>
 * Permet d'associer un animal à un créneau et un bénévole (Ex: Rex est promené par Jean Mardi matin).
 * </p>
 */
public class PlanningAnimal implements ITable {

    private int id_animal;
    private int id_creneau;
    private int id_pers;
    private Date date_d;
    private String values;
    private HashMap<String, fieldType> map;

    public PlanningAnimal() { getStruct(); }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_animal", fieldType.INT4);
        map.put("id_creneau", fieldType.INT4);
        map.put("id_pers", fieldType.INT4);
        map.put("date_d", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        this.values = this.id_animal + ", " + this.id_creneau + ", " + this.id_pers + ", '" + this.date_d + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
}