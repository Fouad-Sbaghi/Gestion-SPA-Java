package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;
import projet.gestion.Rapport;

/**
 * Rapport statistique sur les bénévoles.

 * Affiche le nombre de missions par bénévole.

 */
public class RapportBenevoleRequest extends RapportRequest {

    private static final String SQL = """
                SELECT p.id_pers, p.nom, p.prenom, p.tel, COUNT(a.id_creneau) as missions
                FROM Personnel p
                LEFT JOIN Affectation_Creneau_Activite a ON p.id_pers = a.id_personne
                WHERE (p.type_pers ILIKE 'Benevole' OR p.type_pers ILIKE 'Bénévole')
                GROUP BY p.id_pers, p.nom, p.prenom, p.tel
                ORDER BY missions DESC, p.nom ASC
            """;

    /**
     * Affiche les bénévoles et leur nombre de missions.
     */
    public void afficherStatistiques() {
        printHeader("=== STATISTIQUES DES BÉNÉVOLES ===");
        System.out.printf("%-5s | %-15s | %-15s | %-12s | %s%n", "ID", "Nom", "Prénom", "Téléphone", "Missions");
        System.out.println("-------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            int total = 0;
            while (rs.next()) {
                total++;
                System.out.printf("%-5d | %-15s | %-15s | %-12s | %d%n",
                        rs.getInt("id_pers"), rs.getString("nom"),
                        rs.getString("prenom"), rs.getString("tel"),
                        rs.getInt("missions"));
            }

            if (total == 0) {
                System.out.println("Aucun bénévole enregistré.");
            } else {
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("Total : " + total + " bénévoles actifs.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Stats Bénévoles) : " + e.getMessage());
        }
    }

    /**
     * Génère un rapport sérialisable des bénévoles.
     * 
     * @return Un objet Rapport contenant les données.
     */
    public Rapport genererRapport() {
        Rapport rapport = new Rapport("benevoles", "Statistiques des Bénévoles");

        rapport.ajouterLigne(String.format("%-5s | %-15s | %-15s | %-12s | %s",
                "ID", "Nom", "Prénom", "Téléphone", "Missions"));
        rapport.ajouterLigne("-------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            int total = 0;
            while (rs.next()) {
                total++;
                rapport.ajouterLigne(String.format("%-5d | %-15s | %-15s | %-12s | %d",
                        rs.getInt("id_pers"), rs.getString("nom"),
                        rs.getString("prenom"), rs.getString("tel"),
                        rs.getInt("missions")));
            }
            rapport.ajouterLigne("-------------------------------------------------------------------------");
            rapport.ajouterLigne("Total : " + total + " bénévoles.");

        } catch (SQLException e) {
            rapport.ajouterLigne("Erreur SQL : " + e.getMessage());
        }

        return rapport;
    }
}