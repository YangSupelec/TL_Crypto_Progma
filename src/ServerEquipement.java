import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEquipement extends Thread{

	ServerSocket serverSocket = null; 
	Socket NewServerSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
	Equipement equipement;
	int port;
	boolean mode;// true pour insertion, false pour synchronisation
	
	ServerEquipement(Equipement equipement, boolean mode) throws Exception {
		this.equipement = equipement;
		this.port = equipement.port();
		this.mode=mode;
		// TODO Auto-generated constructor stub
	}
	public void run()
	{
		this.startListening();
		
	}

	public void startListening() {
		if (mode==true)
		{
			// Creation de socket (TCP)
			Certificat client=null;
			try {
				serverSocket = new ServerSocket(this.port);
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
			// Reception du certificat du client
			try {
				client =  (Certificat) ois.readObject(); 
				System.out.println("Le serveur a recu le certificat du client.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission du certificat
			try {
				oos.writeObject(this.equipement.monCertif()); 
				oos.flush();
				System.out.println("Le certificat du serveur est envoyé.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission d’un certificat sur la clé publique du client
			try {
				Certificat certifSdeC = new Certificat(this.equipement.monNom(), client.x509.getSubjectDN().getName().substring(3, client.x509.getSubjectDN().getName().length()), client.x509.getPublicKey(), this.equipement.maClePriv(), 10);
				oos.writeObject(certifSdeC); 
				oos.flush();
				System.out.println("Le certificat du serveur certifiant la clé publique du client est envoyé");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Certificat res = (Certificat) ois.readObject(); 
				System.out.println("Le client a reçu le certificat.");
				if(res.verifCertif(client.x509.getPublicKey()))
				{
					System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
					this.equipement.ajoutCA(res);
				}
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
		else
		{
			
		}
	}

}