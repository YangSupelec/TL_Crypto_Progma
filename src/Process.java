import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Process {
	private static final int PORT_TV = 3040;
	private static final int PORT_DVD = 3041;
	private static HashMap<Integer, Equipement> equipements= new HashMap<Integer, Equipement>();
	private static Integer nbEquipement;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String answer;
		Scanner scan = new Scanner(System.in);
		initOperation();
		answer = scan.next();
		switch (answer)
		{
		case "i":
			if(equipements.isEmpty())
			{
				System.out.println("Il n'y a pas d'equipement.");
			}
			
			
		default:
			System.out.println("Ce choix n'est pas permis.");
			initOperation();
			
		}
//		try {
//			Equipement tv = new Equipement("TV_Server", PORT_TV);
//			Equipement dvd = new Equipement("DVD_Client", PORT_DVD);
//			ServerEquipement s_tv = new ServerEquipement(tv);
//			ClientEquipement c_dvd = new ClientEquipement(dvd, PORT_TV);
//			
//			s_tv.start();
//			TimeUnit.SECONDS.sleep(1);
//			c_dvd.start();
//			//tv.startListening(PORT_DVD);
//			//dvd.startSpeaking();
//			
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	private static void equipement(String nom)
	{
		System.out.println("\t\t Gestion de l'equipement : "+nom);
		System.out.println("i => Informations concernant l'equipement");
		System.out.println("l => Liste des equipements CA");
		System.out.println("d => Liste des equipements de DA");
		System.out.println("s => Effectuer l'insertion en tant que serveur");
		System.out.println("c => Effectuer l'insertion en tant que client");
		System.out.println("r => Retour");
		System.out.println("q => Quitter");
	}

	private static void initOperation() 
	{
		// TODO Auto-generated method stub
		System.out.println("\t\t Gestion des équipements");
		System.out.println("i => Liste des equipements");
		System.out.println("c => Créer un équipement client");
		System.out.println("s => Créer un équipement serveur");
		System.out.println("q => Quitter");
		System.out.println("Entrez votre choix(lettre) : ");
		//opes.put("i", value)
	}

}
