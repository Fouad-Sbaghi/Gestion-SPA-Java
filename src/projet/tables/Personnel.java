package projet.tables;

import java.util.HashMap;
import java.util.Map;

public class Personnel implements ITable {

    private int id_pers;
    private String nom;
    private String prenom;
    private String type_pers;
    private String tel;
    private String user;
    private String password;

    // Attributs imposés par ITable (Comme dans le tp jdbc avec IData)
    private String values;
    private HashMap<String, fieldType> map;

    public Personnel() {
        getStruct();
    }

    public Personnel(int id_pers, String nom, String prenom, String type_pers, String tel, String user, String password) {
        this.id_pers = id_pers;
        this.nom = nom;
        this.prenom = prenom;
        this.type_pers = type_pers;
        this.tel = tel;
        this.user = user;
        this.password = password;
        getStruct();
    }

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_pers", fieldType.INT4);
        map.put("nom", fieldType.VARCHAR);
        map.put("prenom", fieldType.VARCHAR);
        map.put("type_pers", fieldType.VARCHAR);
        map.put("tel", fieldType.VARCHAR);
        map.put("user", fieldType.VARCHAR);
        map.put("password", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        this.values = "'" + this.nom + "', " +
                      "'" + this.prenom + "', " +
                      "'" + this.type_pers + "', " +
                      "'" + this.tel + "', " +
                      "'" + this.user + "', " +
                      "'" + this.password + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return this.map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        if (this.map.size() != tableStruct.size()) {
            return false;
        }
        for (Map.Entry<String, fieldType> entry : this.map.entrySet()) {
            if (!tableStruct.containsKey(entry.getKey()) || tableStruct.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    // Getters & Setters
    public int getId_pers() { return id_pers; }
    public void setId_pers(int id_pers) { this.id_pers = id_pers; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getType_pers() { return type_pers; }
    public void setType_pers(String type_pers) { this.type_pers = type_pers; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Personnel [id=" + id_pers + ", nom=" + nom + ", prenom=" + prenom + "]";
    }
}