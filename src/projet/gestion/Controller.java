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
    private ActiviteRequest activityReq; 
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
        this.activityReq = new ActiviteRequest(); 
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
            rapportHistorique.afficherDossier(id); 
        } catch (NumberFormatException e) {
            System.out.println("Recherche par nom '" + input + "' :");
            List<Animal> res = animalReq.getByName(input);
            for(Animal a : res) System.out.println(a);
        }
    }

    public void chercherFamille(String input) {
        String q = (input == null) ? "" : input.trim();
        if (q.isEmpty()) {
            System.out.println("Erreur : précisez un id ou un nom de famille.");
            return;
        }

        try {
            int id = Integer.parseInt(q);
            Famille f = familyReq.getById(id);
            if (f == null) {
                System.out.println("Famille introuvable : #" + id);
                return;
            }
            System.out.println("=== FAMILLE #" + id + " ===");
            System.out.println("Nom      : " + f.getNom());
            System.out.println("Type     : " + f.getType_famille());
            System.out.println("Adresse  : " + f.getAdresse());
            System.out.println("Contact  : " + f.getContact());
            return;
        } catch (NumberFormatException ignore) { }

        List<Famille> res = familyReq.getByName(q);
        System.out.println("--- Recherche famille : '" + q + "' ---");
        if (res.isEmpty()) {
            System.out.println("Aucune famille trouvée.");
            return;
        }

        System.out.printf("%-5s | %-20s | %-10s | %-25s | %s%n", "ID", "Nom", "Type", "Adresse", "Contact");
        System.out.println("------------------------------------------------------------------------------------------");
        for (Famille f : res) {
            System.out.printf("%-5d | %-20s | %-10s | %-25s | %s%n",
                f.getId_famille(), safe(f.getNom()), safe(f.getType_famille()), truncate(safe(f.getAdresse()), 25), safe(f.getContact()));
        }
    }

    public void chercherBenevole(String input) {
        String q = (input == null) ? "" : input.trim();
        if (q.isEmpty()) {
            System.out.println("Erreur : précisez un id, un prénom, un nom ou un identifiant.");
            return;
        }

        try {
            int id = Integer.parseInt(q);
            Personnel p = personnelReq.getById(id);
            if (p == null) {
                System.out.println("Bénévole introuvable : #" + id);
                return;
            }
            if (p.getType_pers() == null || !p.getType_pers().equalsIgnoreCase("Benevole")) {
                System.out.println("Info : la personne #" + id + " n'est pas un bénévole (type=" + p.getType_pers() + ").");
                return;
            }
            System.out.println("=== BÉNÉVOLE #" + id + " ===");
            System.out.println("Nom      : " + p.getNom());
            System.out.println("Prénom   : " + p.getPrenom());
            System.out.println("Tel      : " + p.getTel());
            System.out.println("User     : " + p.getUser());
            return;
        } catch (NumberFormatException ignore) { }

        List<Personnel> res = personnelReq.searchBenevoles(q);
        System.out.println("--- Recherche bénévole : '" + q + "' ---");
        if (res.isEmpty()) {
            System.out.println("Aucun bénévole trouvé.");
            return;
        }

        System.out.printf("%-5s | %-15s | %-15s | %-15s | %s%n", "ID", "Nom", "Prénom", "Tel", "User");
        System.out.println("--------------------------------------------------------------------------");
        for (Personnel p : res) {
            System.out.printf("%-5d | %-15s | %-15s | %-15s | %s%n",
                p.getId_pers(), safe(p.getNom()), safe(p.getPrenom()), safe(p.getTel()), safe(p.getUser()));
        }
    }

    public void chercherIncident(String input) {
        String q = (input == null) ? "" : input.trim();
        if (q.isEmpty()) {
            System.out.println("Erreur : précisez un id ou un intitulé.");
            return;
        }

        try {
            int id = Integer.parseInt(q);
            incidentReq.afficherInfo(id);
        } catch (NumberFormatException e) {
            incidentReq.afficherRecherche(q);
        }
    }

    private String safe(String s) { return (s == null) ? "-" : s; }

    private String truncate(String s, int max) {
        if (s == null) return "-";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
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
    // 2. GESTION DES FAMILLES (MODIFIÉ)
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

    // MODIF : Met à jour le statut de l'animal après lien
    public void lierFamille(int idAnimal, int idFamille, String type) {
        if(sejourFamilleReq.commencerSejour(idAnimal, idFamille)) {
             try {
                 Animal a = animalReq.getById(idAnimal);
                 if(a != null) {
                     a.setStatut("En " + type); // En Accueil / En Adoption
                     animalReq.update(a);
                     System.out.println("✅ Statut animal mis à jour : " + a.getStatut());
                 }
             } catch (Exception e) {
                 System.out.println("⚠ Séjour créé, mais erreur maj statut.");
             }
        }
    }
    
    // MODIF : Met à jour le statut et ajoute un LOG d'activité
    public void retourDeFamille(int idAnimal) {
        if(sejourFamilleReq.terminerSejour(idAnimal)) {
             try {
                 Animal a = animalReq.getById(idAnimal);
                 if(a != null) {
                     a.setStatut("Au refuge");
                     animalReq.update(a);
                     System.out.println("✅ Statut animal repassé à : Au refuge.");

                     // ADD LOG
                     activityReq.add(idAnimal, "Retour Famille", "Fin de séjour externe");
                     System.out.println("📝 Activité enregistrée.");
                 }
             } catch (Exception e) {
                 System.out.println("⚠ Erreur maj statut/log.");
             }
        }
    }

    // MODIF : Affiche l'historique complet
    public void historiqueFamille(int idFamille) {
        System.out.println("\n=== HISTORIQUE FAMILLE #" + idFamille + " ===");
        Famille f = familyReq.getById(idFamille);
        if(f != null) {
            System.out.println("Famille : " + f.getNom() + ", " + f.getAdresse());
            sejourFamilleReq.afficherHistoriqueParFamille(idFamille);
        } else {
            System.out.println("❌ Famille introuvable.");
        }
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
        int idActiviteDefaut = 1; 
        affectationReq.assigner(idCreneau, idBenevole, idActiviteDefaut);
    }
    
    public void ajouterRdvAnimal(int idAnimal, int idCreneau, int idPers, Date date) {
        planningAnimalReq.assigner(idAnimal, idCreneau, idPers, date);
    }

    // ==================================================================================
    // 5. ACTIVITÉS, SOINS & INCIDENTS
    // ==================================================================================

    public void ajouterActivite(int idAnimal, String type) {
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