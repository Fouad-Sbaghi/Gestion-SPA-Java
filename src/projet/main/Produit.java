package projet.main;

import projet.tables.*;
import java.util.HashMap;

public class Produit implements ITable 
{

	private int id;
	private int lot;
	private String nom;
	private String description;
	private String categorie;
	private double prix; 
	
	private String values ; 
	private HashMap<String, fieldType> map;
	
	
	
	public Produit(int id, int lot, String nom, String description, String categorie, double prix) 
	{
	    // 1. On initialise les données sauf values et map parce que c'est la class qui va les générer a partir des autres attributs
	    this.id = id;
	    this.lot = lot;
	    this.nom = nom;
	    this.description = description;
	    this.categorie = categorie;
	    this.prix = prix;
	    
	    this.getStruct(); 
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLot() {
		return lot;
	}

	public void setLot(int lot) {
		this.lot = lot;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public void setValues(String values) {
		this.values = values;
	}
	
	public HashMap<String, fieldType> getMap()
	{
		return map;
	}
	
	public void setMap(HashMap<String, fieldType> map) {
		this.map = map;
	}

	@Override
	public String getValues()
	{
		return values;
	}
	
	
	@Override
	public void getStruct()
	{
		this.map = new HashMap<>();
		
		map.put("id", fieldType.INT4);
		map.put("lot", fieldType.INT4);
		map.put("nom", fieldType.VARCHAR);
		map.put("description", fieldType.VARCHAR);
		map.put("categorie", fieldType.VARCHAR);
		map.put("prix", fieldType.NUMERIC);

		this.values = this.id + ", " 
                + this.lot + ", " 
                + "'" + this.nom + "', " 
                + "'" + this.description + "', " 
                + "'" + this.categorie + "', " 
                + this.prix;
	}
	
	@Override
	public boolean check(HashMap<String, fieldType> tableStruct)
	{
		this.getStruct();
		
		for (String key : this.map.keySet()) 
		{
			if(!tableStruct.containsKey(key)) //ici on vérifie si les noms ne correspondent pas
			{
				return false;
			}
			
			if(this.map.get(key) != tableStruct.get(key)) // ici on vérif le type
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
    public String toString() 
	{
        return "Produit [id=" + id + ", nom=" + nom + ", prix=" + prix + "]";
    }
	
	
	
}
