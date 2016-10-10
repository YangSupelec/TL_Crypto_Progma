import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEquipement extends Equipement{

	ServerSocket serverSocket = null; 
	Socket NewServerSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
	
	ServerEquipement(String nom, int port) throws Exception {
		super(nom, port);
		// TODO Auto-generated constructor stub
	}

	public void startListening(int port) {
		// Creation de socket (TCP)
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		// Attente de connextions
		try {
			NewServerSocket = serverSocket.accept();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Creation des flux natifs et evolues
		try {
			NativeIn = NewServerSocket.getInputStream(); 
			ois = new ObjectInputStream(NativeIn); 
			NativeOut = NewServerSocket.getOutputStream(); 
			oos = new ObjectOutputStream(NativeOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
			System.out.println(res);
			System.out.println("le serveur a recu le client" +res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission d’un String
		try {
			oos.writeObject(this.monNom); 
			oos.flush();
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
			NewServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Arret du serveur
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
