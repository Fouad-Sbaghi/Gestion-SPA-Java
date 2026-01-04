package projet.gestion.controller;

import projet.requests.rapports.RapportAnimauxAdoptable;
import projet.requests.rapports.RapportBenevoleRequest;

/**
 * Contrôleur gérant la génération des rapports et statistiques.

 * Ce contrôleur permet de générer des rapports sur les bénévoles
 * et la liste des animaux adoptables.

 * 
 */
public class ControllerRapport {

    private RapportAnimauxAdoptable rapportAdoptable;
    private RapportBenevoleRequest rapportBenevole;

    /**
     * Constructeur par défaut.
     * Initialise les générateurs de rapports.
     */
    public ControllerRapport() {
        this.rapportAdoptable = new RapportAnimauxAdoptable();
        this.rapportBenevole = new RapportBenevoleRequest();
    }

    /**
     * Affiche les statistiques des bénévoles.
     */
    public void statsBenevoles() {
        rapportBenevole.afficherStatistiques();
    }

    /**
     * Génère et affiche la liste des animaux adoptables.
     */
    public void statsAdoptables() {
        rapportAdoptable.genererListe();
    }
}
