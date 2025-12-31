package projet.requests.rapports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;

public class RapportPlanningRequest {

    /**
     * Identifie les créneaux qui n'ont pas assez de bénévoles.
     * @return Une liste de messages d'alerte formatés pour l'affichage.
     */
    public List<String> getCreneauxManquants() {
        List<String> alertes = new ArrayList<>();

        // Requête : On compte les personnes affectées par créneau
        // et on ne garde que ceux où (compteur < nb_benevole requis)
        String sql = """
            SELECT c.id_creneau, c.heure_d, c.heure_f, c.nb_benevole, 
                   COUNT(a.id_personne) as presents
            FROM Creneau c
            LEFT JOIN Affectation_Creneau_Activite a ON c.id_creneau = a.id_creneau
            GROUP BY c.id_creneau, c.heure_d, c.heure_f, c.nb_benevole
            HAVING COUNT(a.id_personne) < c.nb_benevole
            ORDER BY c.heure_d ASC
        """;

        try (Connection conn = Connexion.connectR();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_creneau");
                String debut = rs.getString("heure_d");
                String fin = rs.getString("heure_f");
                int requis = rs.getInt("nb_benevole");
                int presents = rs.getInt("presents");

                // Création du message d'alerte
                String message = String.format("Créneau #%d [%s - %s] : %d/%d bénévoles (Manque %d)",
                        id, debut, fin, presents, requis, (requis - presents));
                
                alertes.add(message);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Rapport Planning) : " + e.getMessage());
        }

        return alertes;
    }
}