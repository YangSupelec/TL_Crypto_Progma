import java.util.HashMap;

public class Process {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initOperation();
		try {
			Equipement dvd = new Equipement("DVD_BJ", 3040);
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
