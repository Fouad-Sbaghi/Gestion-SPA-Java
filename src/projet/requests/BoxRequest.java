package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;
import projet.tables.Box;

public class BoxRequest {

    /**
     * Affiche directement l'état des box (ID, Type, Capacité, Occupation actuelle).
     * Calcule l'occupation en comptant les séjours en cours (date_fin IS NULL).
     */
    public void afficherOccupation() {
        // Requête complexe : Jointure pour compter les animaux présents
        String sql = """
            SELECT b.id_box, b.type_box, b.capacite_max, COUNT(s.id_animal) as occupe
            FROM Box b
            LEFT JOIN Sejour_Box s ON b.id_box = s.id_box AND s.DATE_F_BOX IS NULL
            GROUP BY b.id_box, b.type_box, b.capacite_max
            ORDER BY b.id_box ASC
        """;

        System.out.printf("%-5s | %-15s | %-10s | %-10s%n", "ID", "Type", "Capacité", "Occupé");
        System.out.println("---------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_box");
                String type = rs.getString("type_box");
                int cap = rs.getInt("capacite_max");
                int occ = rs.getInt("occupe");

                // Affichage avec alerte visuelle si plein
                String alerte = (occ >= cap) ? " (PLEIN)" : "";
                
                System.out.printf("%-5d | %-15s | %-10d | %-10s%n", id, type, cap, occ + alerte);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Occupation Box) : " + e.getMessage());
        }
    }

    /**
     * Ajoute un nouveau box.
     */
    public void add(Box box) {
        String sql = "INSERT INTO Box (type_box, capacite_max) VALUES (?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, box.getType_box());
            pstmt.setInt(2, box.getCapacite_max());
            pstmt.executeUpdate();
            System.out.println("Box ajouté avec succès.");

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Ajout Box) : " + e.getMessage());
        }
    }

    /**
     * Supprime un box si vide.
     */
    public boolean delete(int idBox) {
        String sql = "DELETE FROM Box WHERE id_box = ?";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBox);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            // Souvent causé par une contrainte de clé étrangère (si le box a encore des animaux)
            System.err.println("Erreur suppression (Le box est-il vide ?) : " + e.getMessage());
            return false;
        }
    }
}