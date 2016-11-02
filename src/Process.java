import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.ViewportLayout;

import java.util.Scanner;

public class Process {
	private static HashMap<Integer, Equipement> equipements= new HashMap<Integer, Equipement>();
	private static Integer nbEquipement=0;
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try {
			initEquipements();
			initOperation();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void initEquipements() throws Exception {
		// TODO Auto-generated method stub
		for(int i=1;i<5;i++) {
			nbEquipement++;
			equipements.put(nbEquipement,new Equipement("equipement_"+i, 2050+i));
		}
	}

	private static void equipement(Equipement equipe) throws Exception
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("\t\t Gestion de l'equipement : "+equipe.monNom);
		System.out.println("i => Informations concernant l'equipement");
		System.out.println("l => Liste des equipements CA");
		System.out.println("d => Liste des equipements de DA");
		System.out.println("s => Effectuer l'insertion en tant que serveur");
		System.out.println("c => Effectuer l'insertion en tant que client");
		System.out.println("a => Effectuer la synchronisation en tant que serveur");
		System.out.println("b => Effectuer la synchronisation en tant que client");
		System.out.println("r => Retour");
		System.out.println("q => Quitter");
		String answer= scan.next();
		switch (answer)
		{
		case "i":
			equipe.affichage();
			equipement(equipe);
			break;
		case "l":
			equipe.affichage_ca();
			equipement(equipe);
			break;
		case "d":
			equipe.affichage_da();
			equipement(equipe);
			break;
		case "s":
			equipementsSauf(equipe, true, true); // choisir un client / insertion
			listeEquipements();
			break;
		case "c":
			equipementsSauf(equipe, false, true); // choisir un serveur / insertion
			listeEquipements();
			break;
		case "a":
			equipementsSauf(equipe, true, false); // choisir un client / sync
			listeEquipements();
			break;
		case "b":
			equipementsSauf(equipe, false, false); // choisir un serveur / sync
			listeEquipements();
			break;
		case "r":
			listeEquipements();
			break;
		case "q":
			System.exit(0);
			break;
		default:
			System.out.println("Ce choix n'est pas permis.");
			equipement(equipe);
			break;
		}
	}
	
	private static void initOperation() throws NumberFormatException, Exception 
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("\n\t\t Gestion des équipements");
		System.out.println("l => Liste des equipements");
		System.out.println("c => Créer un équipement");
		System.out.println("q => Quitter");
		System.out.println("Entrez votre choix(lettre) : ");
		String answer= scan.next();
		switch (answer)
		{
		case "l":
			if(equipements.isEmpty())
			{
				System.out.println("Il n'y a pas d'equipement.");
				initOperation();
			}
			else
			{
				listeEquipements();
			}
			
			break;
		case "c":
			nouvelEquipement();
			break;
		case "q":
			System.exit(0);
			break;
		default:
			System.out.println("Ce choix n'est pas permis.");
			initOperation();
			break;
		}
	}
	private static void nouvelEquipement()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("\nNom de l'equipement : ");
		String nom=scan.next();
		System.out.println("Port de l'equipement : ");
		int port=0;
		boolean flag = true;
		while(flag)
		{
			try
			{
				port=  Integer.parseInt(scan.next());
				if (port<1024)
				{
					System.out.println("Le port ne peut pas être inférieur à 1024.");
					System.out.println("Port de l'equipement : ");
				}
					
				else
				{
					flag=false; // on sort de la boucle
				}
			} catch (NumberFormatException e)
			{
				System.out.println("\nErreur, le port de l'equipement doit etre un nombre.");
				System.out.println("Port de l'equipement : ");
			}
		}
		try {
			nbEquipement++;
			equipements.put(nbEquipement,new Equipement(nom, port));
			System.out.println("L'equipement "+ nom+ " a été ajouté.");
			initOperation();
		} catch (Exception e) {
			System.out.println("Erreur dans la creation de l'equipement.");
			e.printStackTrace();
		}
		
	}
	private static void listeEquipements() throws NumberFormatException, Exception
	{
		System.out.println("\n\t\tListe des equipements");
		List<String> numEquipements = new ArrayList();
		for (Entry<Integer, Equipement> entry : equipements.entrySet())
		{
			System.out.println(entry.getKey()+" => "+entry.getValue().monNom());
			numEquipements.add(Integer.toString(entry.getKey()));
		}
		System.out.println("r => Retour");
		System.out.println("q => Quitter");
		System.out.println("Entrez votre choix(numero de l'equipement ou lettre) : ");
		Scanner scan = new Scanner(System.in);
		String answer= scan.next();
		boolean flag = true;
		while(flag)
		{
			if (numEquipements.contains(answer))
			{
				flag=false;
				equipement(equipements.get(Integer.parseInt(answer)));
			}
			else
			{
				switch (answer)
				{
				case "r":
					initOperation();
					flag=false;
					break;
				case "q":
					System.exit(0);
					flag=false;
					break;
				default:
					System.out.println("Ce choix n'est pas permis.");
					listeEquipements();
					break;
				}
			}
		}
	}
	
	public static void lancer(Equipement serveur, Equipement client, boolean mode) throws Exception
	{
		ServerEquipement s =new ServerEquipement(serveur, mode);
		s.start();
		ClientEquipement c =new ClientEquipement(client, serveur.port(), mode);
		c.start();
		s.join();
		c.join();
		listeEquipements();
	}
	
	private static void equipementsSauf(Equipement equipement, boolean choixClient, boolean mode) throws NumberFormatException, Exception
	{
		if (choixClient)
		{
			System.out.println("\n\t\t Choisissez un client : ");
		}
		else
		{
			System.out.println("\n\t\t Choisissez un serveur : ");
		}
		List<String> numEquipements = new ArrayList();
		for (Entry<Integer, Equipement> entry : equipements.entrySet())
		{
			if (!(entry.getValue().equals(equipement)))
			System.out.println(entry.getKey()+" => "+entry.getValue().monNom());
			numEquipements.add(Integer.toString(entry.getKey()));
		}
		System.out.println("r => Retour");
		System.out.println("q => Quitter");
		System.out.println("Entrez votre choix(numero de l'equipement ou lettre) : ");
		Scanner scan = new Scanner(System.in);
		String answer= scan.next();
		boolean flag = true;
		while(flag)
		{
			if (numEquipements.contains(answer))
			{
				flag=false;
				if (choixClient==true)
				{
					lancer(equipement, equipements.get(Integer.parseInt(answer)), mode);
					listeEquipements();
				}
				else
				{
					lancer(equipements.get(Integer.parseInt(answer)), equipement, mode);
					listeEquipements();
				}
			}
			else
			{
				switch (answer)
				{
				case "r":
					initOperation();
					flag=false;
					break;
				case "q":
					System.exit(0);
					flag=false;
					break;
				default:
					System.out.println("Ce choix n'est pas permis.");
					listeEquipements();
					break;
				}
			}
		}
	}

}