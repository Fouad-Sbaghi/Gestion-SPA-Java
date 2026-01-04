package projet.tables;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Représente un événement notable lié à un animal ou au refuge (Maladie, Accident, Fuite).

 * Cette classe permet de garder une trace datée et détaillée des problèmes survenus
 * afin de constituer un historique médical ou administratif.

 */
public class Incident implements ITable {

    private int id_incident;
    private String type_incident;
    private String intitule;
    private String commentaire;
    private Timestamp date_incident;

    // Attributs techniques pour l'interface ITable
    private String values;
    private HashMap<String, fieldType> map;

    /**
     * Constructeur vide.
     * Initialise la structure de la table pour les opérations génériques.
     */
    public Incident() { 
        getStruct(); 
    }

    /**
     * Constructeur complet.
     * @param id Identifiant unique de l'incident.
     * @param type Catégorie (ex: "Médical", "Comportement", "Fuite").
     * @param intitule Titre court de l'incident.
     * @param comm Description détaillée.
     * @param date Date et heure de l'événement.
     */
    public Incident(int id, String type, String intitule, String comm, Timestamp date) {
        this.id_incident = id;
        this.type_incident = type;
        this.intitule = intitule;
        this.commentaire = comm;
        this.date_incident = date;
        getStruct();
    }

    /**
     * Définit la structure de la table 'Incident' (Colonnes et Types).
     */
    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_incident", fieldType.INT4);
        map.put("type_incident", fieldType.VARCHAR);
        map.put("intitule", fieldType.VARCHAR);
        map.put("commentaire", fieldType.VARCHAR);
        map.put("date_incident", fieldType.VARCHAR);
    }

    /**
     * Génère la chaîne de valeurs pour l'insertion SQL.
     * @return Les attributs formatés pour un INSERT INTO.
     */
    @Override
    public String getValues() {
        this.values = "'" + this.type_incident + "', '" + this.intitule + "', '" + 
                      this.commentaire + "', '" + this.date_incident + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() { return this.map; }

    @Override
    public boolean check(HashMap<String, fieldType> ts) { return true; }
    
    // --- Getters & Setters ---

    /**
     * @return L'identifiant unique de l'incident.
     */
    public int getId_incident() { return id_incident; }
    
    /**
     * @param id_incident Le nouvel identifiant.
     */
    public void setId_incident(int id_incident) { this.id_incident = id_incident; }

    /**
     * @return Le type d'incident (ex: Médical, Accident).
     */
    public String getType_incident() { return type_incident; }
    
    /**
     * @param t Le type d'incident.
     */
    public void setType_incident(String t) { this.type_incident = t; }

    /**
     * @return L'intitulé court de l'incident.
     */
    public String getIntitule() { return intitule; }
    
    /**
     * @param i L'intitulé.
     */
    public void setIntitule(String i) { this.intitule = i; }

    /**
     * @return Le commentaire détaillé.
     */
    public String getCommentaire() { return commentaire; }
    
    /**
     * @param c Le commentaire.
     */
    public void setCommentaire(String c) { this.commentaire = c; }

    /**
     * @return La date et l'heure de l'incident.
     */
    public Timestamp getDate_incident() { return date_incident; }
    
    /**
     * @param d La date et l'heure.
     */
    public void setDate_incident(Timestamp d) { this.date_incident = d; }
}