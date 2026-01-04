package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Famille;

/**
 * DAO pour la table Famille. CRUD et recherche.
 */
public class FamilleRequest {

    /**
     * Ajoute une nouvelle famille dans la base.
     */
    public void add(Famille f) {
        String sql = "INSERT INTO Famille (type_famille, nom, adresse, contact) VALUES (?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, f.getType_famille()); // ex: "Accueil", "Adoption"
            pstmt.setString(2, f.getNom());
            pstmt.setString(3, f.getAdresse());
            pstmt.setString(4, f.getContact());

            pstmt.executeUpdate();
            System.out.println("Famille ajoutée : " + f.getNom());

        } catch (SQLException e) {
            System.err.println("Erreur ajout famille : " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les familles enregistrées.
     * MODIF ICI : Tri par ID croissant.
     */
    public List<Famille> getAll() {
        List<Famille> liste = new ArrayList<>();
        // AVANT : ORDER BY nom ASC
        // APRES : ORDER BY id_famille ASC
        String sql = "SELECT * FROM Famille ORDER BY id_famille ASC";

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Famille f = new Famille();
                f.setId_famille(rs.getInt("id_famille"));
                f.setType_famille(rs.getString("type_famille"));
                f.setNom(rs.getString("nom"));
                f.setAdresse(rs.getString("adresse"));
                f.setContact(rs.getString("contact"));
                liste.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erreur liste familles : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Récupère une famille par son ID.
     */
    public Famille getById(int id) {
        String sql = "SELECT * FROM Famille WHERE id_famille = ?";
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Famille f = new Famille();
                f.setId_famille(rs.getInt("id_famille"));
                f.setType_famille(rs.getString("type_famille"));
                f.setNom(rs.getString("nom"));
                f.setAdresse(rs.getString("adresse"));
                f.setContact(rs.getString("contact"));
                return f;
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération ID : " + e.getMessage());
        }
        return null; // Pas trouvé
    }

    /**
     * Recherche des familles par nom (ou partie du nom).
     */
    public List<Famille> getByName(String nom) {
        List<Famille> liste = new ArrayList<>();
        String sql = "SELECT * FROM Famille WHERE nom ILIKE ? ORDER BY nom ASC";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Famille f = new Famille();
                    f.setId_famille(rs.getInt("id_famille"));
                    f.setType_famille(rs.getString("type_famille"));
                    f.setNom(rs.getString("nom"));
                    f.setAdresse(rs.getString("adresse"));
                    f.setContact(rs.getString("contact"));
                    liste.add(f);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche famille : " + e.getMessage());
        }
        return liste;
    }
}