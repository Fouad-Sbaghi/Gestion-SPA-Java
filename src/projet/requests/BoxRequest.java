package projet.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;
import projet.exceptions.BoxPleinException;
import projet.exceptions.IncompatibiliteTypeException; // Assurez-vous d'avoir créé cette classe
import projet.exceptions.MetierException;
import projet.exceptions.MissingEntityException;
import projet.tables.Animal;
import projet.tables.Box;

public class BoxRequest {

    /**
     * Tente d'affecter un animal à un box.
     * <p>Effectue 3 vérifications avant l'insertion :
     * <ul>
     * <li>L'animal et le box existent.</li>
     * <li>Le box n'est pas plein.</li>
     * <li>Le type du box correspond à l'espèce de l'animal.</li>
     * </ul>
     * </p>
     * * @param idAnimal L'ID de l'animal à placer.
     * @param idBox L'ID du box de destination.
     * @throws BoxPleinException Si le nombre d'animaux atteint la capacité max.
     * @throws IncompatibiliteTypeException Si on tente de mettre un Chien dans un box Chat.
     * @throws MissingEntityException Si l'animal ou le box n'existe pas.
     * @throws MetierException Pour toute autre règle métier non respectée.
     */
    public void affecterAnimal(int idAnimal, int idBox) throws BoxPleinException, IncompatibiliteTypeException, MissingEntityException, MetierException {
        
        // 1. Récupérer les infos (Simulé ici, idéalement via des méthodes getById)
        Animal animal = getAnimalById(idAnimal);
        Box box = getBoxById(idBox);

        if (animal == null) throw new MissingEntityException("Animal", idAnimal);
        if (box == null) throw new MissingEntityException("Box", idBox);

        // 2. Vérifier la compatibilité (Chat vs Chien)
        if (!animal.getEspece().equalsIgnoreCase(box.getType_box())) {
            throw new IncompatibiliteTypeException(animal.getEspece(), box.getType_box());
        }

        // 3. Vérifier la capacité (Compter les occupants actuels)
        int occupants = getNombreOccupants(idBox);
        if (occupants >= box.getCapacite_max()) {
            throw new BoxPleinException(idBox, box.getCapacite_max());
        }

        // 4. Si tout est OK : Insertion dans SejourBox
        String sql = "INSERT INTO SejourBox (id_animal, id_box, date_d) VALUES (?, ?, CURRENT_DATE)";
        
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idBox);
            pstmt.executeUpdate();

            // Mettre à jour le statut de l'animal
            updateStatutAnimal(idAnimal, "En Box");

        } catch (SQLException e) {
            System.err.println("Erreur SQL (Affectation Box) : " + e.getMessage());
        }
    }

    // --- Méthodes utilitaires privées pour alléger le code ---

    private int getNombreOccupants(int idBox) {
        String sql = "SELECT COUNT(*) FROM SejourBox WHERE id_box = ? AND date_f_box IS NULL";
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBox);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private Animal getAnimalById(int id) {
        // Code simplifié pour récupérer l'objet Animal (Espèce nécessaire)
        // Vous pouvez appeler AnimalRequest.getById(id) si vous l'avez codé
        String sql = "SELECT espece FROM Animal WHERE id_animal = ?";
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Animal a = new Animal();
                a.setEspece(rs.getString("espece"));
                return a;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private Box getBoxById(int id) {
        String sql = "SELECT type_box, capacite_max FROM Box WHERE id_box = ?";
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Box b = new Box();
                b.setType_box(rs.getString("type_box"));
                b.setCapacite_max(rs.getInt("capacite_max"));
                return b;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    private void updateStatutAnimal(int id, String statut) {
        String sql = "UPDATE Animal SET statut = ? WHERE id_animal = ?";
        try (Connection conn = Connexion.connectR();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statut);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // Ajoutez ici votre méthode afficherOccupation() existante...
    public void afficherOccupation() {
        // ... votre code existant pour lister les box ...
    }
}