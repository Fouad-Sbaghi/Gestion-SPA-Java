package projet.gestion;

import projet.requests.rapports.RapportAnimauxAdoptable;
import projet.requests.rapports.RapportBenevoleRequest;

public class ControllerRapport {

    private RapportAnimauxAdoptable rapportAdoptable;
    private RapportBenevoleRequest rapportBenevole;

    public ControllerRapport() {
        this.rapportAdoptable = new RapportAnimauxAdoptable();
        this.rapportBenevole = new RapportBenevoleRequest();
    }

    public void statsBenevoles() {
        rapportBenevole.afficherStatistiques();
    }

    public void statsAdoptables() {
        rapportAdoptable.genererListe();
    }
}
