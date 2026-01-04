package projet.tables;

import java.sql.Time;
import java.util.HashMap;

/**
 * Représente une plage horaire de travail.

 * Définit l'heure de début, de fin et le nombre de bénévoles requis pour ce moment.

 */
public class Creneau implements ITable {

    private int id_creneau;
    private int nb_benevole;
    private Time heure_d;
    private Time heure_f;
    private String values;
    private HashMap<String, fieldType> map;

    public Creneau() { getStruct(); }

    public Creneau(int id, int nb, Time debut, Time fin) {
        this.id_creneau = id;
        this.nb_benevole = nb;
        this.heure_d = debut;
        this.heure_f = fin;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_creneau", fieldType.INT4);
        map.put("nb_benevole", fieldType.INT4);
        map.put("heure_d", fieldType.VARCHAR);
        map.put("heure_f", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        this.values = this.nb_benevole + ", '" + this.heure_d + "', '" + this.heure_f + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }

	public int getId_creneau() {
		return id_creneau;
	}

	public void setId_creneau(int id_creneau) {
		this.id_creneau = id_creneau;
	}

	public int getNb_benevole() {
		return nb_benevole;
	}

	public void setNb_benevole(int nb_benevole) {
		this.nb_benevole = nb_benevole;
	}

	public Time getHeure_d() {
		return heure_d;
	}

	public void setHeure_d(Time heure_d) {
		this.heure_d = heure_d;
	}

	public Time getHeure_f() {
		return heure_f;
	}

	public void setHeure_f(Time heure_f) {
		this.heure_f = heure_f;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public void setMap(HashMap<String, fieldType> map) {
		this.map = map;
	}
    
 
}