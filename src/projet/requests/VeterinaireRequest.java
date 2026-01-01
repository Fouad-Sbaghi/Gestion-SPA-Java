package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

public class VeterinaireRequest {

    /**
     * Associe un vétérinaire (Personnel) à un acte médical (Soin).
     * @param idPersonne L'ID du vétérinaire (table Personnel).
     * @param idSoin L'ID du soin effectué.
     */
    public boolean assigner(int idPersonne, int idSoin) {
        String sql = "INSERT INTO Veterinaire (id_personne, id_soin) VALUES (?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonne);
            pstmt.setInt(2, idSoin);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Soin #" + idSoin + " signé par le vétérinaire #" + idPersonne);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur signature soin : " + e.getMessage());
        }
        return false;
    }

    /**
     * Récupère le nom complet du vétérinaire qui a effectué un soin donné.
     * @param idSoin L'ID du soin.
     * @return "Nom Prénom" du vétérinaire, ou "Inconnu" si non trouvé.
     */
    public String getNomVeterinaire(int idSoin) {
        String sql = """
            SELECT p.nom, p.prenom 
            FROM Personnel p
            JOIN Veterinaire v ON p.id_pers = v.id_personne
            WHERE v.id_soin = ?
        """;

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSoin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom") + " " + rs.getString("prenom");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération vétérinaire : " + e.getMessage());
        }
        return "Non assigné";
    }
    
    /**
     * Supprime la signature (en cas d'erreur d'affectation).
     */
    public void retirerSignature(int idSoin) {
        String sql = "DELETE FROM Veterinaire WHERE id_soin = ?";
        
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSoin);
            pstmt.executeUpdate();
            System.out.println("Signature vétérinaire retirée du soin #" + idSoin);

        } catch (SQLException e) {
            System.err.println("Erreur suppression signature : " + e.getMessage());
        }
    }
}