package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import projet.connexion.Connexion;

/**
 * DAO pour la gestion de la table de liaison Animal-Incident.

 * Gère l'association et la dissociation entre animaux et incidents.

 * 
 */
public class AnimalIncidentRequest {

    /**
     * Associe un incident existant à un animal.
     * Table : Animal_Incident
     */
    public boolean associer(int idAnimal, int idIncident) {
        String sql = "INSERT INTO Animal_Incident (id_animal, id_incident) VALUES (?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idIncident);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Lien créé : Incident #" + idIncident + " -> Animal #" + idAnimal);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur d'association (Déjà lié ?) : " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime le lien entre un animal et un incident (sans supprimer l'incident
     * lui-même).
     */
    public boolean dissocier(int idAnimal, int idIncident) {
        String sql = "DELETE FROM Animal_Incident WHERE id_animal = ? AND id_incident = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idIncident);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur de dissociation : " + e.getMessage());
            return false;
        }
    }
}