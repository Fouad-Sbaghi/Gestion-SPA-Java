package projet.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;

/**
 * DAO pour la gestion du planning des animaux.

 * Planifie les rendez-vous entre animaux, bénévoles et créneaux.

 * 
 */
public class PlanningAnimalRequest {

    /**
     * Assigne un animal à un bénévole pour un créneau et une date donnés.
     * 
     * @param idAnimal  L'animal concerné.
     * @param idCreneau Le créneau horaire.
     * @param idPers    Le bénévole responsable.
     * @param date      La date de l'action.
     */
    public boolean assigner(int idAnimal, int idCreneau, int idPers, Date date) {
        String sql = "INSERT INTO Planning_Animal (id_animal, id_creneau, id_pers, DATE_D) VALUES (?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idCreneau);
            pstmt.setInt(3, idPers);
            pstmt.setDate(4, date);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : Rendez-vous planifié pour le " + date);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur planning (Créneau déjà pris ?) : " + e.getMessage());
        }
        return false;
    }

    /**
     * Affiche le planning complet pour une date spécifique.
     * 
     * @param date La date à consulter.
     */
    public void afficherPlanningJour(Date date) {
        String sql = """
                    SELECT c.heure_d, c.heure_f, a.nom AS nom_animal, p.nom AS nom_benevole, p.prenom
                    FROM Planning_Animal pa
                    JOIN Animal a ON pa.id_animal = a.id_animal
                    JOIN Personnel p ON pa.id_pers = p.id_pers
                    JOIN Creneau c ON pa.id_creneau = c.id_creneau
                    WHERE pa.DATE_D = ?
                    ORDER BY c.heure_d ASC
                """;

        System.out.println("=== PLANNING DU " + date + " ===");
        System.out.printf("%-15s | %-15s | %-20s%n", "Heures", "Animal", "Bénévole");
        System.out.println("--------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, date);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String heures = rs.getTime("heure_d") + " - " + rs.getTime("heure_f");
                    String animal = rs.getString("nom_animal");
                    String benevole = rs.getString("nom_benevole") + " " + rs.getString("prenom");

                    System.out.printf("%-15s | %-15s | %-20s%n", heures, animal, benevole);
                }

                if (!found) {
                    System.out.println("Aucune activité prévue à cette date.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lecture planning : " + e.getMessage());
        }
    }

    /**
     * Annule un rendez-vous spécifique.
     */
    public void annuler(int idAnimal, int idCreneau, int idPers, Date date) {
        String sql = "DELETE FROM Planning_Animal WHERE id_animal=? AND id_creneau=? AND id_pers=? AND DATE_D=?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idCreneau);
            pstmt.setInt(3, idPers);
            pstmt.setDate(4, date);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Rendez-vous annulé.");
            } else {
                System.out.println("Erreur : Rendez-vous introuvable.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur annulation : " + e.getMessage());
        }
    }
}