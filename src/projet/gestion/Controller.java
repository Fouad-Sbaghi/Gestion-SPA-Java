package projet.gestion;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import projet.requests.AnimalRequest;
import projet.requests.BoxRequest;
import projet.requests.rapports.RapportPlanningRequest;
import projet.tables.Animal;
import projet.tables.Personnel;

// Import des Exceptions
import projet.exceptions.DuplicatedIdException;
import projet.exceptions.InvalidFormatException;
import projet.exceptions.MissingEntityException;

public class Controller {

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
        System.out.println("--- Liste des Animaux ---");
        List<Animal> liste = animalReq.getAll(); 
        
        if (liste.isEmpty()) {
            System.out.println("Aucun animal trouvé.");
        } else {
            System.out.printf("%-5s | %-15s | %-10s | %s%n", "ID", "Nom", "Espèce", "Statut");
            System.out.println("----------------------------------------------------");
            for (Animal a : liste) {
                System.out.printf("%-5d | %-15s | %-10s | %s%n", 
                    a.getId_animal(), a.getNom(), a.getEspece(), a.getStatut());
            }
        }
    }

    public void ajouterAnimal(Scanner scanner) {
        try {
            System.out.println(">> Ajout d'un nouvel animal");
            Animal a = new Animal();

            // 1. Saisie et Validation du NOM
            System.out.print("Nom : ");
            String saisieNom = scanner.nextLine().trim();
            // Regex : Uniquement lettres, tirets et espaces. Pas de chiffres.
            if (!saisieNom.matches("^[a-zA-Z\\s\\-]+$")) {
                throw new InvalidFormatException("Nom", saisieNom);
            }
            a.setNom(saisieNom);

            // 2. Saisie Espèce
            System.out.print("Espèce (Chat/Chien) : ");
            a.setEspece(scanner.nextLine());

            // 3. Saisie Puce
            System.out.print("Puce (Identifiant unique) : ");
            a.setPuce(scanner.nextLine());
            
            // Valeurs par défaut
            a.setDate_arrivee(new Date(System.currentTimeMillis()));
            a.setStatut("En attente");

            // 4. Appel BDD (Peut lancer DuplicatedIdException)
            animalReq.add(a); 
            System.out.println("✅ Succès : Animal ajouté.");

        } catch (InvalidFormatException e) {
            // Erreur de format (Nom avec chiffres)
            System.out.println("❌ ERREUR DE SAISIE : " + e.getMessage());
            
        } catch (DuplicatedIdException e) {
            // Erreur de doublon (Puce déjà existante)
            System.out.println("❌ ERREUR DOUBLON : " + e.getMessage());
            
        } catch (Exception e) {
            // Autre erreur imprévue
            System.out.println("❌ Erreur technique : " + e.getMessage());
        }
    }

    public void supprimerAnimal(int id) {
        if (currentUser != null && !"Admin".equalsIgnoreCase(currentUser.getType_pers())) {
            System.out.println("⛔ Refusé : Seuls les admins peuvent supprimer.");
            return;
        }

        try {
            // Appel BDD (Peut lancer MissingEntityException)
            animalReq.delete(id);
            System.out.println("✅ Succès : Fiche animal " + id + " supprimée.");

        } catch (MissingEntityException e) {
            // Erreur si l'ID n'existe pas
            System.out.println("❓ INTROUVABLE : " + e.getMessage());
            
        } catch (Exception e) {
            System.out.println("❌ Erreur technique : " + e.getMessage());
        }
    }

    // --- GESTION BOX ---

    public void listerBox() {
        System.out.println("--- État des Box ---");
        boxReq.afficherOccupation(); 
    }
    
    

    // --- PLANNING & ALERTES ---

    public void afficherPlanning() {
        System.out.println("Fonctionnalité Planning à implémenter.");
    }

    public void checkSousEffectif() {
        System.out.println("--- ALERTE : Créneaux en sous-effectif ---");
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