package projet.gestion.controller;

import java.util.List;
import java.util.Scanner;

import projet.requests.ActiviteRequest;
import projet.requests.AnimalRequest;
import projet.requests.IncidentRequest;
import projet.requests.SoinRequest;
import projet.requests.VeterinaireRequest;
import projet.requests.rapports.RapportIncidentRequest;
import projet.tables.Animal;
import projet.tables.Soin;

/**
 * Contrôleur gérant les activités, soins et incidents.

 * Ce contrôleur permet de gérer les types d'activités, d'enregistrer
 * des soins vétérinaires, et de déclarer/rechercher des incidents.

 * 
 */
public class ControllerActivite {

    private ActiviteRequest activityReq;
    private IncidentRequest incidentReq;
    private SoinRequest soinReq;
    private AnimalRequest animalReq;
    private VeterinaireRequest vetoReq;
    private RapportIncidentRequest rapportIncident;

    /**
     * Constructeur par défaut.
     * Initialise les requêtes pour les activités, soins et incidents.
     */
    public ControllerActivite() {
        this.activityReq = new ActiviteRequest();
        this.incidentReq = new IncidentRequest();
        this.soinReq = new SoinRequest();
        this.animalReq = new AnimalRequest();
        this.vetoReq = new VeterinaireRequest();
        this.rapportIncident = new RapportIncidentRequest();
    }

    /**
     * Affiche la liste de tous les types d'activités définis.
     */
    public void listerActivites() {
        System.out.println("--- Liste des types d'activites ---");
        var liste = activityReq.getAll();
        if (liste.isEmpty()) {
            System.out.println("Aucune activite definie.");
        } else {
            System.out.printf("%-5s | %s%n", "ID", "Type");
            System.out.println("---------------------");
            for (var a : liste) {
                System.out.printf("%-5d | %s%n", a.getId_activite(), a.getType_act());
            }
        }
    }

    /**
     * Ajoute une activité pour un animal.
     * 
     * @param idAnimal L'identifiant de l'animal.
     * @param type     Le type d'activité.
     */
    public void ajouterActivite(int idAnimal, String type) {
        activityReq.add(idAnimal, type, "Activite enregistree via menu");
    }

    /**
     * Ajoute un nouveau type d'activité.
     * 
     * @param type Le libellé du type d'activité.
     */
    public void ajouterTypeActivite(String type) {
        activityReq.addType(type);
    }

    /**
     * Supprime un type d'activité.
     * 
     * @param idActivite L'identifiant du type à supprimer.
     */
    public void supprimerActivite(int idActivite) {
        if (activityReq.deleteType(idActivite)) {
            System.out.println("Activite #" + idActivite + " supprimee.");
        } else {
            System.out.println("Erreur : Activite introuvable ou utilisee dans le planning.");
        }
    }

    /**
     * Ajoute un soin vétérinaire via saisie interactive.
     * 
     * @param idAnimal L'identifiant de l'animal.
     * @param idVeto   L'identifiant du vétérinaire.
     * @param scanner  Le scanner pour la saisie utilisateur.
     */
    public void ajouterSoinComplet(int idAnimal, int idVeto, Scanner scanner) {
        System.out.println(">> Ajout d'un soin veterinaire");
        System.out.print("Type (Vaccin/Operation/Examen) : ");
        String type = scanner.nextLine();
        System.out.print("Libelle (ex: Rage, Sterilisation) : ");
        String libelle = scanner.nextLine();
        System.out.print("Commentaire : ");
        String commentaire = scanner.nextLine();

        soinReq.add(idAnimal, type, libelle, commentaire);
        System.out.println("Info : Soin ajoute.");
    }

    /**
     * Affiche la liste globale de tous les soins enregistrés.
     */
    public void listerTousLesSoins() {
        System.out.println("--- Liste globale des Soins ---");
        List<Animal> animaux = animalReq.getAll();
        boolean empty = true;

        System.out.printf("%-5s | %-15s | %-15s | %-20s%n", "ID Soin", "Animal", "Type", "Libelle");
        System.out.println("----------------------------------------------------------------");

        for (Animal a : animaux) {
            List<Soin> soins = soinReq.getByAnimal(a.getId_animal());
            for (Soin s : soins) {
                if (s.getLibelle() != null && s.getLibelle().contains("Activit")) {
                    continue;
                }
                empty = false;
                System.out.printf("%-5d | %-15s | %-15s | %-20s%n",
                        s.getId_soin(), a.getNom(), s.getType_soin(), s.getLibelle());
            }
        }
        if (empty)
            System.out.println("Aucun soin enregistre.");
    }

    /**
     * Supprime un soin par son identifiant.
     * 
     * @param idSoin L'identifiant du soin à supprimer.
     */
    public void supprimerSoin(int idSoin) {
        if (soinReq.delete(idSoin))
            System.out.println("Succes : Soin #" + idSoin + " supprime.");
        else
            System.out.println("Erreur : Soin introuvable.");
    }

    /**
     * Assigne un vétérinaire à un soin.
     * 
     * @param idSoin L'identifiant du soin.
     * @param idVeto L'identifiant du vétérinaire.
     */
    public void signerSoin(int idSoin, int idVeto) {
        vetoReq.assigner(idVeto, idSoin);
    }

    /**
     * Déclare un incident pour un animal via saisie interactive.
     * 
     * @param idAnimal L'identifiant de l'animal concerné.
     * @param scanner  Le scanner pour la saisie utilisateur.
     */
    public void declarerIncident(int idAnimal, Scanner scanner) {
        System.out.println(">> Declaration d'incident pour animal #" + idAnimal);
        System.out.print("Type (Maladie/Accident/Comportement/Autre) : ");
        String type = scanner.nextLine().trim();
        System.out.print("Description courte : ");
        String desc = scanner.nextLine().trim();
        System.out.print("Detail/Commentaire (optionnel) : ");
        String detail = scanner.nextLine().trim();

        incidentReq.add(idAnimal, type, desc, detail);
    }

    /**
     * Affiche les incidents d'un animal.
     * 
     * @param idAnimal L'identifiant de l'animal.
     */
    public void listerIncidents(int idAnimal) {
        rapportIncident.afficherParAnimal(idAnimal);
    }

    /**
     * Recherche et affiche un incident par ID ou intitulé.
     * 
     * @param input L'ID ou l'intitulé de l'incident recherché.
     */
    public void chercherIncident(String input) {
        String q = (input == null) ? "" : input.trim();
        if (q.isEmpty()) {
            System.out.println("Erreur : precisez un id ou un intitule.");
            return;
        }

        try {
            int id = Integer.parseInt(q);
            incidentReq.afficherInfo(id);
        } catch (NumberFormatException e) {
            incidentReq.afficherRecherche(q);
        }
    }
}
