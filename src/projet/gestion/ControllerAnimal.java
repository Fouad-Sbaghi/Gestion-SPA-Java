package projet.gestion;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import projet.requests.AnimalRequest;
import projet.requests.rapports.RapportHistoriqueAnimal;
import projet.tables.Animal;
import projet.tables.Personnel;
import projet.exceptions.InvalidFormatException;

public class ControllerAnimal {

    private AnimalRequest animalReq;
    private RapportHistoriqueAnimal rapportHistorique;
    private Personnel currentUser;

    public ControllerAnimal() {
        this.animalReq = new AnimalRequest();
        this.rapportHistorique = new RapportHistoriqueAnimal();
    }

    public void setCurrentUser(Personnel user) {
        this.currentUser = user;
    }

    public void listerAnimaux() {
        System.out.println("--- Liste complete des Animaux ---");
        List<Animal> liste = animalReq.getAll();

        if (liste.isEmpty()) {
            System.out.println("Aucun animal trouve.");
        } else {
            System.out.printf("%-4s | %-12s | %-10s | %-8s | %-11s | %-11s | %-10s | %s%n",
                    "ID", "Puce", "Nom", "Espece", "Naissance", "Arrivee", "Statut", "Tests (H/B/Ch/Ct)");
            System.out.println(
                    "----------------------------------------------------------------------------------------------------");

            for (Animal a : liste) {
                String tests = String.format("%s/%s/%s/%s",
                        a.isTests_humain() ? "V" : "X", a.isTests_bebe() ? "V" : "X",
                        a.isTests_chien() ? "V" : "X", a.isTests_chat() ? "V" : "X");

                String puce = (a.getPuce() == null) ? "-" : a.getPuce();
                String naiss = (a.getDate_naissance() == null) ? "?" : a.getDate_naissance().toString();

                System.out.printf("%-4d | %-12s | %-10s | %-8s | %-11s | %-11s | %-10s | %s%n",
                        a.getId_animal(), puce, a.getNom(), a.getEspece(), naiss, a.getDate_arrivee(), a.getStatut(),
                        tests);
            }
        }
    }

    /**
     * Valide qu'un nom ne contient pas de chiffres.
     */
    private void validerNom(String nom) throws InvalidFormatException {
        if (nom != null && nom.matches(".*\\d.*")) {
            throw new InvalidFormatException("Nom", nom);
        }
    }

    public void ajouterAnimal(Scanner scanner) {
        try {
            System.out.println(">> Ajout d'un nouvel animal");
            Animal a = new Animal();

            System.out.print("Nom : ");
            String nom = scanner.nextLine().trim();
            validerNom(nom);
            a.setNom(nom);

            System.out.print("Espece (Chat/Chien/Autre) : ");
            a.setEspece(scanner.nextLine().trim());

            System.out.print("Puce (laisser vide si inconnue) : ");
            String puce = scanner.nextLine().trim();
            a.setPuce(puce.isEmpty() ? null : puce);

            System.out.print("Date de naissance (YYYY-MM-DD, vide si inconnue) : ");
            String naissStr = scanner.nextLine().trim();
            if (!naissStr.isEmpty()) {
                try {
                    a.setDate_naissance(Date.valueOf(naissStr));
                } catch (Exception e) {
                    System.out.println("Date invalide, ignoree.");
                }
            }

            System.out.print("Statut (Adoptable/Quarantaine/Soins/Au refuge) [defaut: Au refuge] : ");
            String statut = scanner.nextLine().trim();
            a.setStatut(statut.isEmpty() ? "Au refuge" : statut);

            System.out.println("--- Tests comportementaux (repondez V/F) ---");

            System.out.print("Test Humain (V/F) [defaut: F] : ");
            String tH = scanner.nextLine().trim().toUpperCase();
            a.setTests_humain(tH.equals("V"));

            System.out.print("Test Bebe (V/F) [defaut: F] : ");
            String tB = scanner.nextLine().trim().toUpperCase();
            a.setTests_bebe(tB.equals("V"));

            System.out.print("Test Chien (V/F) [defaut: F] : ");
            String tC = scanner.nextLine().trim().toUpperCase();
            a.setTests_chien(tC.equals("V"));

            System.out.print("Test Chat (V/F) [defaut: F] : ");
            String tCh = scanner.nextLine().trim().toUpperCase();
            a.setTests_chat(tCh.equals("V"));

            a.setDate_arrivee(new Date(System.currentTimeMillis()));

            animalReq.add(a);
            System.out.println("Succes : Animal ajoute !");
        } catch (InvalidFormatException e) {
            System.out.println("Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur ajout animal : " + e.getMessage());
        }
    }

    public void supprimerAnimal(int id) {
        if (currentUser != null && !"Admin".equalsIgnoreCase(currentUser.getType_pers())) {
            System.out.println("Refuse : Droit Admin requis.");
            return;
        }
        if (animalReq.delete(id)) {
            System.out.println("Succes : Animal " + id + " supprime.");
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
            if (res.isEmpty()) {
                System.out.println("Aucun animal trouve avec ce nom.");
            } else {
                for (Animal a : res) {
                    rapportHistorique.afficherDossier(a.getId_animal());
                    System.out.println();
                }
            }
        }
    }

    public void updateAnimal(int id, Scanner scanner) {
        Animal a = animalReq.getById(id);
        if (a == null) {
            System.out.println("Animal introuvable.");
            return;
        }

        System.out.println("\n=== Modification de " + a.getNom() + " (ID: " + id + ") ===");
        System.out.println("Appuyez sur Entree pour conserver la valeur actuelle.\n");

        System.out.print("Nom (Actuel: " + a.getNom() + ") : ");
        String nom = scanner.nextLine().trim();
        if (!nom.isEmpty())
            a.setNom(nom);

        System.out.print("Espece (Actuel: " + a.getEspece() + ") : ");
        String espece = scanner.nextLine().trim();
        if (!espece.isEmpty())
            a.setEspece(espece);

        System.out.print("Puce (Actuel: " + a.getPuce() + ") : ");
        String puce = scanner.nextLine().trim();
        if (!puce.isEmpty())
            a.setPuce(puce);

        System.out.print("Statut (Actuel: " + a.getStatut() + ") : ");
        String statut = scanner.nextLine().trim();
        if (!statut.isEmpty())
            a.setStatut(statut);

        System.out.println("\n--- Tests comportementaux (repondez V/F ou Entree pour ignorer) ---");

        System.out.print("Test Humain (Actuel: " + (a.isTests_humain() ? "V" : "F") + ") : ");
        String tHumain = scanner.nextLine().trim().toUpperCase();
        if (tHumain.equals("V"))
            a.setTests_humain(true);
        else if (tHumain.equals("F"))
            a.setTests_humain(false);

        System.out.print("Test Bebe (Actuel: " + (a.isTests_bebe() ? "V" : "F") + ") : ");
        String tBebe = scanner.nextLine().trim().toUpperCase();
        if (tBebe.equals("V"))
            a.setTests_bebe(true);
        else if (tBebe.equals("F"))
            a.setTests_bebe(false);

        System.out.print("Test Chien (Actuel: " + (a.isTests_chien() ? "V" : "F") + ") : ");
        String tChien = scanner.nextLine().trim().toUpperCase();
        if (tChien.equals("V"))
            a.setTests_chien(true);
        else if (tChien.equals("F"))
            a.setTests_chien(false);

        System.out.print("Test Chat (Actuel: " + (a.isTests_chat() ? "V" : "F") + ") : ");
        String tChat = scanner.nextLine().trim().toUpperCase();
        if (tChat.equals("V"))
            a.setTests_chat(true);
        else if (tChat.equals("F"))
            a.setTests_chat(false);

        if (animalReq.update(a)) {
            System.out.println("\nMise a jour effectuee avec succes !");
        } else {
            System.out.println("\nErreur lors de la mise a jour.");
        }
    }

    public void filtrerAnimaux(String filtre) {
        List<Animal> liste = animalReq.getByStatut(filtre);
        System.out.println("--- Animaux statut : " + filtre + " ---");
        for (Animal a : liste)
            System.out.println(a);
    }

    // Methodes utilitaires pour la recherche
    public AnimalRequest getAnimalReq() {
        return animalReq;
    }
}
