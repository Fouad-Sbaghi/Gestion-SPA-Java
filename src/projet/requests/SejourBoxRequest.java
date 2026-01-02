package projet.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet.connexion.Connexion;
import projet.exceptions.BoxPleinException;
import projet.exceptions.IncompatibiliteTypeException;
import projet.exceptions.MissingEntityException;

public class SejourBoxRequest {

    /**
     * Place un animal dans un box (Demarre un sejour).
     * Verifie la capacite et la compatibilite de type avant placement.
     * 
     * @param idAnimal L'animal a placer.
     * @param idBox    Le box de destination.
     * @throws BoxPleinException            Si le box est a capacite maximale.
     * @throws IncompatibiliteTypeException Si le type de l'animal ne correspond pas
     *                                      au type du box.
     * @throws MissingEntityException       Si l'animal ou le box n'existe pas.
     */
    public boolean placerAnimal(int idAnimal, int idBox)
            throws BoxPleinException, IncompatibiliteTypeException, MissingEntityException {

        // 1. Verifier que le box existe et recuperer ses infos
        String sqlBox = "SELECT type_box, capacite_max FROM Box WHERE id_box = ?";
        String typeBox = null;
        int capaciteMax = 0;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sqlBox)) {
            pstmt.setInt(1, idBox);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    typeBox = rs.getString("type_box");
                    capaciteMax = rs.getInt("capacite_max");
                } else {
                    throw new MissingEntityException("Box", idBox);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur verification box : " + e.getMessage());
            return false;
        }

        // 2. Verifier que l'animal existe et recuperer son espece
        String sqlAnimal = "SELECT espece FROM Animal WHERE id_animal = ?";
        String especeAnimal = null;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sqlAnimal)) {
            pstmt.setInt(1, idAnimal);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    especeAnimal = rs.getString("espece");
                } else {
                    throw new MissingEntityException("Animal", idAnimal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur verification animal : " + e.getMessage());
            return false;
        }

        // 3. Verifier la compatibilite de type (sauf box speciaux comme Quarantaine,
        // Infirmerie)
        if (typeBox != null && !typeBox.equalsIgnoreCase("Quarantaine")
                && !typeBox.equalsIgnoreCase("Infirmerie") && !typeBox.equalsIgnoreCase("Exotique")) {
            if (!typeBox.equalsIgnoreCase(especeAnimal)) {
                throw new IncompatibiliteTypeException(especeAnimal, typeBox);
            }
        }

        // 4. Verifier la capacite du box
        String sqlOccupation = "SELECT COUNT(*) as nb FROM Sejour_Box WHERE id_box = ? AND DATE_F_BOX IS NULL";
        int occupationActuelle = 0;

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sqlOccupation)) {
            pstmt.setInt(1, idBox);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    occupationActuelle = rs.getInt("nb");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur verification occupation : " + e.getMessage());
            return false;
        }

        if (occupationActuelle >= capaciteMax) {
            throw new BoxPleinException(idBox, capaciteMax);
        }

        // 5. Sortir l'animal de son box actuel si necessaire
        sortirAnimal(idAnimal);

        // 6. Placer l'animal dans le nouveau box
        String sql = "INSERT INTO Sejour_Box (id_animal, id_box, DATE_D) VALUES (?, ?, ?)";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);
            pstmt.setInt(2, idBox);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Succes : Animal #" + idAnimal + " place dans le box #" + idBox);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur placement : " + e.getMessage());
        }
        return false;
    }

    /**
     * "Sort" un animal de son box actuel (Met à jour la date de fin).
     * Utilisé lors d'une adoption, d'un départ en famille d'accueil ou d'un
     * changement de box.
     */
    public void sortirAnimal(int idAnimal) {
        // On cherche le séjour en cours (celui où DATE_F_BOX est NULL)
        String sql = "UPDATE Sejour_Box SET DATE_F_BOX = ? WHERE id_animal = ? AND DATE_F_BOX IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, idAnimal);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Animal #" + idAnimal + " sorti de son box.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur sortie de box : " + e.getMessage());
        }
    }

    /**
     * Récupère l'ID du box où se trouve actuellement l'animal.
     * 
     * @return L'ID du box, ou -1 si l'animal n'est pas en box.
     */
    public int getBoxActuel(int idAnimal) {
        String sql = "SELECT id_box FROM Sejour_Box WHERE id_animal = ? AND DATE_F_BOX IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAnimal);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_box");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Pas trouvé
    }

    /**
     * Termine tous les séjours actifs d'un box (DATE_F_BOX = aujourd'hui).
     * 
     * @param idBox Le box à vider.
     * @return Le nombre de séjours clôturés.
     */
    public int viderBox(int idBox) {
        String sql = "UPDATE Sejour_Box SET DATE_F_BOX = ? WHERE id_box = ? AND DATE_F_BOX IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, idBox);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur vidage box : " + e.getMessage());
            return 0;
        }
    }

    /**
     * Compte le nombre d'animaux actuellement dans le box (séjours actifs).
     */
    public int getNbOccupants(int idBox) {
        String sql = "SELECT COUNT(*) as nb FROM Sejour_Box WHERE id_box = ? AND DATE_F_BOX IS NULL";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBox);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("nb");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur comptage occupants : " + e.getMessage());
        }
        return 0;
    }

    /**
     * Supprime tout l'historique de séjours pour un box donné.
     * ATTENTION : Irréversible.
     */
    public void supprimerHistoriqueBox(int idBox) {
        String sql = "DELETE FROM Sejour_Box WHERE id_box = ?";

        try (Connection conn = Connexion.connectR();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBox);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Historique du box #" + idBox + " supprimé (" + rows + " entrées).");
            }

        } catch (SQLException e) {
            System.err.println("Erreur suppression historique box : " + e.getMessage());
        }
    }
}