package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import projet.connexion.Connexion;

public class IncidentRequest {

    /**
     * Crée un nouvel incident ET le lie immédiatement à un animal.
     * Cette méthode effectue deux insertions (Table Incident + Table Animal_Incident).
     * * @param idAnimal L'ID de l'animal concerné.
     * @param type Le type (Maladie, Accident, etc.).
     * @param description L'intitulé ou la description courte.
     */
    public void add(int idAnimal, String type, String description) {
        // 1. Requête pour créer l'incident
        String sqlIncident = "INSERT INTO Incident (type_incident, intitule, date_incident) VALUES (?, ?, ?)";
        
        // 2. Requête pour faire le lien avec l'animal
        String sqlLien = "INSERT INTO Animal_Incident (id_animal, id_incident) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstmtInc = null;
        PreparedStatement pstmtLien = null;
        ResultSet rs = null;

        try {
            conn = Connexion.connectR();
            
            // --- ÉTAPE 1 : Insertion de l'incident ---
            // On demande à récupérer la clé générée (l'ID incident créé)
            pstmtInc = conn.prepareStatement(sqlIncident, Statement.RETURN_GENERATED_KEYS);
            pstmtInc.setString(1, type);
            pstmtInc.setString(2, description);
            pstmtInc.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            
            int rows = pstmtInc.executeUpdate();

            if (rows > 0) {
                // On récupère l'ID qui vient d'être créé par la base de données
                rs = pstmtInc.getGeneratedKeys();
                if (rs.next()) {
                    int idIncidentGenere = rs.getInt(1);

                    // --- ÉTAPE 2 : Création du lien avec l'animal ---
                    pstmtLien = conn.prepareStatement(sqlLien);
                    pstmtLien.setInt(1, idAnimal);
                    pstmtLien.setInt(2, idIncidentGenere);
                    
                    pstmtLien.executeUpdate();
                    
                    System.out.println("Succès : Incident déclaré pour l'animal #" + idAnimal + " (Ref Incident: " + idIncidentGenere + ")");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur déclaration incident : " + e.getMessage());
        } finally {
            // Fermeture propre des ressources (manuelle car on a plusieurs statements)
            try {
                if (rs != null) rs.close();
                if (pstmtInc != null) pstmtInc.close();
                if (pstmtLien != null) pstmtLien.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Supprime un incident (et le lien associé grâce au CASCADE défini dans la BDD ou manuellement).
     */
    public boolean delete(int idIncident) {
        String sql = "DELETE FROM Incident WHERE id_incident = ?";
        
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, idIncident);
            int rows = pstmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression incident : " + e.getMessage());
            return false;
        }
    }
}