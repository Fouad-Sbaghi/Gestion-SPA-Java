package projet.gestion;

import java.sql.*;
import java.util.HashMap;
import projet.tables.*;
import projet.connexion.*;
import projet.main.Produit;

public class Gestion {

    public HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException 
    {
    	Connection conn = Connexion.connectR();
    	DatabaseMetaData meta = conn.getMetaData();
    	ResultSet rs = meta.getColumns(null, "uapv2502163", table.toLowerCase(), null);
    	HashMap<String, fieldType> structure = new HashMap<>();
    	
    	while(rs.next())
    	{
    		String colName = rs.getString("COLUMN_NAME");
            String colType = rs.getString("TYPE_NAME");
            
            fieldType typeEnum = null;
            try 
            {
                typeEnum = fieldType.valueOf(colType.toUpperCase());
            } 
            catch (IllegalArgumentException e) 
            {
                if(colType.equalsIgnoreCase("serial")) typeEnum = fieldType.INT4; // on traite le cas particulier du serial (nottement utilisé pour les id auto increment en sql)
            }

            if (typeEnum != null) {
                structure.put(colName, typeEnum);
                
                if(display) {
                    System.out.println("Colonne: " + colName + " | Type: " + typeEnum);
                }
            }
    	}
    	return structure;
    }

    public void displayTable(String table) throws SQLException 
    {
    	Connection conn = Connexion.connectR();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
        ResultSetMetaData rsMeta = rs.getMetaData(); // Pour savoir combien il y a de colonnes

        System.out.println("--- Contenu de la table " + table + " ---");

        while (rs.next()) {
            // On parcourt toutes les colonnes de la ligne actuelle
            for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                System.out.print(rs.getObject(i) + "\t | \t"); // Affichage avec tab
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
    }

    public void execute(String query) throws SQLException 
    {
    	Connection conn = Connexion.connectR();
        if (conn != null) {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            stmt.close(); 
        }
        else
        {
            throw new SQLException("Pas de connexion active.");   
        }
    }

    public void insert(ITable data, String table) 
    {
        try 
        {
            HashMap<String, fieldType> tableStruct = structTable(table, false);
            
            if (!data.check(tableStruct)) {
                System.out.println("Erreur : La structure de l'objet ne correspond pas à la table " + table);
                return;
            }

            String query = "INSERT INTO " + table + " VALUES (" + data.getValues() + ")";
            
            try 
            {
                execute(query);
                System.out.println("Insertion réussie.");
                
            }
            catch (SQLException e) 
            {
                System.out.println("Doublon détecté (ID existant)");
                
                if (data instanceof Produit) 
                {
                    Produit p = (Produit) data;
                    
                    String updateQuery = "UPDATE " + table + " SET " +
                                         "prix = prix + " + p.getPrix() + ", " +
                                         "description = description || ' ' || '" + p.getDescription() + "' " +
                                         "WHERE id = " + p.getId();
                    
                    execute(updateQuery);
                }
            }

        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
















