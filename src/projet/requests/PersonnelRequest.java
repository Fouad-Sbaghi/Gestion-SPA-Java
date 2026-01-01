package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;
import projet.tables.Personnel;

public class PersonnelRequest {

    public Personnel getByUsername(String username) {
        // "user" est entre guillemets car c'est un mot réservé en PostgreSQL
        String sql = "SELECT * FROM Personnel WHERE \"user\" = ?";
        Personnel personnel = null;

        // On utilise try-with-resources pour fermer la connexion automatiquement après la requête
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
        String sql = "INSERT INTO Personnel (nom, prenom, tel, type_pers, login, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setString(3, p.getTel());
            pstmt.setString(4, p.getType_pers());
            pstmt.setString(5, p.getUser());      // Identifiant de connexion
            pstmt.setString(6, p.getPassword());  // Mot de passe

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : " + p.getType_pers() + " " + p.getPrenom() + " ajouté.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout personnel : " + e.getMessage());
        }
    }
}	