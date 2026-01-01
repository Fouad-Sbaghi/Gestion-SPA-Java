package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Activite;

public class ActiviteRequest {

    /**
     * Récupère la liste des types d'activités disponibles (pour le planning).
     * Table : Activite
     */
    public List<Activite> getAll() {
        List<Activite> liste = new ArrayList<>();
        String sql = "SELECT * FROM Activite ORDER BY type_act ASC";

        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(new Activite(
                    rs.getInt("id_activite"),
                    rs.getString("type_act")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Liste Activités) : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Ajoute un nouveau type d'activité (ex: "Toilettage").
     * Table : Activite
     */
    public void addType(String type) {
        String sql = "INSERT INTO Activite (type_act) VALUES (?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.executeUpdate();
            System.out.println("Nouveau type d'activité ajouté : " + type);

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Ajout Type Activité) : " + e.getMessage());
        }
    }

    /**
     * Enregistre une activité effectuée sur un animal spécifique.
     * Note : Stocké dans la table 'Soin' car 'Activite' ne contient pas d'ID animal.
     * Utilisé par ton Controller.
     */
    public void add(int idAnimal, String type, String commentaire) {
        String sql = "INSERT INTO Soin (id_animal, type_soin, libelle, commentaire, date_soin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setString(2, type); // ex: "Promenade"
            pstmt.setString(3, "Activité Quotidienne");
            pstmt.setString(4, commentaire != null ? commentaire : "RAS");
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            pstmt.executeUpdate();
            System.out.println("Activité enregistrée pour l'animal #" + idAnimal);

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Ajout Activité Animal) : " + e.getMessage());
        }
    }

    /**
     * Supprime un type d'activité.
     */
    public boolean deleteType(int id) {
        String sql = "DELETE FROM Activite WHERE id_activite = ?";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression (Utilisé dans le planning ?) : " + e.getMessage());
            return false;
        }
    }
}