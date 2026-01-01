package projet.gestion;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

// --- IMPORTS DES REQUESTS CRUD ---
import projet.requests.ActiviteRequest; 
import projet.requests.AffectationCreneauActiviteRequest;
import projet.requests.AnimalRequest;
import projet.requests.BoxRequest;
import projet.requests.CreneauRequest;
import projet.requests.FamilleRequest;
import projet.requests.IncidentRequest;
import projet.requests.PersonnelRequest;
import projet.requests.PlanningAnimalRequest;
import projet.requests.SejourBoxRequest;
import projet.requests.SejourFamilleRequest;
import projet.requests.SoinRequest;
import projet.requests.VeterinaireRequest;

// --- IMPORTS DES RAPPORTS ---
import projet.requests.rapports.RapportAnimauxAdoptable;
import projet.requests.rapports.RapportBenevoleRequest;
import projet.requests.rapports.RapportBoxRequest;
import projet.requests.rapports.RapportHistoriqueAnimal;
import projet.requests.rapports.RapportIncidentRequest;
import projet.requests.rapports.RapportPlanningRequest;

import projet.tables.Animal;
import projet.tables.Famille;
import projet.tables.Personnel;
import projet.tables.Soin;

public class Controller {

    // --- INSTANCES REQUESTS (CRUD) ---
    private AnimalRequest animalReq;
    private BoxRequest boxReq;
    private FamilleRequest familyReq;
    private CreneauRequest creneauReq;
    private ActiviteRequest activityReq; // Correction du type ici
    private IncidentRequest incidentReq;
    private PersonnelRequest personnelReq;
    private SoinRequest soinReq;
    
    // --- INSTANCES LIAISONS ---
    private SejourBoxRequest sejourBoxReq;
    private SejourFamilleRequest sejourFamilleReq;
    private PlanningAnimalRequest planningAnimalReq;
    private AffectationCreneauActiviteRequest affectationReq;
    private VeterinaireRequest vetoReq;

    // --- INSTANCES RAPPORTS ---
    private RapportPlanningRequest rapportPlanning;
    private RapportAnimauxAdoptable rapportAdoptable;
    private RapportBenevoleRequest rapportBenevole;
    private RapportBoxRequest rapportBox; 
    private RapportHistoriqueAnimal rapportHistorique;
    private RapportIncidentRequest rapportIncident;
    
    private Personnel currentUser;

    public Controller() {
        // Initialisation CRUD
        this.animalReq = new AnimalRequest();
        this.boxReq = new BoxRequest();
        this.familyReq = new FamilleRequest();
        this.creneauReq = new CreneauRequest();
        this.activityReq = new ActiviteRequest(); // Correction de l'instanciation ici
        this.incidentReq = new IncidentRequest();
        this.personnelReq = new PersonnelRequest();
        this.soinReq = new SoinRequest();

        // Initialisation Liaisons
        this.sejourBoxReq = new SejourBoxRequest();
        this.sejourFamilleReq = new SejourFamilleRequest();
        this.planningAnimalReq = new PlanningAnimalRequest();
        this.affectationReq = new AffectationCreneauActiviteRequest();
        this.vetoReq = new VeterinaireRequest();

        // Initialisation RAPPORTS
        this.rapportPlanning = new RapportPlanningRequest();
        this.rapportAdoptable = new RapportAnimauxAdoptable();
        this.rapportBenevole = new RapportBenevoleRequest();
        this.rapportBox = new RapportBoxRequest();
        this.rapportHistorique = new RapportHistoriqueAnimal();
        this.rapportIncident = new RapportIncidentRequest();
    }

    public void setCurrentUser(Personnel user) {
        this.currentUser = user;
    }

    // ==================================================================================
    // 1. GESTION DES ANIMAUX
    // ==================================================================================

    public void listerAnimaux() {
        System.out.println("--- Liste complète des Animaux ---");
        List<Animal> liste = animalReq.getAll(); 
        
        if (liste.isEmpty()) {
            System.out.println("Aucun animal trouvé.");
        } else {
            System.out.printf("%-4s | %-12s | %-10s | %-8s | %-11s | %-11s | %-10s | %s%n", 
                "ID", "Puce", "Nom", "Espèce", "Naissance", "Arrivée", "Statut", "Tests (H/B/Ch/Ct)");
            System.out.println("----------------------------------------------------------------------------------------------------");
            
            for (Animal a : liste) {
                String tests = String.format("%s/%s/%s/%s", 
                    a.isTests_humain() ? "V" : "X", a.isTests_bebe() ? "V" : "X",
                    a.isTests_chien() ? "V" : "X", a.isTests_chat() ? "V" : "X");

                String puce = (a.getPuce() == null) ? "-" : a.getPuce();
                String naiss = (a.getDate_naissance() == null) ? "?" : a.getDate_naissance().toString();

                System.out.printf("%-4d | %-12s | %-10s | %-8s | %-11s | %-11s | %-10s | %s%n", 
                    a.getId_animal(), puce, a.getNom(), a.getEspece(), naiss, a.getDate_arrivee(), a.getStatut(), tests);
            }
        }
    }

    public void ajouterAnimal(Scanner scanner) {
        try {
            System.out.println(">> Ajout d'un nouvel animal");
            Animal a = new Animal();

            System.out.print("Nom : "); a.setNom(scanner.nextLine());
            System.out.print("Espèce (Chat/Chien) : "); a.setEspece(scanner.nextLine());
            System.out.print("Puce (Identifiant unique) : "); a.setPuce(scanner.nextLine());
            
            a.setDate_arrivee(new Date(System.currentTimeMillis()));
            a.setStatut("En attente");

            animalReq.add(a); 
            System.out.println("Succès : Animal ajouté.");
        } catch (Exception e) {
            System.out.println("Erreur ajout animal : " + e.getMessage());
        }
    }

    public void supprimerAnimal(int id) {
        if (currentUser != null && !"Admin".equalsIgnoreCase(currentUser.getType_pers())) {
            System.out.println("Refusé : Droit Admin requis.");
            return;
        }
        if (animalReq.delete(id)) {
            System.out.println("Succès : Animal " + id + " supprimé.");
        } else {
            System.out.println("Erreur : ID introuvable.");
        }
    }

    public void chercherAnimal(String input) {
        try {
            int id = Integer.parseInt(input);
            // Si c'est un ID, on affiche le rapport complet (Dossier médical, etc.)
            rapportHistorique.afficherDossier(id); 
        } catch (NumberFormatException e) {
            // Si ce n'est pas un nombre, on cherche par Nom
            System.out.println("Recherche par nom '" + input + "' :");
            List<Animal> res = animalReq.getByName(input);
            for(Animal a : res) System.out.println(a);
        }
    }

    public void updateAnimal(int id, Scanner scanner) {
        Animal a = animalReq.getById(id);
        if (a == null) { System.out.println("Animal introuvable."); return; }
        
        System.out.println("Modification de " + a.getNom());
        System.out.print("Nouveau statut (Actuel: " + a.getStatut() + ") [Entrée=Ignorer] : ");
        String stat = scanner.nextLine();
        if(!stat.isEmpty()) a.setStatut(stat);

        animalReq.update(a);
        System.out.println("Mise à jour effectuée.");
    }

    public void filtrerAnimaux(String filtre) {
        List<Animal> liste = animalReq.getByStatut(filtre);
        System.out.println("--- Animaux statut : " + filtre + " ---");
        for(Animal a : liste) System.out.println(a);
    }

    // ==================================================================================
    // 2. GESTION DES FAMILLES
    // ==================================================================================

    public void ajouterFamille(Scanner scanner) {
        System.out.println(">> Création Famille");
        Famille f = new Famille();
        
        System.out.print("Nom de famille : "); f.setNom(scanner.nextLine());
        System.out.print("Type (Accueil/Adoption) : "); f.setType_famille(scanner.nextLine());
        System.out.print("Adresse : "); f.setAdresse(scanner.nextLine());
        System.out.print("Contact : "); f.setContact(scanner.nextLine());
        
        familyReq.add(f);
    }

    public void listerFamilles() {
        System.out.println("--- Liste des Familles ---");
        List<Famille> liste = familyReq.getAll();
        for(Famille f : liste) {
            System.out.printf("[%d] %s (%s) - %s%n", f.getId_famille(), f.getNom(), f.getType_famille(), f.getAdresse());
        }
    }

    public void lierFamille(int idAnimal, int idFamille, String type) {
        if(sejourFamilleReq.commencerSejour(idAnimal, idFamille)) {
             Animal a = animalReq.getById(idAnimal);
             if(a != null) {
                 a.setStatut("En " + type);
                 animalReq.update(a);
             }
        }
    }
    
    public void retourDeFamille(int idAnimal) {
        if(sejourFamilleReq.terminerSejour(idAnimal)) {
            Animal a = animalReq.getById(idAnimal);
             if(a != null) {
                 a.setStatut("Au refuge");
                 animalReq.update(a);
             }
             System.out.println("Statut de l'animal mis à jour : Au refuge.");
        }
    }

    public void historiqueFamille(int idFamille) {
        System.out.println("Historique de la famille " + idFamille);
        Famille f = familyReq.getById(idFamille);
        if(f != null) System.out.println("Famille : " + f.getNom() + ", " + f.getAdresse());
    }

    // ==================================================================================
    // 3. GESTION DES BOX
    // ==================================================================================

    public void listerBox() {
        System.out.println("--- État actuel des Box ---");
        boxReq.afficherOccupation(); 
    }

    public void infoBox(int idBox) {
        boxReq.afficherInfoBox(idBox);
    }

    public void rapportAvanceBox() {
        System.out.println("--- Statistiques détaillées des Box ---");
        rapportBox.afficherStatistiques(); 
    }

    public void placerAnimalBox(int idBox, int idAnimal) {
        sejourBoxReq.placerAnimal(idAnimal, idBox);
    }

    public void viderBox(int idBox) {
        int rows = sejourBoxReq.viderBox(idBox);
        if (rows > 0) {
            System.out.println("Succès : " + rows + " séjour(s) clôturé(s) pour le box #" + idBox + ".");
        } else {
            System.out.println("Info : Aucun séjour actif trouvé pour le box #" + idBox + ".");
        }
    }

    // ==================================================================================
    // 4. GESTION BÉNÉVOLES & PLANNING
    // ==================================================================================

    public void ajouterBenevole(Scanner scanner) {
        System.out.println(">> Nouveau Bénévole");
        Personnel p = new Personnel();
        p.setType_pers("Benevole");
        
        System.out.print("Nom : "); p.setNom(scanner.nextLine());
        System.out.print("Prénom : "); p.setPrenom(scanner.nextLine());
        System.out.print("Tel : "); p.setTel(scanner.nextLine());
        System.out.print("User : "); p.setUser(scanner.nextLine());
        System.out.print("Pass : "); p.setPassword(scanner.nextLine());

        personnelReq.add(p);
        System.out.println("Bénévole ajouté.");
    }

    public void afficherPlanning() {
        System.out.println("--- Planning Général ---");
        List<?> liste = creneauReq.getAll();
        System.out.println("Créneaux configurés : " + liste.size());
        affectationReq.listerAffectations();
    }

    public void checkSousEffectif() {
        System.out.println("--- ALERTE : Créneaux en sous-effectif ---");
        List<String> alertes = rapportPlanning.getCreneauxManquants();
        if (alertes.isEmpty()) System.out.println("R.A.S. Planning complet.");
        else for (String alerte : alertes) System.out.println("⚠ " + alerte);
    }

    public void assignerBenevole(int idCreneau, int idBenevole) {
        int idActiviteDefaut = 1; // ID 1 supposé exister (Nettoyage/Général)
        affectationReq.assigner(idCreneau, idBenevole, idActiviteDefaut);
    }
    
    public void ajouterRdvAnimal(int idAnimal, int idCreneau, int idPers, Date date) {
        planningAnimalReq.assigner(idAnimal, idCreneau, idPers, date);
    }

    // ==================================================================================
    // 5. ACTIVITÉS, SOINS & INCIDENTS
    // ==================================================================================

    public void ajouterActivite(int idAnimal, String type) {
        // CORRECTION : activityReq est maintenant de type ActiviteRequest
        activityReq.add(idAnimal, type, "Activité enregistrée via menu");
    }
    
    public void ajouterSoinComplet(int idAnimal, int idVeto, Scanner scanner) {
        System.out.println(">> Ajout d'un soin vétérinaire");
        System.out.print("Type (Vaccin/Opération/Examen) : ");
        String type = scanner.nextLine();
        System.out.print("Libellé (ex: Rage, Stérilisation) : ");
        String libelle = scanner.nextLine();
        System.out.print("Commentaire : ");
        String commentaire = scanner.nextLine();

        soinReq.add(idAnimal, type, libelle, commentaire);
        System.out.println("Info : Soin ajouté. Pensez à lier la signature vétérinaire si nécessaire.");
    }
    
    public void listerTousLesSoins() {
        System.out.println("--- Liste globale des Soins ---");
        List<Animal> animaux = animalReq.getAll();
        boolean empty = true;
        
        System.out.printf("%-5s | %-15s | %-15s | %-20s%n", "ID Soin", "Animal", "Type", "Libellé");
        System.out.println("----------------------------------------------------------------");

        for (Animal a : animaux) {
            List<Soin> soins = soinReq.getByAnimal(a.getId_animal());
            for (Soin s : soins) {
                empty = false;
                System.out.printf("%-5d | %-15s | %-15s | %-20s%n", 
                    s.getId_soin(), a.getNom(), s.getType_soin(), s.getLibelle());
            }
        }
        if (empty) System.out.println("Aucun soin enregistré.");
    }

    public void supprimerSoin(int idSoin) {
        if (soinReq.delete(idSoin)) System.out.println("Succès : Soin #" + idSoin + " supprimé.");
        else System.out.println("Erreur : Soin introuvable.");
    }

    public void signerSoin(int idSoin, int idVeto) {
        vetoReq.assigner(idVeto, idSoin);
    }

    public void declarerIncident(int idAnimal, Scanner scanner) {
        System.out.print("Type (Maladie/Accident) : ");
        String type = scanner.nextLine();
        System.out.print("Description : ");
        String desc = scanner.nextLine();
        
        incidentReq.add(idAnimal, type, desc);
    }

    public void listerIncidents(int idAnimal) {
        rapportIncident.afficherParAnimal(idAnimal);
    }

    // ==================================================================================
    // 6. RAPPORTS GLOBAUX
    // ==================================================================================

    public void statsBenevoles() {
        rapportBenevole.afficherStatistiques();
    }

    public void statsAdoptables() {
        rapportAdoptable.genererListe();
    }
}