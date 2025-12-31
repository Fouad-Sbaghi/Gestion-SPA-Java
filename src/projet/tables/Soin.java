package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Soin implements ITable {

    private int id_soin;
    private int id_animal;
    private String type_soin;
    private String libelle;
    private String commentaire;
    private Timestamp date_soin;

    private String values;
    private HashMap<String, fieldType> map;

    public Soin() { getStruct(); }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_soin", fieldType.INT4);
        map.put("id_animal", fieldType.INT4);
        map.put("type_soin", fieldType.VARCHAR);
        map.put("libelle", fieldType.VARCHAR);
        map.put("commentaire", fieldType.VARCHAR);
        map.put("date_soin", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        this.values = this.id_animal + ", " +
                      "'" + this.type_soin + "', " +
                      "'" + this.libelle + "', " +
                      "'" + this.commentaire + "', " +
                      "'" + this.date_soin + "'";
        return this.values;
    }
    
    // Méthodes check et getMap identiques aux autres...
    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; } // Simplifié ici

	public int getId_soin() {
		return id_soin;
	}

	public void setId_soin(int id_soin) {
		this.id_soin = id_soin;
	}

	public int getId_animal() {
		return id_animal;
	}

	public void setId_animal(int id_animal) {
		this.id_animal = id_animal;
	}

	public String getType_soin() {
		return type_soin;
	}

	public void setType_soin(String type_soin) {
		this.type_soin = type_soin;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Timestamp getDate_soin() {
		return date_soin;
	}

	public void setDate_soin(Timestamp date_soin) {
		this.date_soin = date_soin;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public void setMap(HashMap<String, fieldType> map) {
		this.map = map;
	}
    
    
}