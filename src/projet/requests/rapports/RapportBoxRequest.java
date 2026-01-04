package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;

/**
 * Rapport statistique sur l'occupation des box.
 * <p>
 * Affiche la capacité vs occupation par type de box.
 * </p>
 */
public class RapportBoxRequest extends RapportRequest {

    /**
     * Affiche un rapport statistique sur l'occupation des box par catégorie.
     * Compare la capacité totale disponible vs le nombre d'animaux présents.
     */
    public void afficherStatistiques() {
        // Cette requête calcule la capacité totale cumulée et le nombre d'animaux
        // présents par type de box
        String sql = """
                    SELECT
                        b.type_box,
                        COUNT(DISTINCT b.id_box) as nb_box,
                        SUM(b.capacite_max) as capacite_totale,
                        COUNT(s.id_animal) as animaux_presents
                    FROM Box b
                    LEFT JOIN Sejour_Box s ON b.id_box = s.id_box AND s.DATE_F_BOX IS NULL
                    GROUP BY b.type_box
                    ORDER BY b.type_box ASC
                """;

        printHeader("=== RAPPORT D'OCCUPATION DES BOX (PAR TYPE) ===");
        System.out.printf("%-20s | %-10s | %-15s | %-15s | %s%n",
                "Type de Box", "Nb Box", "Places Totales", "Animaux Présents", "Taux Remplissage");
        System.out.println("----------------------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            boolean dataFound = false;

            while (rs.next()) {
                dataFound = true;
                String type = rs.getString("type_box");
                int nbBox = rs.getInt("nb_box");
                int capTotale = rs.getInt("capacite_totale");
                int presents = rs.getInt("animaux_presents");

                // Calcul du pourcentage
                double pourcentage = (capTotale > 0) ? ((double) presents / capTotale) * 100 : 0;

                // Formattage couleur visuel (Facultatif, ici textuel)
                String alerte = (pourcentage >= 100) ? " (PLEIN !)" : "";

                System.out.printf("%-20s | %-10d | %-15d | %-15d | %6.2f%%%s%n",
                        type, nbBox, capTotale, presents, pourcentage, alerte);
            }

            if (!dataFound) {
                System.out.println("Aucun box configuré dans le système.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Rapport Box) : " + e.getMessage());
        }
    }
}