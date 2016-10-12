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
			// Gestion des exceptions
		}
		// Attente de connextions
		try {
			NewServerSocket = serverSocket.accept();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// Creation des flux natifs et evolues
		try {
			NativeIn = NewServerSocket.getInputStream(); 
			ois = new ObjectInputStream(NativeIn); 
			NativeOut = NewServerSocket.getOutputStream(); 
			oos = new ObjectOutputStream(NativeOut);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
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
		}
		// Fermeture des flux evolues et natifs
		try {
			ois.close();
			oos.close(); NativeIn.close(); NativeOut.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		// Fermeture de la connexion
		try {
			NewServerSocket.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		// Arret du serveur
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

}
