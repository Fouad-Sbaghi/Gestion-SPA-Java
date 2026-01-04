package projet.connexion;

import java.sql.DriverManager;
import java.sql.SQLException;
import projet.exceptions.ConnexionBDDException;

public class Connexion {

    public static java.sql.Connection connection;

    /**
     * Etablit une connexion à la base de données PostgreSQL.
     * 
     * @return La connexion établie, ou null en cas d'échec.
     */
    public static java.sql.Connection connectR() {
        try {
            Class.forName("org.postgresql.Driver");

            String dataBase = "etd";
            String port = "5432";
            String user = "uapv2502163";
            String pswd = "projetams2026";

            String url = "jdbc:postgresql://pedago.univ-avignon.fr:" + port + "/" + dataBase;

            connection = DriverManager.getConnection(url, user, pswd);

        } catch (ClassNotFoundException e) {
            ConnexionBDDException ex = new ConnexionBDDException("Driver PostgreSQL introuvable: " + e.getMessage());
            System.err.println(ex.getMessage());
        } catch (SQLException e) {
            ConnexionBDDException ex = new ConnexionBDDException(e);
            System.err.println(ex.getMessage());
        }

        return connection;
    }

    public static void close() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                // --- ON COMMENTE CETTE LIGNE ---
                // System.out.println("Déconnexion réussie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}