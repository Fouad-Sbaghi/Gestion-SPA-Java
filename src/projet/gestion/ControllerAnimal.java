package projet.gestion;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import projet.requests.AnimalRequest;
import projet.requests.rapports.RapportHistoriqueAnimal;
import projet.tables.Animal;
import projet.tables.Personnel;
import projet.exceptions.donnee.format.InvalidFormatException;
import projet.exceptions.regle.DroitsInsuffisantsException;

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
     * Valide qu'un nom ne contient que des lettres (avec accents), espaces et
     * tirets.
     */
    private void validerNom(String nom) throws InvalidFormatException {
        // Regex : "Ne correspond PAS uniquement à des lettres, espaces ou tirets"
        if (nom != null && !nom.matches("[a-zA-Zà-ÿÀ-ß\\s-]+")) {
            throw new InvalidFormatException("Nom", nom);
        }
    }

    /**
     * Valide le format d'une puce (10 chiffres selon la norme ISO).
     */
    private void validerPuce(String puce) throws projet.exceptions.donnee.format.InvalidPuceException {
        if (puce != null && !puce.isEmpty() && !puce.matches("^[0-9]{10}$")) {
            throw new projet.exceptions.donnee.format.InvalidPuceException(puce,
                    "La puce doit contenir exactement 10 chiffres");
        }
    }

    /**
     * Valide que l'espèce est connue.
     */
    private void validerEspece(String espece) throws projet.exceptions.regle.EspeceInconnueException {
        String[] especesAcceptees = { "Chat", "Chien", "Lapin", "Rongeur", "Oiseau", "Reptile", "Autre" };
        boolean valide = false;
        for (String e : especesAcceptees) {
            if (e.equalsIgnoreCase(espece)) {
                valide = true;
                break;
            }
        }
        if (!valide) {
            throw new projet.exceptions.regle.EspeceInconnueException(espece, especesAcceptees);
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

            System.out.print("Espece (Chat/Chien/Lapin/Rongeur/Oiseau/Reptile/Autre) : ");
            String espece = scanner.nextLine().trim();
            validerEspece(espece);
            a.setEspece(espece);

            System.out.print("Puce (laisser vide si inconnue, format: 10 chiffres) : ");
            String puce = scanner.nextLine().trim();
            if (!puce.isEmpty()) {
                validerPuce(puce);
            }
            a.setPuce(puce.isEmpty() ? null : puce);

            System.out.print("Date de naissance (YYYY-MM-DD, vide si inconnue) : ");
            String naissStr = scanner.nextLine().trim();
            if (!naissStr.isEmpty()) {
                try {
                    Date dateNaissance = Date.valueOf(naissStr);
                    // Vérifier que la date n'est pas dans le futur
                    if (dateNaissance.after(new Date(System.currentTimeMillis()))) {
                        throw new projet.exceptions.donnee.format.DateFutureException("Date de naissance", naissStr);
                    }
                    a.setDate_naissance(dateNaissance);
                } catch (projet.exceptions.donnee.format.DateFutureException e) {
                    throw e; // Relancer pour le catch externe
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

            Date dateArrivee = new Date(System.currentTimeMillis());
            a.setDate_arrivee(dateArrivee);

            // Vérifier que la date de naissance est antérieure ou égale à la date d'arrivée
            if (a.getDate_naissance() != null && a.getDate_naissance().after(dateArrivee)) {
                throw new projet.exceptions.donnee.format.DateIncoherenteException(
                        "Date de naissance", "Date d'arrivée",
                        "La date de naissance ne peut pas être postérieure à la date d'arrivée");
            }

            animalReq.add(a);
            System.out.println("Succes : Animal ajoute !");
        } catch (projet.exceptions.donnee.format.DateIncoherenteException e) {
            System.out.println(e.getMessage());
        } catch (projet.exceptions.donnee.format.DateFutureException e) {
            System.out.println(e.getMessage());
        } catch (projet.exceptions.donnee.format.InvalidPuceException e) {
            System.out.println(e.getMessage());
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        } catch (projet.exceptions.regle.EspeceInconnueException e) {
            System.out.println(e.getMessage());
        } catch (projet.exceptions.donnee.DuplicatedIdException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur ajout animal : " + e.getMessage());
        }
    }

    public void supprimerAnimal(int id) {
        try {
            if (currentUser != null && !"Admin".equalsIgnoreCase(currentUser.getType_pers())) {
                throw new DroitsInsuffisantsException("Suppression d'animal", currentUser.getType_pers(), "Admin");
            }

            // Vérifier si l'animal a un séjour actif (box ou famille)
            projet.requests.SejourBoxRequest boxReq = new projet.requests.SejourBoxRequest();
            projet.requests.SejourFamilleRequest familleReq = new projet.requests.SejourFamilleRequest();

            int boxActuel = boxReq.getBoxActuel(id);
            int familleActuelle = familleReq.getFamilleActuelle(id);

            if (boxActuel != -1) {
                throw new projet.exceptions.regle.SejourActifException(id,
                        "suppression (l'animal est dans le box #" + boxActuel + ")");
            }
            if (familleActuelle != -1) {
                throw new projet.exceptions.regle.SejourActifException(id,
                        "suppression (l'animal est dans la famille #" + familleActuelle + ")");
            }

            // Vérifier que l'animal existe
            Animal a = animalReq.getById(id);
            if (a == null) {
                throw new projet.exceptions.donnee.ElementIntrouvableException("Animal", id);
            }

            if (animalReq.delete(id)) {
                System.out.println("Succes : Animal " + id + " supprime.");
            }
        } catch (DroitsInsuffisantsException e) {
            System.out.println(e.getMessage());
        } catch (projet.exceptions.regle.SejourActifException e) {
            System.out.println(e.getMessage());
        } catch (projet.exceptions.donnee.ElementIntrouvableException e) {
            System.out.println(e.getMessage());
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
            if (a.isTests_humain() || a.isTests_bebe() || a.isTests_chien() || a.isTests_chat()) {
                System.out.println(a);
            }
    }

    // Methodes utilitaires pour la recherche
    public AnimalRequest getAnimalReq() {
        return animalReq;
    }
}
