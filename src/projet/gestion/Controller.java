package projet.gestion;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import projet.requests.AnimalRequest;
import projet.requests.BoxRequest;
import projet.requests.rapports.RapportPlanningRequest;
import projet.tables.Animal;
import projet.tables.Personnel;

public class Controller {

    // On instancie les Requests une seule fois pour les réutiliser
    private AnimalRequest animalReq;
    private BoxRequest boxReq;
    private RapportPlanningRequest rapportReq;
    
    private Personnel currentUser;

    public Controller() {
        this.animalReq = new AnimalRequest();
        this.boxReq = new BoxRequest();
        this.rapportReq = new RapportPlanningRequest();
    }

    public void setCurrentUser(Personnel user) {
        this.currentUser = user;
    }

    // --- GESTION ANIMAUX ---

    public void listerAnimaux() {
        System.out.println("--- Liste complète des Animaux ---");
        List<Animal> liste = animalReq.getAll(); 
        
        if (liste.isEmpty()) {
            System.out.println("Aucun animal trouvé.");
        } else {
            // En-tête large
            System.out.printf("%-4s | %-12s | %-10s | %-8s | %-11s | %-11s | %-10s | %s%n", 
                "ID", "Puce", "Nom", "Espèce", "Naissance", "Arrivée", "Statut", "Tests (H/B/Ch/Ct)");
            System.out.println("----------------------------------------------------------------------------------------------------");
            
            for (Animal a : liste) {
                // On formate les tests pour que ce soit lisible (ex: V/X/V/X)
                String tests = String.format("%s/%s/%s/%s", 
                    a.isTests_humain() ? "V" : "X",
                    a.isTests_bebe()   ? "V" : "X",
                    a.isTests_chien()  ? "V" : "X",
                    a.isTests_chat()   ? "V" : "X"
                );

                // Gestion des null pour éviter d'afficher "null"
                String puce = (a.getPuce() == null) ? "-" : a.getPuce();
                String naiss = (a.getDate_naissance() == null) ? "?" : a.getDate_naissance().toString();

                System.out.printf("%-4d | %-12s | %-10s | %-8s | %-11s | %-11s | %-10s | %s%n", 
                    a.getId_animal(), 
                    puce, 
                    a.getNom(), 
                    a.getEspece(), 
                    naiss, 
                    a.getDate_arrivee(), 
                    a.getStatut(),
                    tests);
            }
        }
    }

    public void ajouterAnimal(Scanner scanner) {
        try {
            System.out.println(">> Ajout d'un nouvel animal");
            Animal a = new Animal();

            System.out.print("Nom : ");
            a.setNom(scanner.nextLine());

            System.out.print("Espèce (Chat/Chien) : ");
            a.setEspece(scanner.nextLine());

            System.out.print("Puce (Identifiant unique) : ");
            a.setPuce(scanner.nextLine());
            
            // Pour simplifier, on met la date du jour
            a.setDate_arrivee(new Date(System.currentTimeMillis()));
            a.setStatut("En attente");

            // Appel à la base de données
            animalReq.add(a); 
            System.out.println("Succès : Animal ajouté.");

        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    public void supprimerAnimal(int id) {
        // Vérification des droits (optionnel)
        if (currentUser != null && !"Admin".equalsIgnoreCase(currentUser.getType_pers())) {
            System.out.println("Refusé : Seuls les admins peuvent supprimer.");
            return;
        }

        // Appel BDD
        if (animalReq.delete(id)) {
            System.out.println("Succès : Fiche animal " + id + " supprimée.");
        } else {
            System.out.println("Erreur : Impossible de supprimer (ID introuvable ?)");
        }
    }

    // --- GESTION BOX ---

    public void listerBox() {
        System.out.println("--- État des Box ---");
        // Suppose une méthode getOccupation() dans BoxRequest
        boxReq.afficherOccupation(); 
    }

    // --- PLANNING & ALERTES ---

    public void afficherPlanning() {
        System.out.println("Fonctionnalité Planning à implémenter (appel CreneauRequest).");
    }

    public void checkSousEffectif() {
        System.out.println("--- ALERTE : Créneaux en sous-effectif ---");
        // Suppose une méthode dans RapportPlanningRequest
        List<String> alertes = rapportReq.getCreneauxManquants();
        
        if (alertes.isEmpty()) {
            System.out.println("Aucun problème détecté. Le planning est complet.");
        } else {
            for (String alerte : alertes) {
                System.out.println("⚠ " + alerte);
            }
        }
    }
}