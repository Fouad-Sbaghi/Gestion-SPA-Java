package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Creneau;

/**
 * DAO pour la gestion des créneaux horaires.
 * <p>
 * Gère les plages horaires de travail et les affectations bénévoles.
 * </p>
 * 
 * @see projet.tables.Creneau
 */
public class CreneauRequest {

    /**
     * Récupère tous les créneaux disponibles.
     */
    public List<Creneau> getAll() {
        List<Creneau> liste = new ArrayList<>();
        String sql = "SELECT * FROM Creneau ORDER BY heure_d ASC";

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Creneau c = new Creneau();
                c.setId_creneau(rs.getInt("id_creneau"));
                c.setNb_benevole(rs.getInt("nb_benevole"));
                c.setHeure_d(rs.getTime("heure_d"));
                c.setHeure_f(rs.getTime("heure_f"));
                liste.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Liste Créneaux) : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Ajoute un nouveau créneau horaire.
     * 
     * @param nbBenevole Nombre max de bénévoles requis.
     * @param debut      Heure de début (ex: "08:00:00").
     * @param fin        Heure de fin (ex: "12:00:00").
     * @throws HorairesInvalidesException si l'heure de fin est avant ou égale à
     *                                    l'heure de début.
     */
    public void add(int nbBenevole, String debut, String fin)
            throws projet.exceptions.regle.HorairesInvalidesException {
        String sql = "INSERT INTO Creneau (nb_benevole, heure_d, heure_f) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Time heureDebut = Time.valueOf(debut);
            Time heureFin = Time.valueOf(fin);

            // Vérifier que l'heure de fin est après l'heure de début
            if (heureFin.compareTo(heureDebut) <= 0) {
                throw new projet.exceptions.regle.HorairesInvalidesException(heureDebut, heureFin);
            }

            pstmt.setInt(1, nbBenevole);
            pstmt.setTime(2, heureDebut);
            pstmt.setTime(3, heureFin);

            pstmt.executeUpdate();
            System.out.println("Créneau ajouté : " + debut + " - " + fin);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur format heure (utilisez HH:MM:SS) : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur SQL (Ajout Créneau) : " + e.getMessage());
        }
    }

    /**
     * Supprime un créneau par son ID.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM Creneau WHERE id_creneau = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression (Créneau utilisé ?) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère un créneau par son ID.
     */
    public Creneau getById(int id) {
        String sql = "SELECT * FROM Creneau WHERE id_creneau = ?";
        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Creneau c = new Creneau();
                c.setId_creneau(rs.getInt("id_creneau"));
                c.setNb_benevole(rs.getInt("nb_benevole"));
                c.setHeure_d(rs.getTime("heure_d"));
                c.setHeure_f(rs.getTime("heure_f"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}