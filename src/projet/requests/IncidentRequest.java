package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import projet.connexion.Connexion;

public class IncidentRequest {

    /**
     * Cree un nouvel incident ET le lie immediatement a un animal.
     * Cette methode effectue deux insertions (Table Incident + Table
     * Animal_Incident).
     * 
     * @param idAnimal    L'ID de l'animal concerne.
     * @param type        Le type (Maladie, Accident, etc.).
     * @param description L'intitule ou la description courte.
     * @param commentaire Details supplementaires (peut etre vide).
     */
    public void add(int idAnimal, String type, String description, String commentaire) {
        // 1. Requete pour creer l'incident
        String sqlIncident = "INSERT INTO Incident (type_incident, intitule, commentaire, date_incident) VALUES (?, ?, ?, ?)";

        // 2. Requete pour faire le lien avec l'animal
        String sqlLien = "INSERT INTO Animal_Incident (id_animal, id_incident) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstmtInc = null;
        PreparedStatement pstmtLien = null;
        ResultSet rs = null;

        try {
            conn = Connexion.connectR();

            // --- ETAPE 1 : Insertion de l'incident ---
            pstmtInc = conn.prepareStatement(sqlIncident, Statement.RETURN_GENERATED_KEYS);
            pstmtInc.setString(1, type);
            pstmtInc.setString(2, description);
            pstmtInc.setString(3, (commentaire != null && !commentaire.isEmpty()) ? commentaire : null);
            pstmtInc.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int rows = pstmtInc.executeUpdate();

            if (rows > 0) {
                rs = pstmtInc.getGeneratedKeys();
                if (rs.next()) {
                    int idIncidentGenere = rs.getInt(1);

                    // --- ETAPE 2 : Creation du lien avec l'animal ---
                    pstmtLien = conn.prepareStatement(sqlLien);
                    pstmtLien.setInt(1, idAnimal);
                    pstmtLien.setInt(2, idIncidentGenere);

                    pstmtLien.executeUpdate();

                    System.out.println("Succes : Incident declare pour l'animal #" + idAnimal + " (Ref Incident: "
                            + idIncidentGenere + ")");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur declaration incident : " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmtInc != null)
                    pstmtInc.close();
                if (pstmtLien != null)
                    pstmtLien.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Supprime un incident (et le lien associé grâce au CASCADE défini dans la BDD
     * ou manuellement).
     */
    public boolean delete(int idIncident) {
        String sql = "DELETE FROM Incident WHERE id_incident = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idIncident);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression incident : " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche des incidents par intitulé/type/commentaire et affiche les
     * résultats.
     * Recherche insensible à la casse (ILIKE).
     */
    public void afficherRecherche(String query) {
        String sql = """
                    SELECT i.id_incident,
                           i.date_incident,
                           i.type_incident,
                           i.intitule,
                           COALESCE(i.commentaire, '') AS commentaire,
                           CASE
                               WHEN i.type_incident ILIKE ? THEN 'Type'
                               WHEN i.intitule ILIKE ? THEN 'Intitulé'
                               WHEN i.commentaire ILIKE ? THEN 'Commentaire'
                               ELSE '-'
                           END AS match_field,
                           COALESCE(string_agg(a.nom || ' (#' || a.id_animal || ')', ', ' ORDER BY a.id_animal), '-') AS animaux
                    FROM Incident i
                    LEFT JOIN Animal_Incident ai ON ai.id_incident = i.id_incident
                    LEFT JOIN Animal a ON a.id_animal = ai.id_animal
                    WHERE i.intitule ILIKE ?
                       OR i.commentaire ILIKE ?
                       OR i.type_incident ILIKE ?
                    GROUP BY i.id_incident, i.date_incident, i.type_incident, i.intitule, i.commentaire
                    ORDER BY i.date_incident DESC, i.id_incident DESC
                """;

        System.out.println("--- Recherche incident : '" + query + "' ---");
        System.out.printf("%-5s | %-10s | %-12s | %-35s | %-11s | %-22s | %s%n",
                "ID", "Date", "Type", "Intitulé", "Match", "Commentaire", "Animaux");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------------");

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String like = "%" + query + "%";

            // match_field
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);

            // WHERE
            pstmt.setString(4, like);
            pstmt.setString(5, like);
            pstmt.setString(6, like);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    int id = rs.getInt("id_incident");
                    String dateStr = rs.getString("date_incident");
                    if (dateStr != null && dateStr.length() > 10)
                        dateStr = dateStr.substring(0, 10);

                    String type = rs.getString("type_incident");
                    String intitule = rs.getString("intitule");
                    String matchField = rs.getString("match_field");
                    String commentaire = rs.getString("commentaire");
                    String animaux = rs.getString("animaux");

                    System.out.printf("%-5d | %-10s | %-12s | %-35s | %-11s | %-22s | %s%n",
                            id,
                            dateStr,
                            truncate(type, 12),
                            truncate(intitule, 35),
                            matchField,
                            truncate(commentaire, 22),
                            animaux);
                }
                if (!found) {
                    System.out.println("Aucun incident trouvé.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche incident : " + e.getMessage());
        }
    }

    /**
     * Affiche le détail d'un incident par ID (avec les animaux liés).
     */
    public void afficherInfo(int idIncident) {
        String sql = """
                    SELECT i.id_incident,
                           i.date_incident,
                           i.type_incident,
                           i.intitule,
                           COALESCE(i.commentaire, '') AS commentaire,
                           COALESCE(string_agg(a.nom || ' (#' || a.id_animal || ')', ', ' ORDER BY a.id_animal), '-') AS animaux
                    FROM Incident i
                    LEFT JOIN Animal_Incident ai ON ai.id_incident = i.id_incident
                    LEFT JOIN Animal a ON a.id_animal = ai.id_animal
                    WHERE i.id_incident = ?
                    GROUP BY i.id_incident, i.date_incident, i.type_incident, i.intitule, i.commentaire
                """;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idIncident);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Incident introuvable : #" + idIncident);
                    return;
                }

                String dateStr = rs.getString("date_incident");
                String type = rs.getString("type_incident");
                String intitule = rs.getString("intitule");
                String commentaire = rs.getString("commentaire");
                String animaux = rs.getString("animaux");

                System.out.println("=== INCIDENT #" + idIncident + " ===");
                System.out.println("Date      : " + dateStr);
                System.out.println("Type      : " + type);
                System.out.println("Intitulé  : " + intitule);
                System.out.println("Animaux   : " + animaux);
                System.out.println("Comment.  : " + (commentaire == null || commentaire.isBlank() ? "-" : commentaire));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lecture incident : " + e.getMessage());
        }
    }

    private String truncate(String s, int max) {
        if (s == null)
            return "";
        if (s.length() <= max)
            return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }

}