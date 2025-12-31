package projet.gestion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;
import projet.tables.ITable;

public class Gestion {

    /**
     * Exécute une requête SQL de mise à jour (INSERT, UPDATE, DELETE, CREATE, DROP).
     */
    public void execute(String sql) throws SQLException {
        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            // Pas d'affichage ici pour laisser le choix à l'appelant
        }
    }

    /**
     * Insère un objet implémentant ITable dans la base de données.
     * Utilise la méthode getValues() de l'objet.
     */
    public void insert(ITable obj, String nomTable) {
        String values = obj.getValues();
        String sql = "INSERT INTO " + nomTable + " VALUES (" + values + ")";
        
        try {
            execute(sql);
            System.out.println("Insertion réussie dans " + nomTable);
        } catch (SQLException e) {
            System.err.println("Erreur d'insertion : " + e.getMessage());
        }
    }

    /**
     * Affiche tout le contenu d'une table dans la console (SELECT *).
     */
    public void displayTable(String nomTable) {
        String sql = "SELECT * FROM " + nomTable;

        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int nbCols = rsmd.getColumnCount();

            System.out.println("--- Contenu de la table " + nomTable + " ---");

            // Affichage des en-têtes
            for (int i = 1; i <= nbCols; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t | ");
            }
            System.out.println("\n-------------------------------------------------");

            // Affichage des données
            while (rs.next()) {
                for (int i = 1; i <= nbCols; i++) {
                    System.out.print(rs.getString(i) + "\t | ");
                }
                System.out.println();
            }
            System.out.println("-------------------------------------------------");

        } catch (SQLException e) {
            System.err.println("Erreur lecture table : " + e.getMessage());
        }
    }
    
    /**
     * Affiche la structure (colonnes et types) d'une table.
     */
    public void structTable(String nomTable) {
        String sql = "SELECT * FROM " + nomTable + " LIMIT 1"; // On ne veut que les métadonnées

        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int nbCols = rsmd.getColumnCount();

            System.out.println("--- Structure de " + nomTable + " ---");
            for (int i = 1; i <= nbCols; i++) {
                System.out.println("- " + rsmd.getColumnName(i) + " : " + rsmd.getColumnTypeName(i));
            }

        } catch (SQLException e) {
            System.err.println("Erreur structure : " + e.getMessage());
        }
    }
}