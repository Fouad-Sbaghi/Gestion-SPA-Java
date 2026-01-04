package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;
import projet.tables.Box;
import projet.exceptions.donnee.ElementIntrouvableException;

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
            // Souvent causé par une contrainte de clé étrangère (si le box a encore des
            // animaux)
            System.err.println("Erreur suppression (Le box est-il vide ?) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Affiche les informations détaillées d'un box (caractéristiques + animaux
     * actuellement présents).
     * Un animal est considéré "présent" si son séjour a DATE_F_BOX IS NULL.
     */
    public void afficherInfoBox(int idBox) throws ElementIntrouvableException {
        String sqlBox = """
                    SELECT b.id_box, b.type_box, b.capacite_max, COUNT(s.id_animal) as occupe
                    FROM Box b
                    LEFT JOIN Sejour_Box s ON b.id_box = s.id_box AND s.DATE_F_BOX IS NULL
                    WHERE b.id_box = ?
                    GROUP BY b.id_box, b.type_box, b.capacite_max
                """;

        String sqlAnimaux = """
                    SELECT a.id_animal, a.nom, a.espece, a.statut, a.puce, s.DATE_D
                    FROM Sejour_Box s
                    JOIN Animal a ON a.id_animal = s.id_animal
                    WHERE s.id_box = ? AND s.DATE_F_BOX IS NULL
                    ORDER BY s.DATE_D ASC, a.id_animal ASC
                """;

        try (Connection conn = Connexion.connectR()) {

            // 1) Infos box + occupation
            Integer cap = null;
            Integer occ = null;
            String type = null;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlBox)) {
                pstmt.setInt(1, idBox);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        type = rs.getString("type_box");
                        cap = rs.getInt("capacite_max");
                        occ = rs.getInt("occupe");
                    }
                }
            }

            if (cap == null || occ == null || type == null) {
                throw new ElementIntrouvableException("le box #" + idBox);
            }

            int libres = Math.max(0, cap - occ);
            String alerte = (occ >= cap) ? " (PLEIN)" : "";

            System.out.println("=== INFO BOX #" + idBox + " ===");
            System.out.println("Type      : " + type);
            System.out.println("Capacité  : " + cap);
            System.out.println("Occupé    : " + occ + alerte);
            System.out.println("Places    : " + libres + " libre(s)");

            // 2) Animaux présents
            System.out.println("\n--- Animaux présents ---");
            boolean any = false;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlAnimaux)) {
                pstmt.setInt(1, idBox);
                try (ResultSet rs = pstmt.executeQuery()) {
                    System.out.printf("%-8s | %-18s | %-12s | %-12s | %-14s | %s%n",
                            "ID", "Nom", "Espèce", "Statut", "Puce", "Depuis");
                    System.out.println(
                            "--------------------------------------------------------------------------------");
                    while (rs.next()) {
                        any = true;
                        int idAnimal = rs.getInt("id_animal");
                        String nom = rs.getString("nom");
                        String espece = rs.getString("espece");
                        String statut = rs.getString("statut");
                        String puce = rs.getString("puce");
                        java.sql.Date depuis = rs.getDate("DATE_D");

                        System.out.printf("%-8d | %-18s | %-12s | %-12s | %-14s | %s%n",
                                idAnimal,
                                (nom != null ? nom : ""),
                                (espece != null ? espece : ""),
                                (statut != null ? statut : ""),
                                (puce != null ? puce : ""),
                                (depuis != null ? depuis.toString() : ""));
                    }
                }
            }

            if (!any) {
                System.out.println("Aucun animal présent dans ce box.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Info Box) : " + e.getMessage());
        }
    }

    /**
     * Met à jour les informations d'un box.
     */
    public void update(Box box) {
        String sql = "UPDATE Box SET type_box = ?, capacite_max = ? WHERE id_box = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, box.getType_box());
            pstmt.setInt(2, box.getCapacite_max());
            pstmt.setInt(3, box.getId_box());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Box #" + box.getId_box() + " mis à jour.");
            } else {
                System.out.println("Erreur : Box #" + box.getId_box() + " introuvable.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Update Box) : " + e.getMessage());
        }
    }

    /**
     * Récupère le type d'un box.
     */
    public String getBoxType(int idBox) {
        String sql = "SELECT type_box FROM Box WHERE id_box = ?";
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBox);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("type_box");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (Get Box Type) : " + e.getMessage());
        }
        return null;
    }
}