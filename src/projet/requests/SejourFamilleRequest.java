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
     * Termine le séjour actuel d'un animal en famille.
     */
    public boolean terminerSejour(int idAnimal) {
        String sql = "UPDATE Sejour_Famille SET DATE_F_FAMILLE = ? WHERE id_animal = ? AND DATE_F_FAMILLE IS NULL";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Séjour terminé pour l'animal #" + idAnimal);
                return true; // Ajout du return true pour le controller
            } else {
                System.out.println("Erreur : Aucun séjour en cours trouvé pour ce couple Animal/Famille.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur fin séjour famille : " + e.getMessage());
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
                if (!found) System.out.println("Aucun historique pour cette famille.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur historique famille : " + e.getMessage());
        }
    }
}