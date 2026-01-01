package projet.tables;

import java.util.HashMap;

/**
 * Représente un membre du personnel ou un bénévole de la SPA.
 * <p>
 * Cette classe stocke les informations d'identité (Nom, Prénom, Téléphone)
 * ainsi que les identifiants de connexion (User, Password) nécessaires
 * pour accéder à l'application via le module d'authentification.
 * </p>
 * * @see projet.tables.ITable
 */
public class Personnel implements ITable {

    private int id_pers;
    private String nom;
    private String prenom;
    private String type_pers; // Ex: Admin, Vétérinaire, Bénévole
    private String tel;
    private String user;
    private String password;

    // Attributs techniques pour l'interface ITable
    private String values;
    private HashMap<String, fieldType> map;

    /**
     * Constructeur vide.
     * Initialise la structure de la table pour les opérations génériques.
     */
    public Personnel() {
        getStruct();
    }

    /**
     * Constructeur complet.
     * * @param id Identifiant unique (souvent auto-généré par la BDD).
     * @param nom Nom de famille.
     * @param prenom Prénom.
     * @param type Rôle dans la SPA (Admin, Bénévole...).
     * @param tel Numéro de téléphone.
     * @param user Identifiant de connexion.
     * @param pwd Mot de passe.
     */
    public Personnel(int id, String nom, String prenom, String type, String tel, String user, String pwd) {
        this.id_pers = id;
        this.nom = nom;
        this.prenom = prenom;
        this.type_pers = type;
        this.tel = tel;
        this.user = user;
        this.password = pwd;
        getStruct();
    }

    /**
     * Définit la structure de la table 'Personnel' (Colonnes et Types).
     */
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

    /**
     * Génère la chaîne de valeurs pour l'insertion SQL.
     * @return Les attributs formatés pour un INSERT INTO.
     */
    @Override
    public String getValues() {
        // Attention aux guillemets simples pour les chaînes de caractères (VARCHAR)
        this.values = "'" + this.nom + "', '" + this.prenom + "', '" + this.type_pers + "', '" + 
                      this.tel + "', '" + this.user + "', '" + this.password + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return this.map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        // Vérification simplifiée pour l'instant
        return true;
    }

    // --- Getters et Setters ---

    public int getId_pers() { return id_pers; }
    public void setId_pers(int id) { this.id_pers = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getType_pers() { return type_pers; }
    public void setType_pers(String type) { this.type_pers = type; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Personnel [ID=" + id_pers + ", Nom=" + nom + ", Prénom=" + prenom + ", Rôle=" + type_pers + "]";
    }
}