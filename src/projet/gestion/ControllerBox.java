package projet.gestion;

import projet.requests.BoxRequest;
import projet.requests.SejourBoxRequest;
import projet.requests.rapports.RapportBoxRequest;
import projet.requests.SejourFamilleRequest;
import projet.exceptions.regle.AnimalDejaPlaceException;
import projet.exceptions.regle.BoxPleinException;
import projet.exceptions.regle.IncompatibiliteTypeException;
import projet.exceptions.regle.MissingEntityException;
import projet.exceptions.donnee.ElementIntrouvableException;

/**
 * Contrôleur gérant toutes les opérations liées aux box d'hébergement.
 * <p>
 * Ce contrôleur permet de gérer les box (ajout, modification, suppression),
 * de placer et retirer des animaux, et d'afficher des statistiques
 * d'occupation.
 * Il gère les règles métier comme la compatibilité espèce/type de box et la
 * capacité maximale.
 * </p>
 * 
 * @author Projet SPA
 * @version 1.0
 * @see projet.requests.BoxRequest
 * @see projet.requests.SejourBoxRequest
 */
public class ControllerBox {

    private BoxRequest boxReq;
    private SejourBoxRequest sejourBoxReq;
    private SejourFamilleRequest sejourFamilleReq;
    private RapportBoxRequest rapportBox;

    /**
     * Constructeur par défaut.
     * Initialise les requêtes pour les box et les séjours.
     */
    public ControllerBox() {
        this.boxReq = new BoxRequest();
        this.sejourBoxReq = new SejourBoxRequest();
        this.sejourFamilleReq = new SejourFamilleRequest();
        this.rapportBox = new RapportBoxRequest();
    }

    /**
     * Affiche l'état d'occupation de tous les box.
     */
    public void listerBox() {
        System.out.println("--- Etat actuel des Box ---");
        boxReq.afficherOccupation();
    }

    /**
     * Affiche les informations détaillées d'un box spécifique.
     * 
     * @param idBox L'identifiant du box.
     * @throws ElementIntrouvableException Si le box n'existe pas.
     */
    public void infoBox(int idBox) throws ElementIntrouvableException {
        boxReq.afficherInfoBox(idBox);
    }

    /**
     * Affiche les statistiques avancées des box.
     */
    public void rapportAvanceBox() {
        System.out.println("--- Statistiques detaillees des Box ---");
        rapportBox.afficherStatistiques();
    }

    /**
     * Place un animal dans un box.
     * Termine automatiquement tout séjour en famille et met à jour le statut.
     * 
     * @param idBox    L'identifiant du box de destination.
     * @param idAnimal L'identifiant de l'animal à placer.
     */
    public void placerAnimalBox(int idBox, int idAnimal) {
        try {
            sejourFamilleReq.terminerSejour(idAnimal);

            if (sejourBoxReq.placerAnimal(idAnimal, idBox)) {
                try {
                    projet.requests.AnimalRequest anReq = new projet.requests.AnimalRequest();
                    projet.tables.Animal a = anReq.getById(idAnimal);

                    String typeBox = boxReq.getBoxType(idBox);
                    String newStatut = "Adoptable";

                    if (typeBox != null) {
                        if (typeBox.equalsIgnoreCase("Quarantaine")) {
                            newStatut = "Quarantaine";
                        } else if (typeBox.equalsIgnoreCase("Infirmerie")) {
                            newStatut = "Soins";
                        }
                    }

                    a.setStatut(newStatut);
                    anReq.update(a);
                    System.out.println("Statut animal mis a jour : " + newStatut);

                } catch (Exception ex) {
                    System.out.println("Erreur maj statut : " + ex.getMessage());
                }
            }
        } catch (BoxPleinException e) {
            System.out.println(e.getMessage());
        } catch (IncompatibiliteTypeException e) {
            System.out.println(e.getMessage());
        } catch (MissingEntityException e) {
            System.out.println(e.getMessage());
        } catch (AnimalDejaPlaceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Vide un box en clôturant tous les séjours actifs.
     * 
     * @param idBox L'identifiant du box à vider.
     */
    public void viderBox(int idBox) {
        int rows = sejourBoxReq.viderBox(idBox);
        if (rows > 0) {
            System.out.println("Succes : " + rows + " sejour(s) cloture(s) pour le box #" + idBox + ".");
        } else {
            System.out.println("Info : Aucun sejour actif trouve pour le box #" + idBox + ".");
        }
    }

    /**
     * Ajoute un nouveau box via saisie interactive.
     * 
     * @param scanner Le scanner pour la saisie utilisateur.
     */
    public void ajouterBox(java.util.Scanner scanner) {
        System.out.println(">> Ajout d'un nouveau Box");
        System.out.print("Type (Chien/Chat) : ");
        String type = scanner.nextLine().trim();
        System.out.print("Capacité max : ");
        String capStr = scanner.nextLine().trim();

        try {
            int cap = Integer.parseInt(capStr);
            projet.tables.Box box = new projet.tables.Box(0, type, cap);
            boxReq.add(box);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : La capacité doit être un nombre entier.");
        }
    }

    /**
     * Modifie un box existant via saisie interactive.
     * 
     * @param idBox   L'identifiant du box à modifier.
     * @param scanner Le scanner pour la saisie utilisateur.
     */
    public void updateBox(int idBox, java.util.Scanner scanner) {
        System.out.println(">> Modification du Box #" + idBox);
        System.out.print("Nouveau Type (Chien/Chat) : ");
        String type = scanner.nextLine().trim();
        System.out.print("Nouvelle Capacité max : ");
        String capStr = scanner.nextLine().trim();

        try {
            int cap = Integer.parseInt(capStr);
            projet.tables.Box box = new projet.tables.Box(idBox, type, cap);
            boxReq.update(box);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : La capacité doit être un nombre entier.");
        }
    }

    /**
     * Supprime un box du système.
     * Vérifie que le box est vide avant suppression.
     * 
     * @param idBox L'identifiant du box à supprimer.
     */
    public void supprimerBox(int idBox) {
        int occupants = sejourBoxReq.getNbOccupants(idBox);
        if (occupants > 0) {
            System.out.println("Erreur : Le box #" + idBox + " n'est pas vide (" + occupants + " animaux présents).");
            System.out.println("Veuillez vider le box avant de le supprimer.");
            return;
        }

        sejourBoxReq.supprimerHistoriqueBox(idBox);

        if (boxReq.delete(idBox)) {
            System.out.println("Box #" + idBox + " supprimé avec succès.");
        } else {
            System.out.println("Échec de la suppression du Box #" + idBox + ".");
        }
    }
}
