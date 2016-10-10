import java.util.HashMap;

public class Process {
	private static final int PORT_TV = 3044;
	private static final int PORT_DVD = 3045;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initOperation();
		try {
			ServerEquipement tv = new ServerEquipement("TV_Server", PORT_TV);
			ClientEquipement dvd = new ClientEquipement("DVD_Client", PORT_DVD);
			tv.startListening(PORT_DVD);
			dvd.startSpeaking();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
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
		//HashMap<String,String> opes = new HashMap<String, String>();
		//opes.put("i", value)
	}

}
