package projet.gestion;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un rapport sérialisable.
 * <p>
 * Contient le type, le titre, les données et la date de génération.
 * </p>
 */
public class Rapport implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String titre;
    private LocalDateTime dateGeneration;
    private List<String> lignes;

    /**
     * Constructeur.
     * 
     * @param type  Le type de rapport (benevoles, adoptables, box).
     * @param titre Le titre du rapport.
     */
    public Rapport(String type, String titre) {
        this.type = type;
        this.titre = titre;
        this.dateGeneration = LocalDateTime.now();
        this.lignes = new ArrayList<>();
    }

    /**
     * Ajoute une ligne au rapport.
     * 
     * @param ligne La ligne à ajouter.
     */
    public void ajouterLigne(String ligne) {
        lignes.add(ligne);
    }

    /**
     * Retourne le type de rapport.
     */
    public String getType() {
        return type;
    }

    /**
     * Retourne le titre du rapport.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Retourne la date de génération.
     */
    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    /**
     * Retourne les lignes du rapport.
     */
    public List<String> getLignes() {
        return lignes;
    }

    /**
     * Affiche le rapport dans la console.
     */
    public void afficher() {
        System.out.println("\n========================================================");
        System.out.println("       " + titre.toUpperCase());
        System.out.println(
                "       Généré le : " + dateGeneration.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("========================================================\n");

        for (String ligne : lignes) {
            System.out.println(ligne);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s",
                type,
                titre,
                dateGeneration.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }
}
