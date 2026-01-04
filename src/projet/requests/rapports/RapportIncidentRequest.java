package projet.requests.rapports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

/**
 * Rapport des incidents par animal.

 * Liste les accidents et maladies déclarés.

 */
public class RapportIncidentRequest extends RapportRequest {

    /**
     * Affiche la liste des incidents (maladies, accidents) pour un animal donné.
     * 
     * @param idAnimal L'ID de l'animal concerné.
     */
    public void afficherParAnimal(int idAnimal) {
        String sql = """
                    SELECT i.date_incident, i.type_incident, i.intitule, i.commentaire
                    FROM Incident i
                    JOIN Animal_Incident ai ON i.id_incident = ai.id_incident
                    WHERE ai.id_animal = ?
                    ORDER BY i.date_incident DESC
                """;

        printHeader("=== RAPPORT INCIDENTS (Animal #" + idAnimal + ") ===");
        System.out.printf("%-12s | %-15s | %-20s | %s%n", "Date", "Type", "Intitulé", "Détail");
        System.out.println("--------------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    // Formatage de la date pour ne garder que YYYY-MM-DD si c'est un Timestamp
                    String dateStr = rs.getString("date_incident");
                    if (dateStr != null && dateStr.length() > 10) {
                        dateStr = dateStr.substring(0, 10);
                    }

                    String type = rs.getString("type_incident");
                    String titre = rs.getString("intitule");
                    String comm = rs.getString("commentaire");
                    if (comm == null)
                        comm = "-";

                    System.out.printf("%-12s | %-15s | %-20s | %s%n", dateStr, type, titre, comm);
                }

                if (!found) {
                    System.out.println("Aucun incident ou maladie déclaré pour cet animal.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Rapport Incidents) : " + e.getMessage());
        }
    }
}