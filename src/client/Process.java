package client;

public class Process {
	private static final int PORT_DVD = 3041;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initOperation();
		try {
			ClientEquipement dvd = new ClientEquipement("DVD_Client", PORT_DVD, "localhost");
			System.out.println("------------------------------");
			dvd.startSpeaking();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
