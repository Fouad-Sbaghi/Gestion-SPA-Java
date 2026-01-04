package projet.gestion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import projet.requests.rapports.RapportAnimauxAdoptable;
import projet.requests.rapports.RapportBenevoleRequest;
import projet.requests.rapports.RapportBoxRequest;

/**
 * Classe qui gère la sérialisation Java des rapports.
 * <p>
 * Permet de sauvegarder les rapports dans des fichiers binaires (.ser)
 * et de les restaurer ultérieurement pour affichage.
 * </p>
 * 
 * @version 1.0
 */
public class Sauvegarde {

    private static final String DOSSIER_RAPPORTS = "src/projet/rapports_saved";

    /**
     * Initialise le dossier de rapports s'il n'existe pas.
     */
    private static void initDossier() {
        File dossier = new File(DOSSIER_RAPPORTS);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }
    }

    /**
     * Génère un nom de fichier unique basé sur le type et la date.
     * 
     * @param type Le type de rapport.
     * @return Le chemin complet du fichier .ser.
     */
    private static String genererNomFichier(String type) {
        initDossier();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return DOSSIER_RAPPORTS + File.separator + "rapport_" + type + "_" + timestamp + ".ser";
    }

    /**
     * Sauvegarde un rapport dans un fichier binaire (Sérialisation).
     * 
     * @param rapport Le rapport à sauvegarder.
     * @param fichier Le fichier de destination.
     * @throws IOException En cas d'erreur d'écriture.
     */
    public static void sauvegarder(Rapport rapport, File fichier) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(rapport);
        }
    }

    /**
     * Charge un rapport depuis un fichier binaire (Désérialisation).
     * 
     * @param fichier Le fichier source contenant le rapport.
     * @return Le rapport reconstruit.
     * @throws IOException            En cas d'erreur de lecture.
     * @throws ClassNotFoundException Si la classe Rapport est incompatible.
     */
    public static Rapport charger(File fichier) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            return (Rapport) ois.readObject();
        }
    }

    /**
     * Charge et affiche un rapport depuis un fichier.
     * 
     * @param nomFichier Le nom du fichier (avec ou sans chemin).
     */
    public static void chargerEtAfficher(String nomFichier) {
        File fichier = new File(nomFichier);

        // Si le fichier n'existe pas, chercher dans le dossier rapports
        if (!fichier.exists()) {
            fichier = new File(DOSSIER_RAPPORTS + File.separator + nomFichier);
        }

        if (!fichier.exists()) {
            System.out.println("Erreur : Fichier introuvable : " + nomFichier);
            return;
        }

        try {
            Rapport rapport = charger(fichier);
            rapport.afficher();
        } catch (IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Format de fichier incompatible.");
        }
    }

    /**
     * Liste tous les rapports sauvegardés dans le dossier.
     */
    public static void listerRapports() {
        initDossier();
        File dossier = new File(DOSSIER_RAPPORTS);
        File[] fichiers = dossier.listFiles((dir, name) -> name.endsWith(".ser"));

        if (fichiers == null || fichiers.length == 0) {
            System.out.println("Aucun rapport sauvegardé.");
            return;
        }

        System.out.println("\n--- Rapports sauvegardés ---");
        System.out.printf("%-5s | %-40s | %s%n", "N°", "Fichier", "Infos");
        System.out.println("----------------------------------------------------------------------");

        int i = 1;
        for (File f : fichiers) {
            try {
                Rapport r = charger(f);
                System.out.printf("%-5d | %-40s | %s%n", i++, f.getName(), r.toString());
            } catch (Exception e) {
                System.out.printf("%-5d | %-40s | (erreur de lecture)%n", i++, f.getName());
            }
        }
    }

    /**
     * Exporte le rapport des bénévoles (sérialisation binaire).
     * Appelle la méthode genererRapport() de RapportBenevoleRequest.
     * 
     * @return Le chemin du fichier créé, ou null en cas d'erreur.
     */
    public static String exporterBenevoles() {
        String fichier = genererNomFichier("benevoles");

        try {
            RapportBenevoleRequest rapportReq = new RapportBenevoleRequest();
            Rapport rapport = rapportReq.genererRapport();
            sauvegarder(rapport, new File(fichier));
            System.out.println("Rapport sérialisé : " + fichier);
            return fichier;
        } catch (IOException e) {
            System.err.println("Erreur export rapport bénévoles : " + e.getMessage());
            return null;
        }
    }

    /**
     * Exporte le rapport des animaux adoptables (sérialisation binaire).
     * Appelle la méthode genererRapport() de RapportAnimauxAdoptable.
     * 
     * @return Le chemin du fichier créé, ou null en cas d'erreur.
     */
    public static String exporterAdoptables() {
        String fichier = genererNomFichier("adoptables");

        try {
            RapportAnimauxAdoptable rapportReq = new RapportAnimauxAdoptable();
            Rapport rapport = rapportReq.genererRapport();
            sauvegarder(rapport, new File(fichier));
            System.out.println("Rapport sérialisé : " + fichier);
            return fichier;
        } catch (IOException e) {
            System.err.println("Erreur export rapport adoptables : " + e.getMessage());
            return null;
        }
    }

    /**
     * Exporte le rapport d'occupation des box (sérialisation binaire).
     * Appelle la méthode genererRapport() de RapportBoxRequest.
     * 
     * @return Le chemin du fichier créé, ou null en cas d'erreur.
     */
    public static String exporterBox() {
        String fichier = genererNomFichier("box");

        try {
            RapportBoxRequest rapportReq = new RapportBoxRequest();
            Rapport rapport = rapportReq.genererRapport();
            sauvegarder(rapport, new File(fichier));
            System.out.println("Rapport sérialisé : " + fichier);
            return fichier;
        } catch (IOException e) {
            System.err.println("Erreur export rapport box : " + e.getMessage());
            return null;
        }
    }
}
