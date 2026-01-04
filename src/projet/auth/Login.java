package projet.auth;

import projet.exceptions.AuthentificationException;
import projet.requests.PersonnelRequest;
import projet.tables.Personnel;

public class Login {

    private PersonnelRequest personnelRequest;

    public Login() {
        this.personnelRequest = new PersonnelRequest();
    }

    /**
     * Tente d'authentifier un utilisateur.
     * 
     * @param username Le login (colonne 'user' en BDD)
     * @param password Le mot de passe
     * @return L'objet Personnel si succès.
     * @throws AuthentificationException si l'utilisateur est inconnu ou le mot de
     *                                   passe incorrect.
     */
    public Personnel authentifier(String username, String password) throws AuthentificationException {
        // 1. Récupère l'utilisateur en BDD
        Personnel user = personnelRequest.getByUsername(username);

        // 2. Vérifie si l'utilisateur existe
        if (user == null) {
            throw new AuthentificationException(username);
        }

        // 3. Vérifie si le mot de passe correspond
        if (!user.getPassword().equals(password)) {
            throw new AuthentificationException(username, "Mot de passe incorrect");
        }

        return user; // Connexion réussie
    }
}