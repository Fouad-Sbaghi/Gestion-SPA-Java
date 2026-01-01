package projet.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

public class SejourFamilleRequest {

    /**
     * Débute un séjour en famille (Accueil ou Adoption).
     * @param idAnimal L'animal concerné.
     * @param idFamille La famille d'accueil/adoptante.
     */
    public boolean commencerSejour(int idAnimal, int idFamille) {
        // On clôture d'abord tout séjour précédent potentiellement ouvert
        terminerSejour(idAnimal);

        String sql = "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idFamille);
            pstmt.setDate(3, new Date(System.currentTimeMillis())); // Date du jour

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : Animal #" + idAnimal + " confié à la famille #" + idFamille);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur début séjour famille : " + e.getMessage());
        }
        return false;
    }

    /**
     * Termine le séjour actuel d'un animal en famille (retour au refuge ou changement).
     * Met à jour la date de fin (DATE_F_FAMILLE) à aujourd'hui.
     */
    public boolean terminerSejour(int idAnimal) {
        String sql = "UPDATE Sejour_Famille SET DATE_F_FAMILLE = ? WHERE id_animal = ? AND DATE_F_FAMILLE IS NULL";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Fin du séjour en famille pour l'animal #" + idAnimal);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur fin séjour famille : " + e.getMessage());
        }
        return false;
    }

    /**
     * Récupère l'ID de la famille qui héberge actuellement l'animal.
     * @return ID de la famille, ou -1 si l'animal n'est pas en famille.
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
}