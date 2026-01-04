package projet.tables;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente un animal de la SPA.
 * Mappe la table 'Animal' avec puce, tests comportementaux et statut.
 */
public class Animal implements ITable {

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

    private String values;
    private HashMap<String, fieldType> map;

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

    @Override
    public void getStruct() {
        this.map = new HashMap<>();
        map.put("id_animal", fieldType.INT4);
        map.put("puce", fieldType.VARCHAR);
        map.put("espece", fieldType.VARCHAR);
        map.put("nom", fieldType.VARCHAR);
        map.put("date_naissance", fieldType.VARCHAR);
        map.put("statut", fieldType.VARCHAR);
        map.put("tests_humain", fieldType.VARCHAR);
        map.put("tests_bebe", fieldType.VARCHAR);
        map.put("tests_chien", fieldType.VARCHAR);
        map.put("tests_chat", fieldType.VARCHAR);
        map.put("date_arrivee", fieldType.VARCHAR);
    }

    @Override
    public String getValues() {
        String naissanceStr = (this.date_naissance != null) ? "'" + this.date_naissance + "'" : "NULL";

        this.values = "'" + this.puce + "', '" + this.espece + "', '" + this.nom + "', " +
                naissanceStr + ", '" + this.statut + "', " +
                this.tests_humain + ", " + this.tests_bebe + ", " +
                this.tests_chien + ", " + this.tests_chat + ", '" + this.date_arrivee + "'";
        return this.values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return this.map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        if (this.map.size() != tableStruct.size())
            return false;
        for (Map.Entry<String, fieldType> entry : this.map.entrySet()) {
            if (!tableStruct.containsKey(entry.getKey()) || tableStruct.get(entry.getKey()) != entry.getValue())
                return false;
        }
        return true;
    }

    // Getters & Setters
    public int getId_animal() {
        return id_animal;
    }

    public void setId_animal(int id) {
        this.id_animal = id;
    }

    public String getPuce() {
        return puce;
    }

    public void setPuce(String p) {
        this.puce = p;
    }

    public String getEspece() {
        return espece;
    }

    public void setEspece(String e) {
        this.espece = e;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String n) {
        this.nom = n;
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date d) {
        this.date_naissance = d;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String s) {
        this.statut = s;
    }

    public boolean isTests_humain() {
        return tests_humain;
    }

    public void setTests_humain(boolean t) {
        this.tests_humain = t;
    }

    public boolean isTests_bebe() {
        return tests_bebe;
    }

    public void setTests_bebe(boolean t) {
        this.tests_bebe = t;
    }

    public boolean isTests_chien() {
        return tests_chien;
    }

    public void setTests_chien(boolean t) {
        this.tests_chien = t;
    }

    public boolean isTests_chat() {
        return tests_chat;
    }

    public void setTests_chat(boolean t) {
        this.tests_chat = t;
    }

    public Date getDate_arrivee() {
        return date_arrivee;
    }

    public void setDate_arrivee(Date d) {
        this.date_arrivee = d;
    }

    @Override
    public String toString() {
        String naiss = (date_naissance != null) ? date_naissance.toString() : "?";
        String puceStr = (puce != null) ? puce : "-";
        String tests = String.format("H:%s B:%s Ch:%s Ct:%s",
                tests_humain ? "V" : "F",
                tests_bebe ? "V" : "F",
                tests_chien ? "V" : "F",
                tests_chat ? "V" : "F");

        return String.format("[#%d] %s (%s) - Puce: %s | Ne(e): %s | Statut: %s | Tests: %s | Arrivee: %s",
                id_animal, nom, espece, puceStr, naiss, statut, tests, date_arrivee);
    }
}