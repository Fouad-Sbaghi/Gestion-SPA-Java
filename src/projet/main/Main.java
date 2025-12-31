package projet.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;

import projet.connexion.Connexion;
import projet.gestion.Gestion;


public class Main {

    public static void main(String[] args) {
        Gestion gestion = new Gestion();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String table = "produit";

        System.out.println("----------------------- Console SQL Gestionnaire ------------------------");
        System.out.println("|    Commandes : CREATE, INSERT, DISPLAY, REMOVE, STRUCT, DROP, EXIT    |");
        System.out.println("-------------------------------------------------------------------------");
        try {
            while (true) {
                System.out.print("\nCommande > ");
                String cmd = reader.readLine().trim().toUpperCase();

                switch (cmd) {
                    case "CREATE":
                        String sqlCreate = "CREATE TABLE " + table + " (" +
                                "id INT PRIMARY KEY, " +
                                "lot INT, " +
                                "nom VARCHAR(50), " +
                                "description VARCHAR(255), " +
                                "categorie VARCHAR(50), " +
                                "prix NUMERIC" + 
                                ")";
                        try 
                        {
                            gestion.execute(sqlCreate);
                            System.out.println("Table '" + table + "' créée.");
                        } 
                        catch (SQLException e) 
                        {
                            System.out.println("Erreur (Table existe déjà ?) : " + e.getMessage());
                        }
                        break;

                    case "INSERT":
                        try 
                        {
                            System.out.println("Entrez les infos du produit :");
                            System.out.print("ID : ");
                            int id = Integer.parseInt(reader.readLine());
                            
                            System.out.print("Numéro Lot : ");
                            int lot = Integer.parseInt(reader.readLine());
                            
                            System.out.print("Nom : ");
                            String nom = reader.readLine();
                            
                            System.out.print("Description : ");
                            String desc = reader.readLine();
                            
                            System.out.print("Catégorie : ");
                            String cat = reader.readLine();
                            
                            System.out.print("Prix (ex 59.99: ");
                            double prix = Double.parseDouble(reader.readLine());

                            Produit p = new Produit(id, lot, nom, desc, cat, prix);
                            gestion.insert(p, table);
                            
                        } 
                        catch (NumberFormatException e) 
                        {
                            System.out.println("Erreur : Entrez un nombre valide.");
                        }
                        break;

                    case "DISPLAY":
                        gestion.displayTable(table);
                        break;

                    case "REMOVE":
                        System.out.print("ID du produit à supprimer : ");
                        String idRem = reader.readLine();
                        gestion.execute("DELETE FROM " + table + " WHERE id = " + idRem);
                        break;

                    case "STRUCT":
                        gestion.structTable(table, true);
                        break;

                    case "DROP":
                        gestion.execute("DROP TABLE " + table);
                        System.out.println("Table supprimée.");
                        break;
                        
                    case "EXIT":
                        System.out.println("Fermeture.");
                        Connexion.close();
                        return;

                    default:
                        System.out.println("Commande inconnue.");
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
