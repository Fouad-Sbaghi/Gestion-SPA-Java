package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;
import projet.gestion.Rapport;

/**
 * Rapport statistique sur l'occupation des box.
 * <p>
 * Affiche la capacité vs occupation par type de box.
 * </p>
 */
public class RapportBoxRequest extends RapportRequest {

    private static final String SQL = """
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

    /**
     * Affiche un rapport statistique sur l'occupation des box par catégorie.
     */
    public void afficherStatistiques() {
        printHeader("=== RAPPORT D'OCCUPATION DES BOX (PAR TYPE) ===");
        System.out.printf("%-20s | %-10s | %-15s | %-15s | %s%n",
                "Type de Box", "Nb Box", "Places Totales", "Animaux Présents", "Taux Remplissage");
        System.out.println("----------------------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            boolean dataFound = false;
            while (rs.next()) {
                dataFound = true;
                String type = rs.getString("type_box");
                int nbBox = rs.getInt("nb_box");
                int capTotale = rs.getInt("capacite_totale");
                int presents = rs.getInt("animaux_presents");
                double pourcentage = (capTotale > 0) ? ((double) presents / capTotale) * 100 : 0;
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

    /**
     * Génère un rapport sérialisable sur l'occupation des box.
     * 
     * @return Un objet Rapport contenant les données.
     */
    public Rapport genererRapport() {
        Rapport rapport = new Rapport("box", "Rapport d'Occupation des Box");

        rapport.ajouterLigne(String.format("%-20s | %-10s | %-15s | %-15s | %s",
                "Type de Box", "Nb Box", "Places Totales", "Animaux Présents", "Taux"));
        rapport.ajouterLigne(
                "----------------------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                String type = rs.getString("type_box");
                int nbBox = rs.getInt("nb_box");
                int capTotale = rs.getInt("capacite_totale");
                int presents = rs.getInt("animaux_presents");
                double pourcentage = (capTotale > 0) ? ((double) presents / capTotale) * 100 : 0;

                rapport.ajouterLigne(String.format("%-20s | %-10d | %-15d | %-15d | %6.2f%%",
                        type, nbBox, capTotale, presents, pourcentage));
            }

        } catch (SQLException e) {
            rapport.ajouterLigne("Erreur SQL : " + e.getMessage());
        }

        return rapport;
    }
}