package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;

/**
 * Rapport statistique sur les bénévoles.
 * <p>
 * Affiche le nombre de missions par bénévole.
 * </p>
 */
public class RapportBenevoleRequest extends RapportRequest {

    /**
     * Affiche les bénévoles et leur nombre de missions (créneaux affectés).
     */
    public void afficherStatistiques() {
        // Cette requête compte le nombre de fois qu'un bénévole apparaît dans la table
        // d'affectation
        String sql = """
                    SELECT p.id_pers, p.nom, p.prenom, p.tel, COUNT(a.id_creneau) as missions
                    FROM Personnel p
                    LEFT JOIN Affectation_Creneau_Activite a ON p.id_pers = a.id_personne
                    WHERE (p.type_pers ILIKE 'Benevole' OR p.type_pers ILIKE 'Bénévole')
                    GROUP BY p.id_pers, p.nom, p.prenom, p.tel
                    ORDER BY missions DESC, p.nom ASC
                """;

        printHeader("=== STATISTIQUES DES BÉNÉVOLES ===");
        System.out.printf("%-5s | %-15s | %-15s | %-12s | %s%n", "ID", "Nom", "Prénom", "Téléphone", "Missions");
        System.out.println("-------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            int totalBenevoles = 0;

            while (rs.next()) {
                totalBenevoles++;
                int id = rs.getInt("id_pers");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String tel = rs.getString("tel");
                int missions = rs.getInt("missions");

                System.out.printf("%-5d | %-15s | %-15s | %-12s | %d%n", id, nom, prenom, tel, missions);
            }

            if (totalBenevoles == 0) {
                System.out.println("Aucun bénévole enregistré.");
            } else {
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("Total : " + totalBenevoles + " bénévoles actifs.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Stats Bénévoles) : " + e.getMessage());
        }
    }
}