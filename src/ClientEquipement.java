import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientEquipement extends Equipement{

	int ServerPort;
	String ServerName;
	Socket clientSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;

	ClientEquipement(String nom, int port) throws Exception {
		super(nom, port);
		this.ServerPort = port;
	}
	
	ClientEquipement(String nom, int port, String serverName) throws Exception {
		this(nom, port);
		this.ServerName = serverName;
	}

	public void startSpeaking() {
		// Creation de socket (TCP) 
		try {
			clientSocket = new Socket(ServerName,ServerPort); } 
		catch (Exception e) {
			e.printStackTrace();
		}
		// Creation des flux natifs et evolues
		try {
			NativeOut = clientSocket.getOutputStream(); 
			oos = new ObjectOutputStream(NativeOut); 
			NativeIn = clientSocket.getInputStream(); 
			ois = new ObjectInputStream(NativeIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission d’un String
		try {
			oos.writeObject(this.monNom); 
			System.out.println("le client envoie son nom.");
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Fermeture des flux evolues et natifs
		try {
			ois.close();
			oos.close(); 
			NativeIn.close(); 
			NativeOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Fermeture de la connexion
		try {
			clientSocket.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
