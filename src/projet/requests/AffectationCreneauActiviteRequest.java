package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;
import projet.exceptions.regle.CapaciteInsuffisanteException;

public class AffectationCreneauActiviteRequest {

    /**
     * Assigne un bénévole à un créneau pour une activité donnée.
     * Table : Affectation_Creneau_Activite
     * 
     * @throws CapaciteInsuffisanteException si le créneau a atteint sa capacité
     *                                       maximale.
     */
    public boolean assigner(int idCreneau, int idPersonne, int idActivite) throws CapaciteInsuffisanteException {
        // Vérifier la capacité du créneau
        String sqlCapacite = "SELECT nb_benevole FROM Creneau WHERE id_creneau = ?";
        String sqlActuel = "SELECT COUNT(*) as nb FROM Affectation_Creneau_Activite WHERE id_creneau = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmtCap = conn.prepareStatement(sqlCapacite);
                PreparedStatement pstmtActuel = conn.prepareStatement(sqlActuel)) {

            pstmtCap.setInt(1, idCreneau);
            pstmtActuel.setInt(1, idCreneau);

            int capaciteMax = 0;
            int actuel = 0;

            try (ResultSet rs = pstmtCap.executeQuery()) {
                if (rs.next()) {
                    capaciteMax = rs.getInt("nb_benevole");
                }
            }

            try (ResultSet rs = pstmtActuel.executeQuery()) {
                if (rs.next()) {
                    actuel = rs.getInt("nb");
                }
            }

            if (actuel >= capaciteMax) {
                throw new CapaciteInsuffisanteException("Créneau #" + idCreneau, actuel, capaciteMax);
            }

        } catch (SQLException e) {
            System.err.println("Erreur vérification capacité : " + e.getMessage());
        }

        String sql = "INSERT INTO Affectation_Creneau_Activite (id_creneau, id_personne, id_activite) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCreneau);
            pstmt.setInt(2, idPersonne);
            pstmt.setInt(3, idActivite);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : Bénévole #" + idPersonne + " assigné au créneau #" + idCreneau);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur d'assignation (Déjà inscrit ?) : " + e.getMessage());
        }
        return false;
    }

    /**
     * Désinscrit un bénévole d'un créneau.
     */
    public boolean retirer(int idCreneau, int idPersonne) {
        String sql = "DELETE FROM Affectation_Creneau_Activite WHERE id_creneau = ? AND id_personne = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCreneau);
            pstmt.setInt(2, idPersonne);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : Bénévole désinscrit du créneau.");
                return true;
            } else {
                System.out.println("Erreur : Aucune inscription trouvée pour ces ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la désinscription : " + e.getMessage());
        }
        return false;
    }

    /**
     * Affiche toutes les affectations (Qui fait quoi et quand).
     */
    public void listerAffectations() {
        String sql = """
                    SELECT c.id_creneau, c.heure_d, c.heure_f, p.nom, p.prenom, a.type_act
                    FROM Affectation_Creneau_Activite aff
                    JOIN Creneau c ON aff.id_creneau = c.id_creneau
                    JOIN Personnel p ON aff.id_personne = p.id_pers
                    JOIN Activite a ON aff.id_activite = a.id_activite
                    ORDER BY c.id_creneau, p.nom
                """;

        System.out.println("--- Planning des Affectations ---");
        System.out.printf("%-5s | %-15s | %-20s | %-15s%n", "ID Cr", "Heures", "Bénévole", "Activité");
        System.out.println("------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String heures = rs.getTime("heure_d") + "-" + rs.getTime("heure_f");
                String benevole = rs.getString("nom") + " " + rs.getString("prenom");

                System.out.printf("%-5d | %-15s | %-20s | %-15s%n",
                        rs.getInt("id_creneau"),
                        heures,
                        benevole,
                        rs.getString("type_act"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur affichage planning : " + e.getMessage());
        }
    }

    /**
     * NOUVEAU : Affiche le planning personnel d'un bénévole spécifique.
     */
    public void afficherPlanningPersonne(int idPersonne) {
        String sql = """
                    SELECT c.id_creneau, c.heure_d, c.heure_f, a.type_act
                    FROM Affectation_Creneau_Activite aff
                    JOIN Creneau c ON aff.id_creneau = c.id_creneau
                    JOIN Activite a ON aff.id_activite = a.id_activite
                    WHERE aff.id_personne = ?
                    ORDER BY c.heure_d ASC
                """;

        System.out.println("--- Planning Personnel ---");
        System.out.printf("%-5s | %-15s | %-20s%n", "ID Cr", "Heures", "Activité");
        System.out.println("---------------------------------------------");

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonne);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    String heures = rs.getTime("heure_d") + " - " + rs.getTime("heure_f");

                    System.out.printf("%-5d | %-15s | %-20s%n",
                            rs.getInt("id_creneau"),
                            heures,
                            rs.getString("type_act"));
                }
                if (empty) {
                    System.out.println("Aucun créneau prévu pour ce bénévole.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lecture planning perso : " + e.getMessage());
        }
    }
}