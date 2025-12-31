package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import projet.connexion.Connexion;

public class RapportAnimauxAdoptable extends RapportRequest {

    /**
     * Affiche la liste des animaux prêts à l'adoption.
     */
    public void genererListe() {
        String sql = """
            SELECT id_animal, nom, espece, date_naissance, puce 
            FROM Animal 
            WHERE statut = 'Adoptable' 
            ORDER BY date_arrivee ASC
        """;

        printHeader("=== RAPPORT : ANIMAUX PRÊTS À L'ADOPTION ===");
        System.out.printf("%-5s | %-15s | %-10s | %-12s | %-15s%n", "ID", "Nom", "Espèce", "Né(e) le", "Puce");
        System.out.println("-------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean empty = true;
            while (rs.next()) {
                empty = false;
                int id = rs.getInt("id_animal");
                String nom = rs.getString("nom");
                String esp = rs.getString("espece");
                String naiss = (rs.getDate("date_naissance") != null) ? rs.getDate("date_naissance").toString() : "?";
                String puce = (rs.getString("puce") != null) ? rs.getString("puce") : "-";

                System.out.printf("%-5d | %-15s | %-10s | %-12s | %-15s%n", id, nom, esp, naiss, puce);
            }

            if (empty) {
                System.out.println("Aucun animal adoptable pour le moment.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Rapport Adoption) : " + e.getMessage());
        }
    }
}