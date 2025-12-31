package projet.tables;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class Animal implements ITable {

    // Attributs correspondant aux colonnes de la BDD
    private int id_animal;
    private String puce;
    private String espece;
    private String nom;
    private Date date_naissance;
    private String statut;
    private boolean tests_humain;
    private boolean tests_bebe;
    private boolean tests_chien;
    private boolean tests_chat;
    private Date date_arrivee;

    // Attributs pour l'interface ITable
    private String values;
    private HashMap<String, fieldType> map;

    // Constructeur vide
    public Animal() {
        getStruct();
    }

    // Constructeur complet
    public Animal(int id_animal, String puce, String espece, String nom, Date date_naissance,
                  String statut, boolean t_humain, boolean t_bebe, boolean t_chien, boolean t_chat, Date date_arrivee) {
        this.id_animal = id_animal;
        this.puce = puce;
        this.espece = espece;
        this.nom = nom;
        this.date_naissance = date_naissance;
        this.statut = statut;
        this.tests_humain = t_humain;
        this.tests_bebe = t_bebe;
        this.tests_chien = t_chien;
        this.tests_chat = t_chat;
        this.date_arrivee = date_arrivee;
        getStruct();
    }

    // --- Implémentation ITable ---

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_animal", fieldType.INT4);
        map.put("puce", fieldType.VARCHAR);
        map.put("espece", fieldType.VARCHAR);
        map.put("nom", fieldType.VARCHAR);
        map.put("date_naissance", fieldType.VARCHAR); // Dates gérées comme String/Varchar pour la structure
        map.put("statut", fieldType.VARCHAR);
        map.put("tests_humain", fieldType.VARCHAR); // Booleens gérés comme String pour la structure générique
        map.put("tests_bebe", fieldType.VARCHAR);
        map.put("tests_chien", fieldType.VARCHAR);
        map.put("tests_chat", fieldType.VARCHAR);
        map.put("date_arrivee", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        // Formatage pour l'INSERT SQL : 'valeur', 'valeur'...
        // Gestion des NULL pour les dates
        String naissanceStr = (this.date_naissance != null) ? "'" + this.date_naissance + "'" : "NULL";
        
        this.values = "'" + this.puce + "', " +
                      "'" + this.espece + "', " +
                      "'" + this.nom + "', " +
                      naissanceStr + ", " +
                      "'" + this.statut + "', " +
                      this.tests_humain + ", " +
                      this.tests_bebe + ", " +
                      this.tests_chien + ", " +
                      this.tests_chat + ", " +
                      "'" + this.date_arrivee + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return this.map;
    }

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

    // --- Getters et Setters ---

    public int getId_animal() { return id_animal; }
    public void setId_animal(int id_animal) { this.id_animal = id_animal; }

    public String getPuce() { return puce; }
    public void setPuce(String puce) { this.puce = puce; }

    public String getEspece() { return espece; }
    public void setEspece(String espece) { this.espece = espece; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Date getDate_naissance() { return date_naissance; }
    public void setDate_naissance(Date date_naissance) { this.date_naissance = date_naissance; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    // Pour les booléens, la convention est souvent "is..." ou "get..."
    public boolean isTests_humain() { return tests_humain; }
    public void setTests_humain(boolean tests_humain) { this.tests_humain = tests_humain; }

    public boolean isTests_bebe() { return tests_bebe; }
    public void setTests_bebe(boolean tests_bebe) { this.tests_bebe = tests_bebe; }

    public boolean isTests_chien() { return tests_chien; }
    public void setTests_chien(boolean tests_chien) { this.tests_chien = tests_chien; }

    public boolean isTests_chat() { return tests_chat; }
    public void setTests_chat(boolean tests_chat) { this.tests_chat = tests_chat; }

    public Date getDate_arrivee() { return date_arrivee; }
    public void setDate_arrivee(Date date_arrivee) { this.date_arrivee = date_arrivee; }

    @Override
    public String toString() {
        return "Animal [ID=" + id_animal + ", Nom=" + nom + ", Espèce=" + espece + ", Statut=" + statut + "]";
    }
}