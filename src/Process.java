import java.util.HashMap;

public class Process {
	private static final int PORT_TV = 3040;
	private static final int PORT_DVD = 3041;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initOperation();
		try {
			ServerEquipement tv = new ServerEquipement("TV_Server", PORT_TV);
			ClientEquipement dvd = new ClientEquipement("DVD_Client", PORT_DVD,"localhost");
			tv.startListening(PORT_DVD);
			dvd.startSpeaking();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private static void initOperation() {
		// TODO Auto-generated method stub
		HashMap<String,String> opes = new HashMap<String, String>();
		//opes.put("i", value)
	}

}
