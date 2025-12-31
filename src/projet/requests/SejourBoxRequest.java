package projet.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

public class SejourBoxRequest {

    /**
     * Place un animal dans un box (Démarre un séjour).
     * @param idAnimal L'animal à placer.
     * @param idBox Le box de destination.
     */
    public boolean placerAnimal(int idAnimal, int idBox) {
        // 1. D'abord, on s'assure que l'animal n'est pas déjà considéré comme "actif" dans un autre box
        sortirAnimal(idAnimal); 

        String sql = "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idBox);
            pstmt.setDate(3, new Date(System.currentTimeMillis())); // Date du jour

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : Animal #" + idAnimal + " placé dans le box #" + idBox);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur placement (Box plein ou animal déjà placé ?) : " + e.getMessage());
        }
        return false;
    }

    /**
     * "Sort" un animal de son box actuel (Met à jour la date de fin).
     * Utilisé lors d'une adoption, d'un départ en famille d'accueil ou d'un changement de box.
     */
    public void sortirAnimal(int idAnimal) {
        // On cherche le séjour en cours (celui où DATE_F_BOX est NULL)
        String sql = "UPDATE Sejour_Box SET DATE_F_BOX = ? WHERE id_animal = ? AND DATE_F_BOX IS NULL";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Animal #" + idAnimal + " sorti de son box.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur sortie de box : " + e.getMessage());
        }
    }

    /**
     * Récupère l'ID du box où se trouve actuellement l'animal.
     * @return L'ID du box, ou -1 si l'animal n'est pas en box.
     */
    public int getBoxActuel(int idAnimal) {
        String sql = "SELECT id_box FROM Sejour_Box WHERE id_animal = ? AND DATE_F_BOX IS NULL";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_box");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Pas trouvé
    }
}