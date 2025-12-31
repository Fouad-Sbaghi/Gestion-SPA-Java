package projet.auth;

import projet.requests.PersonnelRequest;
import projet.tables.Personnel;

public class Login {

    private PersonnelRequest personnelRequest;

    public Login() {
        this.personnelRequest = new PersonnelRequest();
    }

    /**
     * Tente d'authentifier un utilisateur.
     * @param username Le login (colonne 'user' en BDD)
     * @param password Le mot de passe
     * @return L'objet Personnel si succès, null si échec.
     */
    public Personnel authentifier(String username, String password) {
        // 1. Récupère l'utilisateur en BDD
        Personnel user = personnelRequest.getByUsername(username);

        // 2. Vérifie si l'utilisateur existe ET si le mot de passe correspond
        if (user != null && user.getPassword().equals(password)) {
            return user; // Connexion réussie
        }

        return null; // Échec (utilisateur inconnu ou mauvais mot de passe)
    }
}