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
	private static ServerEquipement server;
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try {
			initOperation();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			joueCommeServer(equipe);
			equipement(equipe);
			break;
		case "c":
			joueCommeClient(equipe);
			break;
		case "r":
			listeEquipements();
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
	
	private static void joueCommeClient(Equipement equipe) {
		try {
			if (server == null) {
				Scanner scan = new Scanner(System.in);
				System.out.println("\n\t\t Il n'y a pas de serveur en fonction, voulez-vous être un serveur?");
				System.out.println("o => oui");
				System.out.println("n => non et retour");
				String answer= scan.next();
				switch (answer)
				{
				case "o":
					joueCommeServer(equipe);
					break;
				case "n":
					System.out.println("Pas de serveur en fonction, commencez par en créer un.");
					listeEquipements();
					break;
				}
			} else {
				ClientEquipement client = new ClientEquipement(equipe, server.port);
				client.start();
				TimeUnit.SECONDS.sleep(1);
				equipement(equipe);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void joueCommeServer(Equipement equipement) throws Exception {
		if (server == null) {
			server = new ServerEquipement(equipement);
			server.start();
			System.out.println("\n\t\t "+equipement.monNom+" Fonctionne comme serveur en port "+equipement.monPort);
			listeEquipements();
		} else {
			Scanner scan = new Scanner(System.in);
			System.out.println("\n\t\t Il y a déjà un server "+server.equipement.monNom+" en fonction, voulez-vous le remplacer ?");
			System.out.println("o => oui");
			System.out.println("n => non et retour");
			String answer= scan.next();
			switch (answer)
			{
			case "o":
				server.stop();
				server.join();
				server = null;
				joueCommeServer(equipement);
				listeEquipements();
				break;
			case "n":
				equipement(equipement);
				break;
			}
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

}