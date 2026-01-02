package projet.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

public class SejourFamilleRequest {

    /**
     * Debute un sejour en famille (Accueil ou Adoption).
     */
    public boolean commencerSejour(int idAnimal, int idFamille) {
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

        // Verifier si un sejour existe deja pour aujourd'hui (meme clos)
        // Note: Avec le TIMESTAMP, on pourrait autoriser plusieurs, mais on garde la
        // logique "existe pour aujourd'hui" un peu floue ou on l'adapte.
        // On va simplifier : un animal peut changer de famille plusieurs fois par jour
        // si TIMESTAMP.

        // Sinon, cloturer tout sejour precedent et en creer un nouveau
        terminerSejourSilent(idAnimal);

        String sql = "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idFamille);
            pstmt.setTimestamp(3, now);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succes : Animal #" + idAnimal + " confie a la famille #" + idFamille);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur debut sejour famille : " + e.getMessage());
        }
        return false;
    }

    /**
     * Termine le sejour actuel d'un animal en famille (version silencieuse pour
     * appel interne).
     */
    private void terminerSejourSilent(int idAnimal) {
        String sql = "UPDATE Sejour_Famille SET DATE_F_FAMILLE = ? WHERE id_animal = ? AND DATE_F_FAMILLE IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);
            pstmt.executeUpdate(); // Pas de message

        } catch (SQLException e) {
            // Silencieux
        }
    }

    /**
     * Termine le sejour actuel d'un animal en famille.
     */
    public boolean terminerSejour(int idAnimal) {
        String sql = "UPDATE Sejour_Famille SET DATE_F_FAMILLE = ? WHERE id_animal = ? AND DATE_F_FAMILLE IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Sejour termine pour l'animal #" + idAnimal);
                return true;
            } else {
                System.out.println("Info : Aucun sejour en cours trouve pour cet animal.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur fin sejour famille : " + e.getMessage());
        }
        return false;
    }

    /**
     * Récupère l'ID de la famille qui héberge actuellement l'animal.
     */
    public int getFamilleActuelle(int idAnimal) {
        String sql = "SELECT id_famille FROM Sejour_Famille WHERE id_animal = ? AND DATE_F_FAMILLE IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_famille");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche famille actuelle : " + e.getMessage());
        }
        return -1; // Pas en famille
    }

    /**
     * AJOUT : Affiche l'historique des animaux pour une famille donnée.
     */
    public void afficherHistoriqueParFamille(int idFamille) {
        String sql = """
                    SELECT a.nom, a.espece, s.DATE_D, s.DATE_F_FAMILLE
                    FROM Sejour_Famille s
                    JOIN Animal a ON s.id_animal = a.id_animal
                    WHERE s.id_famille = ?
                    ORDER BY s.DATE_D DESC
                """;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFamille);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("--- Animaux accueillis ---");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String nom = rs.getString("nom");
                    String espece = rs.getString("espece");
                    Date debut = rs.getDate("DATE_D");
                    Date fin = rs.getDate("DATE_F_FAMILLE");
                    String finStr = (fin != null) ? fin.toString() : "EN COURS";

                    System.out.printf("- %s (%s) : du %s au %s%n", nom, espece, debut, finStr);
                }
                if (!found)
                    System.out.println("Aucun historique pour cette famille.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur historique famille : " + e.getMessage());
        }
    }
}