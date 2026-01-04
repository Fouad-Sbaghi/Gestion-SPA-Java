package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.tables.Animal;

/**
 * DAO (Data Access Object) pour la gestion des animaux.
 * <p>
 * Fournit les opérations CRUD et les requêtes de recherche
 * pour la table Animal de la base de données.
 * </p>
 * 
 * @see projet.tables.Animal
 */
public class AnimalRequest {

    /**
     * Récupère la liste de tous les animaux enregistrés.
     * 
     * @return Une liste d'objets Animal.
     */
    public List<Animal> getAll() {
        List<Animal> liste = new ArrayList<>();
        String sql = "SELECT * FROM Animal ORDER BY id_animal ASC";

        try (Connection conn = Connexion.connectR();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Animal a = new Animal();
                // Mapping des colonnes BDD vers l'objet Java
                a.setId_animal(rs.getInt("id_animal"));
                a.setPuce(rs.getString("puce"));
                a.setEspece(rs.getString("espece"));
                a.setNom(rs.getString("nom"));
                a.setDate_naissance(rs.getDate("date_naissance"));
                a.setStatut(rs.getString("statut"));
                a.setDate_arrivee(rs.getDate("date_arrivee"));

                // Récupération des booléens (tests comportementaux)
                a.setTests_humain(rs.getBoolean("tests_humain"));
                a.setTests_bebe(rs.getBoolean("tests_bebe"));
                a.setTests_chien(rs.getBoolean("tests_chien"));
                a.setTests_chat(rs.getBoolean("tests_chat"));

                liste.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL (Lecture Animaux) : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Ajoute un nouvel animal dans la base de données.
     * 
     * @param animal L'objet Animal à insérer.
     */
    public void add(Animal animal) {
        String sql = "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, date_arrivee, " +
                "tests_humain, tests_bebe, tests_chien, tests_chat) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getPuce());
            pstmt.setString(2, animal.getEspece());
            pstmt.setString(3, animal.getNom());
            pstmt.setDate(4, animal.getDate_naissance()); // Peut être null, JDBC gère ça
            pstmt.setString(5, animal.getStatut());
            pstmt.setDate(6, animal.getDate_arrivee());

            // Valeurs par défaut false si non renseignées
            pstmt.setBoolean(7, animal.isTests_humain());
            pstmt.setBoolean(8, animal.isTests_bebe());
            pstmt.setBoolean(9, animal.isTests_chien());
            pstmt.setBoolean(10, animal.isTests_chat());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Ajout Animal) : " + e.getMessage());
        }
    }

    /**
     * Supprime un animal via son ID.
     * 
     * @param id L'identifiant de l'animal.
     * @return true si suppression réussie, false sinon.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM Animal WHERE id_animal = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Suppression Animal) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche des animaux par nom exact (insensible à la casse).
     */
    public List<Animal> getByName(String nom) {
        List<Animal> liste = new ArrayList<>();
        String sql = "SELECT * FROM Animal WHERE nom ILIKE ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Recherche exacte (insensible à la casse)
            pstmt.setString(1, nom);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Animal a = new Animal();
                    a.setId_animal(rs.getInt("id_animal"));
                    a.setNom(rs.getString("nom"));
                    a.setEspece(rs.getString("espece"));
                    a.setPuce(rs.getString("puce"));
                    a.setDate_naissance(rs.getDate("date_naissance"));
                    a.setDate_arrivee(rs.getDate("date_arrivee"));
                    a.setStatut(rs.getString("statut"));

                    // Récupération des tests (booléens)
                    a.setTests_humain(rs.getBoolean("tests_humain"));
                    a.setTests_bebe(rs.getBoolean("tests_bebe"));
                    a.setTests_chien(rs.getBoolean("tests_chien"));
                    a.setTests_chat(rs.getBoolean("tests_chat"));

                    liste.add(a);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur recherche par nom : " + e.getMessage());
        }
        return liste;
    }

    /**
     * Récupère un animal spécifique par son ID.
     * 
     * @param id L'identifiant de l'animal.
     * @return L'objet Animal s'il existe, sinon null.
     */
    public Animal getById(int id) {
        String sql = "SELECT * FROM Animal WHERE id_animal = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Animal a = new Animal();

                    // Informations de base
                    a.setId_animal(rs.getInt("id_animal"));
                    a.setNom(rs.getString("nom"));
                    a.setEspece(rs.getString("espece"));
                    a.setPuce(rs.getString("puce"));
                    a.setDate_naissance(rs.getDate("date_naissance"));
                    a.setDate_arrivee(rs.getDate("date_arrivee"));
                    a.setStatut(rs.getString("statut"));

                    // Tests (Booleens)
                    a.setTests_humain(rs.getBoolean("tests_humain"));
                    a.setTests_bebe(rs.getBoolean("tests_bebe"));
                    a.setTests_chien(rs.getBoolean("tests_chien"));
                    a.setTests_chat(rs.getBoolean("tests_chat"));

                    return a;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération animal par ID : " + e.getMessage());
        }
        return null; // Retourne null si pas trouvé
    }

    /**
     * Met à jour les informations d'un animal existant.
     * 
     * @param a L'objet Animal contenant les nouvelles données.
     */
    public boolean update(Animal a) {
        String sql = "UPDATE Animal SET nom=?, espece=?, puce=?, date_naissance=?, date_arrivee=?, statut=?, " +
                "tests_humain=?, tests_bebe=?, tests_chien=?, tests_chat=? WHERE id_animal=?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, a.getNom());
            pstmt.setString(2, a.getEspece());
            pstmt.setString(3, a.getPuce());
            pstmt.setDate(4, a.getDate_naissance());
            pstmt.setDate(5, a.getDate_arrivee());
            pstmt.setString(6, a.getStatut());

            // Mises à jour des tests (booléens)
            pstmt.setBoolean(7, a.isTests_humain());
            pstmt.setBoolean(8, a.isTests_bebe());
            pstmt.setBoolean(9, a.isTests_chien());
            pstmt.setBoolean(10, a.isTests_chat());

            // Clause WHERE
            pstmt.setInt(11, a.getId_animal());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur mise à jour animal : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère la liste des animaux selon leur statut.
     * 
     * @param statut Le statut recherché (ex: "Adoptable", "Soin").
     */
    public List<Animal> getByStatut(String statut) {
        List<Animal> liste = new ArrayList<>();
        String sql = "SELECT * FROM Animal WHERE statut = ?"; // Ou LIKE ? pour plus de souplesse

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Animal a = new Animal();
                    a.setId_animal(rs.getInt("id_animal"));
                    a.setNom(rs.getString("nom"));
                    a.setEspece(rs.getString("espece"));
                    a.setPuce(rs.getString("puce"));
                    a.setDate_naissance(rs.getDate("date_naissance"));
                    a.setDate_arrivee(rs.getDate("date_arrivee"));
                    a.setStatut(rs.getString("statut"));

                    // Tests
                    a.setTests_humain(rs.getBoolean("tests_humain"));
                    a.setTests_bebe(rs.getBoolean("tests_bebe"));
                    a.setTests_chien(rs.getBoolean("tests_chien"));
                    a.setTests_chat(rs.getBoolean("tests_chat"));

                    liste.add(a);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur filtre statut : " + e.getMessage());
        }
        return liste;
    }
}