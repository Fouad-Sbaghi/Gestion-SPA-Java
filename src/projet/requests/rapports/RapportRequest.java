package projet.requests.rapports;

/**
 * Classe abstraite de base pour tous les rapports.

 * Fournit des utilitaires d'affichage communs aux rapports dérivés.

 */
public abstract class RapportRequest {

    /**
     * Méthode utilitaire pour afficher un titre standardisé dans la console.
     * Accessible par toutes les classes filles (protected).
     */
    protected void printHeader(String title) {
        System.out.println("\n========================================================");
        System.out.println("       " + title.toUpperCase());
        System.out.println("========================================================\n");
    }
}