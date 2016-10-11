<<<<<<< HEAD:src/client/Process.java
package client;
=======
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/Process.java

public class Process {
	private static final int PORT_DVD = 3041;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initOperation();
		try {
<<<<<<< HEAD:src/client/Process.java
			ClientEquipement dvd = new ClientEquipement("DVD_Client", PORT_DVD, "localhost");
			System.out.println("------------------------------");
			dvd.startSpeaking();
=======
			Equipement tv = new Equipement("TV_Server", PORT_TV);
			Equipement dvd = new Equipement("DVD_Client", PORT_DVD);
			ServerEquipement s_tv = new ServerEquipement(tv);
			ClientEquipement c_dvd = new ClientEquipement(dvd, PORT_TV);
			
			s_tv.start();
			TimeUnit.SECONDS.sleep(1);
			c_dvd.start();
			//tv.startListening(PORT_DVD);
			//dvd.startSpeaking();
			
			
			
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/Process.java
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
<<<<<<< HEAD:src/client/Process.java
=======
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
		//HashMap<String,String> opes = new HashMap<String, String>();
		//opes.put("i", value)
	}

>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/Process.java
}
