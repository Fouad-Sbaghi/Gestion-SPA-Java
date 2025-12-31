package projet.tables;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class Creneau implements ITable {

    private int id_creneau;
    private int nb_benevole;
    private Time heure_d;
    private Time heure_f;

    // Implémentation ITable
    private String values;
    private HashMap<String, fieldType> map;

    public Creneau() {
        getStruct();
    }

    public Creneau(int id_creneau, int nb_benevole, Time heure_d, Time heure_f) {
        this.id_creneau = id_creneau;
        this.nb_benevole = nb_benevole;
        this.heure_d = heure_d;
        this.heure_f = heure_f;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_creneau", fieldType.INT4);
        map.put("nb_benevole", fieldType.INT4);
        map.put("heure_d", fieldType.VARCHAR); // On passe les heures en string pour l'insertion
        map.put("heure_f", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        // Format SQL : nb, 'HH:MM:SS', 'HH:MM:SS'
        this.values = this.nb_benevole + ", " +
                      "'" + this.heure_d + "', " +
                      "'" + this.heure_f + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        if (this.map.size() != tableStruct.size()) return false;
        for (Map.Entry<String, fieldType> entry : this.map.entrySet()) {
            if (!tableStruct.containsKey(entry.getKey()) || tableStruct.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    // Getters & Setters
    public int getId_creneau() { return id_creneau; }
    public void setId_creneau(int id_creneau) { this.id_creneau = id_creneau; }
    public int getNb_benevole() { return nb_benevole; }
    public void setNb_benevole(int nb_benevole) { this.nb_benevole = nb_benevole; }
    public Time getHeure_d() { return heure_d; }
    public void setHeure_d(Time heure_d) { this.heure_d = heure_d; }
    public Time getHeure_f() { return heure_f; }
    public void setHeure_f(Time heure_f) { this.heure_f = heure_f; }
}