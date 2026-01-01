package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Personnel;

public class PersonnelRequest {

    public Personnel getByUsername(String username) {
        // "user" est entre guillemets car c'est un mot réservé en PostgreSQL
        String sql = "SELECT * FROM Personnel WHERE \"user\" = ?";
        Personnel personnel = null;

        // On utilise try-with-resources pour fermer la connexion automatiquement après
        // la requête
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    personnel = new Personnel();
                    personnel.setId_pers(rs.getInt("id_pers"));
                    personnel.setNom(rs.getString("nom"));
                    personnel.setPrenom(rs.getString("prenom"));
                    personnel.setType_pers(rs.getString("type_pers"));
                    personnel.setTel(rs.getString("tel"));
                    personnel.setUser(rs.getString("user"));
                    personnel.setPassword(rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return personnel;
    }

    /**
     * Ajoute un nouveau membre du personnel (Bénévole, Veto, Admin).
     */
    public void add(Personnel p) {
        // Adapte les noms de colonnes (login/user, mdp/password) selon ta BDD
        String sql = "INSERT INTO Personnel (nom, prenom, tel, type_pers, \"user\", password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setString(3, p.getTel());
            pstmt.setString(4, p.getType_pers());
            pstmt.setString(5, p.getUser()); // Identifiant de connexion
            pstmt.setString(6, p.getPassword()); // Mot de passe

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : " + p.getType_pers() + " " + p.getPrenom() + " ajouté.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout personnel : " + e.getMessage());
        }
    }

    /**
     * Récupère un membre du personnel par son ID.
     */
    public Personnel getById(int id) {
        String sql = "SELECT * FROM Personnel WHERE id_pers = ?";
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Personnel personnel = new Personnel();
                    personnel.setId_pers(rs.getInt("id_pers"));
                    personnel.setNom(rs.getString("nom"));
                    personnel.setPrenom(rs.getString("prenom"));
                    personnel.setType_pers(rs.getString("type_pers"));
                    personnel.setTel(rs.getString("tel"));
                    personnel.setUser(rs.getString("user"));
                    personnel.setPassword(rs.getString("password"));
                    return personnel;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return null;
    }

    /**
     * Recherche des bénévoles par nom/prénom/identifiant (recherche partielle,
     * insensible à la casse).
     */
    public List<Personnel> searchBenevoles(String query) {
        List<Personnel> liste = new ArrayList<>();
        String sql = """
                    SELECT *
                    FROM Personnel
                    WHERE (type_pers ILIKE 'Benevole' OR type_pers ILIKE 'Bénévole')
                      AND (nom ILIKE ? OR prenom ILIKE ? OR "user" ILIKE ?)
                    ORDER BY nom ASC, prenom ASC
                """;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String like = "%" + query + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Personnel p = new Personnel();
                    p.setId_pers(rs.getInt("id_pers"));
                    p.setNom(rs.getString("nom"));
                    p.setPrenom(rs.getString("prenom"));
                    p.setType_pers(rs.getString("type_pers"));
                    p.setTel(rs.getString("tel"));
                    p.setUser(rs.getString("user"));
                    p.setPassword(rs.getString("password"));
                    liste.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche bénévoles : " + e.getMessage());
        }

        return liste;
    }

}