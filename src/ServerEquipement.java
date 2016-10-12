import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

<<<<<<< HEAD
public class ServerEquipement extends Thread{
=======
public class ServerEquipement extends Equipement{
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0

	ServerSocket serverSocket = null; 
	Socket NewServerSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
<<<<<<< HEAD
	Equipement equipement;
	int port;
	
	ServerEquipement(Equipement equipement) throws Exception {
		this.equipement = equipement;
		this.port = equipement.port();
		// TODO Auto-generated constructor stub
	}
	public void run()
	{
		this.startListening();
		
	}

	public void startListening() {
		// Creation de socket (TCP)
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
			
=======
	
	ServerEquipement(String nom, int port) throws Exception {
		super(nom, port);
		// TODO Auto-generated constructor stub
	}

	public void startListening(int port) {
		// Creation de socket (TCP)
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// Gestion des exceptions
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Attente de connextions
		try {
			NewServerSocket = serverSocket.accept();
		} catch (Exception e) {
<<<<<<< HEAD
			e.printStackTrace();
=======
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Creation des flux natifs et evolues
		try {
			NativeIn = NewServerSocket.getInputStream(); 
			ois = new ObjectInputStream(NativeIn); 
			NativeOut = NewServerSocket.getOutputStream(); 
			oos = new ObjectOutputStream(NativeOut);
		} catch (IOException e) {
<<<<<<< HEAD
			e.printStackTrace();
=======
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
<<<<<<< HEAD
			System.out.println("Reception Serveur : "+res);
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
			System.out.println(res);
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
		// Fermeture des flux evolues et natifs
		try {
			ois.close();
<<<<<<< HEAD
			oos.close(); 
			NativeIn.close(); 
			NativeOut.close();
		} catch (IOException e) {
			e.printStackTrace();
=======
			oos.close(); NativeIn.close(); NativeOut.close();
		} catch (IOException e) {
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Fermeture de la connexion
		try {
			NewServerSocket.close();
		} catch (IOException e) {
<<<<<<< HEAD
			e.printStackTrace();
=======
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
		// Arret du serveur
		try {
			serverSocket.close();
		} catch (IOException e) {
<<<<<<< HEAD
			e.printStackTrace();
=======
			System.out.println(e.toString());
>>>>>>> ee50c048d5b06aa1b2ca83f884bc4a757dcd98a0
		}
	}

}
