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
            System.out.println("Connexion réussie");
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); 
            System.out.println("Connexion échouée");
        }
        
		return connection;   
    }
    
    public static void close() throws SQLException
    {
    	try
    	{
        	if(connection != null && !connection.isClosed())
        	{
        		connection.close();
        		System.out.println("Déconnexion réussie");
        	}
    	}
    	catch(SQLException e)
    	{
    		System.out.println("Déconnexion écouhée");
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) { 
    	Connexion.connectR();
    }

}
