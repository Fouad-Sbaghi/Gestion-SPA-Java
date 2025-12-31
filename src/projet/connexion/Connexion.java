package projet.connexion;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    public static java.sql.Connection connection;
    
    public static java.sql.Connection connectR() {
        try {
            Class.forName("org.postgresql.Driver");
            
            String dataBase = "etd";
            String port = "5432";
            String user = "uapv2502163";
            String pswd = "projetams2026";

            String url = "jdbc:postgresql://pedago.univ-avignon.fr:" + port + "/" + dataBase;

            connection = DriverManager.getConnection(url, user, pswd);
            
            // --- ON COMMENTE CETTE LIGNE ---
            // System.out.println("Connexion réussie"); 
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); 
            // On laisse celle-ci pour savoir si ça plante
            System.err.println("Connexion échouée"); 
        }
        
        return connection;   
    }
    
    public static void close() throws SQLException {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
                // --- ON COMMENTE CETTE LIGNE ---
                // System.out.println("Déconnexion réussie");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}