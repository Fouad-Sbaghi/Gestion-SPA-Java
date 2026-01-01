package projet.requests.rapports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

public class RapportHistoriqueAnimal extends RapportRequest {

    /**
     * Affiche le dossier complet d'un animal (Identité, Soins, Incidents, Séjours).
     * 
     * @param idAnimal L'identifiant de l'animal à consulter.
     */
    public void afficherDossier(int idAnimal) {
        String sqlAnimal = "SELECT * FROM Animal WHERE id_animal = ?";

        String sqlSoins = "SELECT * FROM Soin WHERE id_animal = ? ORDER BY date_soin DESC";

        String sqlIncidents = """
                    SELECT i.date_incident, i.type_incident, i.intitule
                    FROM Incident i
                    JOIN Animal_Incident ai ON i.id_incident = ai.id_incident
                    WHERE ai.id_animal = ?
                    ORDER BY i.date_incident DESC
                """;

        String sqlSejourBox = """
                    SELECT b.type_box, s.DATE_D, s.DATE_F_BOX
                    FROM Sejour_Box s
                    JOIN Box b ON s.id_box = b.id_box
                    WHERE s.id_animal = ?
                    ORDER BY s.DATE_D DESC
                """;

        String sqlSejourFamille = """
                    SELECT f.nom, f.type_famille, s.DATE_D, s.DATE_F_FAMILLE
                    FROM Sejour_Famille s
                    JOIN Famille f ON s.id_famille = f.id_famille
                    WHERE s.id_animal = ?
                    ORDER BY s.DATE_D DESC
                """;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstAn = conn.prepareStatement(sqlAnimal);
                PreparedStatement pstSoin = conn.prepareStatement(sqlSoins);
                PreparedStatement pstInc = conn.prepareStatement(sqlIncidents);
                PreparedStatement pstBox = conn.prepareStatement(sqlSejourBox);
                PreparedStatement pstFam = conn.prepareStatement(sqlSejourFamille)) {

            // 1. Identité
            pstAn.setInt(1, idAnimal);
            try (ResultSet rs = pstAn.executeQuery()) {
                if (rs.next()) {
                    printHeader("=== DOSSIER ANIMAL #" + idAnimal + " ===");
                    System.out.println("Nom : " + rs.getString("nom"));
                    System.out.println("Espèce : " + rs.getString("espece"));
                    System.out.println("Puce : " + rs.getString("puce"));
                    System.out.println("Statut Actuel : " + rs.getString("statut"));
                    System.out.println("Arrivée le : " + rs.getDate("date_arrivee"));
                } else {
                    System.out.println("Erreur : Aucun animal trouvé avec l'ID " + idAnimal);
                    return;
                }
            }

            System.out.println("\n--- Historique Médical (Soins) ---");
            pstSoin.setInt(1, idAnimal);
            try (ResultSet rs = pstSoin.executeQuery()) {
                boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    System.out.printf("- [%s] %s : %s (%s)%n",
                            rs.getTimestamp("date_soin").toString().split(" ")[0], // Date seulement
                            rs.getString("type_soin"),
                            rs.getString("libelle"),
                            rs.getString("commentaire"));
                }
                if (empty)
                    System.out.println("Aucun soin enregistré.");
            }

            System.out.println("\n--- Incidents & Accidents ---");
            pstInc.setInt(1, idAnimal);
            try (ResultSet rs = pstInc.executeQuery()) {
                boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    System.out.printf("- [%s] %s : %s%n",
                            rs.getTimestamp("date_incident").toString().split(" ")[0],
                            rs.getString("type_incident"),
                            rs.getString("intitule"));
                }
                if (empty)
                    System.out.println("R.A.S (Aucun incident).");
            }

            System.out.println("\n--- Historique des Logements (Box) ---");
            pstBox.setInt(1, idAnimal);
            try (ResultSet rs = pstBox.executeQuery()) {
                boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    String fin = (rs.getDate("DATE_F_BOX") != null) ? rs.getDate("DATE_F_BOX").toString()
                            : "ACTUELLEMENT";
                    System.out.printf("- Box %s : du %s au %s%n",
                            rs.getString("type_box"),
                            rs.getDate("DATE_D"),
                            fin);
                }
                if (empty)
                    System.out.println("Aucun historique de box.");
            }

            System.out.println("\n--- Historique des Logements (Famille) ---");
            pstFam.setInt(1, idAnimal);
            try (ResultSet rs = pstFam.executeQuery()) {
                boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    String fin = (rs.getDate("DATE_F_FAMILLE") != null) ? rs.getDate("DATE_F_FAMILLE").toString()
                            : "ACTUELLEMENT";
                    System.out.printf("- %s (%s) : du %s au %s%n",
                            rs.getString("nom"),
                            rs.getString("type_famille"),
                            rs.getDate("DATE_D"),
                            fin);
                }
                if (empty)
                    System.out.println("Aucun séjour en famille.");
            }

            System.out.println("=========================================");

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Dossier Animal) : " + e.getMessage());
        }
    }
}