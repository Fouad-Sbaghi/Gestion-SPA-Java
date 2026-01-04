package projet.tables;

import java.util.HashMap;

/**
 * Représente une famille externe (Accueil ou Adoptante).

 * Permet de stocker les coordonnées des personnes accueillant les animaux
 * temporairement ou définitivement.

 */
public class Famille implements ITable {

    private int id_famille;
    private String type_famille;
    private String nom;
    private String adresse;
    private String contact;
    private String values;
    private HashMap<String, fieldType> map;

    public Famille() { getStruct(); }

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
        this.values = "'" + this.type_famille + "', '" + this.nom + "', '" + this.adresse + "', '" + this.contact + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }
    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) { return true; }

    // Getters & Setters
    public int getId_famille() { return id_famille; }
    public void setId_famille(int id) { this.id_famille = id; }
    public String getType_famille() { return type_famille; }
    public void setType_famille(String t) { this.type_famille = t; }
    public String getNom() { return nom; }
    public void setNom(String n) { this.nom = n; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String a) { this.adresse = a; }
    public String getContact() { return contact; }
    public void setContact(String c) { this.contact = c; }
}