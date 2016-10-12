package server;
public class ServerProcess {
	private static final int PORT_TV = 3040;
	private static final int PORT_DVD = 3041;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerEquipement tv = new ServerEquipement("TV_Server", PORT_TV);
			System.out.println("------------------------------");
			tv.init(PORT_DVD);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
