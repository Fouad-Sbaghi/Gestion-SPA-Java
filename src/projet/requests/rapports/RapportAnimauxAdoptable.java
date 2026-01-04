package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;
import projet.gestion.Rapport;

/**
 * Rapport listant les animaux prêts à l'adoption.
 * <p>
 * Affiche les animaux avec le statut "Adoptable".
 * </p>
 */
public class RapportAnimauxAdoptable extends RapportRequest {

    private static final String SQL = """
                SELECT id_animal, nom, espece, date_naissance, puce
                FROM Animal
                WHERE statut ILIKE 'Adoptable'
                ORDER BY date_arrivee ASC
            """;

    /**
     * Affiche la liste des animaux prêts à l'adoption.
     */
    public void genererListe() {
        printHeader("=== RAPPORT : ANIMAUX PRÊTS À L'ADOPTION ===");
        System.out.printf("%-5s | %-15s | %-10s | %-12s | %-15s%n", "ID", "Nom", "Espèce", "Né(e) le", "Puce");
        System.out.println("-------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            boolean empty = true;
            while (rs.next()) {
                empty = false;
                String naiss = (rs.getDate("date_naissance") != null) ? rs.getDate("date_naissance").toString() : "?";
                String puce = (rs.getString("puce") != null) ? rs.getString("puce") : "-";

                System.out.printf("%-5d | %-15s | %-10s | %-12s | %-15s%n",
                        rs.getInt("id_animal"), rs.getString("nom"),
                        rs.getString("espece"), naiss, puce);
            }

            if (empty) {
                System.out.println("Aucun animal adoptable pour le moment.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Rapport Adoption) : " + e.getMessage());
        }
    }

    /**
     * Génère un rapport sérialisable des animaux adoptables.
     * 
     * @return Un objet Rapport contenant les données.
     */
    public Rapport genererRapport() {
        Rapport rapport = new Rapport("adoptables", "Animaux Prêts à l'Adoption");

        rapport.ajouterLigne(String.format("%-5s | %-15s | %-10s | %-12s | %-15s",
                "ID", "Nom", "Espèce", "Né(e) le", "Puce"));
        rapport.ajouterLigne("-------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            int count = 0;
            while (rs.next()) {
                count++;
                String naiss = (rs.getDate("date_naissance") != null) ? rs.getDate("date_naissance").toString() : "?";
                String puce = (rs.getString("puce") != null) ? rs.getString("puce") : "-";

                rapport.ajouterLigne(String.format("%-5d | %-15s | %-10s | %-12s | %-15s",
                        rs.getInt("id_animal"), rs.getString("nom"),
                        rs.getString("espece"), naiss, puce));
            }
            rapport.ajouterLigne("-------------------------------------------------------------------");
            rapport.ajouterLigne("Total : " + count + " animaux adoptables.");

        } catch (SQLException e) {
            rapport.ajouterLigne("Erreur SQL : " + e.getMessage());
        }

        return rapport;
    }
}