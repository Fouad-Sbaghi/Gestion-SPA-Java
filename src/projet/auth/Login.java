package projet.auth;

import projet.exceptions.AuthentificationException;
import projet.requests.PersonnelRequest;
import projet.tables.Personnel;

/**
 * Gestionnaire d'authentification utilisateur.
 * <p>
 * Vérifie les identifiants et retourne l'objet Personnel en cas de succès.
 * </p>
 * 
 * @see projet.tables.Personnel
 * @see projet.exceptions.AuthentificationException
 */
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
        Personnel user = personnelRequest.getByUsername(username);

        if (user == null) {
            throw new AuthentificationException(username);
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthentificationException(username, "Mot de passe incorrect");
        }

        return user;
    }
}