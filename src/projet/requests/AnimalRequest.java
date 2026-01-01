package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projet.connexion.Connexion;
import projet.exceptions.DuplicatedIdException;
import projet.exceptions.MissingEntityException;
import projet.tables.Animal;

public class AnimalRequest {

    /**
     * Récupère la liste de tous les animaux enregistrés.
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
                a.setId_animal(rs.getInt("id_animal"));
                a.setPuce(rs.getString("puce"));
                a.setEspece(rs.getString("espece"));
                a.setNom(rs.getString("nom"));
                a.setDate_naissance(rs.getDate("date_naissance"));
                a.setStatut(rs.getString("statut"));
                a.setDate_arrivee(rs.getDate("date_arrivee"));
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
     * <p>Vérifie d'abord si le numéro de puce est unique.</p>
     * * @param animal L'objet Animal à insérer.
     * @throws DuplicatedIdException Si un animal avec le même numéro de puce existe déjà.
     */
    public void add(Animal animal) throws DuplicatedIdException {
        // 1. Vérification du doublon de puce
        if (puceExiste(animal.getPuce())) {
            throw new DuplicatedIdException(animal.getPuce());
        }

        // 2. Insertion normale
        String sql = "INSERT INTO Animal (puce, espece, nom, date_naissance, statut, date_arrivee, " +
                     "tests_humain, tests_bebe, tests_chien, tests_chat) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getPuce());
            pstmt.setString(2, animal.getEspece());
            pstmt.setString(3, animal.getNom());
            pstmt.setDate(4, animal.getDate_naissance());
            pstmt.setString(5, animal.getStatut());
            pstmt.setDate(6, animal.getDate_arrivee());
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
     * * @param id L'identifiant de l'animal.
     * @throws MissingEntityException Si l'ID fourni ne correspond à aucun animal en base.
     */
    public void delete(int id) throws MissingEntityException {
        String sql = "DELETE FROM Animal WHERE id_animal = ?";

        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                // Si aucune ligne n'a été effacée, c'est que l'ID n'existe pas
                throw new MissingEntityException("Animal", id);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Suppression Animal) : " + e.getMessage());
        }
    }
    
    // --- Méthode utilitaire privée ---
    
    private boolean puceExiste(String puce) {
        String sql = "SELECT COUNT(*) FROM Animal WHERE puce = ?";
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, puce);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}