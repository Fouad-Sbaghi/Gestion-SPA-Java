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
        Date today = new Date(System.currentTimeMillis());

        // Verifier si un sejour existe deja pour aujourd'hui (meme clos)
        if (sejourExistePourAujourdhui(idAnimal, today)) {
            // Rouvrir le sejour existant en mettant DATE_F_FAMILLE a NULL et en changeant
            // la famille
            return rouvrirSejour(idAnimal, idFamille, today);
        }

        // Sinon, cloturer tout sejour precedent et en creer un nouveau
        terminerSejourSilent(idAnimal);

        String sql = "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idFamille);
            pstmt.setDate(3, today);

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
     * Verifie si un sejour existe pour cet animal a cette date.
     */
    private boolean sejourExistePourAujourdhui(int idAnimal, Date date) {
        String sql = "SELECT COUNT(*) FROM Sejour_Famille WHERE id_animal = ? AND DATE_D = ?";
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnimal);
            pstmt.setDate(2, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // Ignorer
        }
        return false;
    }

    /**
     * Rouvre un sejour existant en remettant DATE_F_FAMILLE a NULL.
     */
    private boolean rouvrirSejour(int idAnimal, int idFamille, Date date) {
        String sql = "UPDATE Sejour_Famille SET DATE_F_FAMILLE = NULL, id_famille = ? WHERE id_animal = ? AND DATE_D = ?";
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFamille);
            pstmt.setInt(2, idAnimal);
            pstmt.setDate(3, date);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succes : Sejour rouvert pour Animal #" + idAnimal + " chez famille #" + idFamille);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur reouverture sejour : " + e.getMessage());
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

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
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

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
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