package projet.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Famille;

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
     */
    public List<Famille> getAll() {
        List<Famille> liste = new ArrayList<>();
        String sql = "SELECT * FROM Famille ORDER BY nom ASC";

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
     * Démarre un séjour en famille pour un animal (Adoption ou Accueil).
     * Table : Sejour_Famille
     */
    public boolean demarrerSejour(int idAnimal, int idFamille) {
        String sql = "INSERT INTO Sejour_Famille (id_animal, id_famille, DATE_D) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idFamille);
            pstmt.setDate(3, new Date(System.currentTimeMillis())); // Date du jour

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succès : Animal #" + idAnimal + " placé dans la famille #" + idFamille);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur placement famille (Animal déjà placé ?) : " + e.getMessage());
        }
        return false;
    }

    /**
     * Termine un séjour en famille (met à jour la date de fin).
     */
    public void terminerSejour(int idAnimal, int idFamille) {
        String sql = "UPDATE Sejour_Famille SET DATE_F_FAMILLE = ? WHERE id_animal = ? AND id_famille = ? AND DATE_F_FAMILLE IS NULL";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);
            pstmt.setInt(3, idFamille);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Séjour terminé pour l'animal #" + idAnimal);
            } else {
                System.out.println("Erreur : Aucun séjour en cours trouvé pour ce couple Animal/Famille.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur fin de séjour : " + e.getMessage());
        }
    }
}