package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Soin;

/**
 * DAO pour la gestion des soins vétérinaires.

 * Gère l'enregistrement et l'historique médical des animaux.

 * 
 */
public class SoinRequest {

    /**
     * Ajoute un soin vétérinaire ou une intervention médicale.
     * 
     * @param idAnimal    L'animal concerné.
     * @param type        "Vaccin", "Chirurgie", "Examen", etc.
     * @param libelle     Nom du médicament ou de l'acte (ex: "Rabies",
     *                    "Stérilisation").
     * @param commentaire Détails ou observations du vétérinaire.
     */
    public void add(int idAnimal, String type, String libelle, String commentaire) {
        String sql = "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setString(2, type);
            pstmt.setString(3, libelle);
            pstmt.setString(4, commentaire);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Soin enregistré : " + type + " (" + libelle + ")");
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout soin : " + e.getMessage());
        }
    }

    /**
     * Récupère la liste complète des soins d'un animal (Historique médical).
     */
    public List<Soin> getByAnimal(int idAnimal) {
        List<Soin> liste = new ArrayList<>();
        String sql = "SELECT * FROM Soin WHERE id_animal = ? ORDER BY date_soin DESC";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Soin s = new Soin();
                    s.setId_soin(rs.getInt("id_soin"));
                    s.setId_animal(rs.getInt("id_animal"));
                    s.setType_soin(rs.getString("type_soin"));
                    s.setLibelle(rs.getString("libelle"));
                    s.setCommentaire(rs.getString("commentaire"));
                    s.setDate_soin(rs.getTimestamp("date_soin"));

                    liste.add(s);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur historique soins : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Supprime un soin (en cas d'erreur de saisie).
     */
    public boolean delete(int idSoin) {
        String sql = "DELETE FROM Soin WHERE id_soin = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSoin);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression soin : " + e.getMessage());
            return false;
        }
    }
}