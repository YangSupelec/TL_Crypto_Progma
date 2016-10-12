import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

<<<<<<< HEAD
public class ClientEquipement extends Thread{
=======
public class ClientEquipement extends Equipement{
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0

	int ServerPort;
	String ServerName;
	Socket clientSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
<<<<<<< HEAD
	Equipement equipement;

	ClientEquipement(Equipement equipement, int serveurPort) throws Exception {
		this.equipement = equipement;
		this.ServerPort = serveurPort;
	}
	public void run()
	{
		this.startSpeaking();
	}
	
=======

	ClientEquipement(String nom, int port) throws Exception {
		super(nom, port);
		this.ServerPort = port;
	}
	
	ClientEquipement(String nom, int port, String serverName) throws Exception {
		this(nom, port);
		this.ServerName = serverName;
	}
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0

	public void startSpeaking() {
		// Creation de socket (TCP) 
		try {
<<<<<<< HEAD
			clientSocket = new Socket(ServerName,ServerPort);
		} 
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
			oos.writeObject(this.equipement.monNom()); 
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
=======
			clientSocket = new Socket(ServerName,ServerPort); } 
		catch (Exception e) {
			System.out.println(e.toString());
		}
		// Creation des flux natifs et evolues
		try {
			NativeOut = clientSocket.getOutputStream(); oos = new ObjectOutputStream(NativeOut); NativeIn = clientSocket.getInputStream(); ois = new ObjectInputStream(NativeIn);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// Emission d’un String
		try {
			oos.writeObject(this.monNom); 
			oos.flush();
		} catch (Exception e) {
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
<<<<<<< HEAD
			System.out.println("Reception Client : "+res);
		} catch (Exception e) {
			e.printStackTrace();
=======
			System.out.println(res);
		} catch (Exception e) {
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Fermeture des flux evolues et natifs
		try {
			ois.close();
			oos.close(); 
			NativeIn.close(); 
			NativeOut.close();
		} catch (IOException e) {
<<<<<<< HEAD
			e.printStackTrace();
=======
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Fermeture de la connexion
		try {
			clientSocket.close(); 
		} catch (IOException e) {
<<<<<<< HEAD
			e.printStackTrace();
=======
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
	}
}
