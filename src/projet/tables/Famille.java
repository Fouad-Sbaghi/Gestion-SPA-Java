package projet.tables;

import java.util.HashMap;
import java.util.Map;

public class Famille implements ITable {

    private int id_famille;
    private String type_famille; // Accueil ou Adoption
    private String nom;
    private String adresse;
    private String contact;

    private String values;
    private HashMap<String, fieldType> map;

    public Famille() { getStruct(); }

    public Famille(int id_famille, String type_famille, String nom, String adresse, String contact) {
        this.id_famille = id_famille;
        this.type_famille = type_famille;
        this.nom = nom;
        this.adresse = adresse;
        this.contact = contact;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_famille", fieldType.INT4);
        map.put("type_famille", fieldType.VARCHAR);
        map.put("nom", fieldType.VARCHAR);
        map.put("adresse", fieldType.VARCHAR);
        map.put("contact", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        this.values = "'" + this.type_famille + "', " +
                      "'" + this.nom + "', " +
                      "'" + this.adresse + "', " +
                      "'" + this.contact + "'";
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
    public int getId_famille() { return id_famille; }
    public void setId_famille(int id_famille) { this.id_famille = id_famille; }
    public String getType_famille() { return type_famille; }
    public void setType_famille(String type_famille) { this.type_famille = type_famille; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}