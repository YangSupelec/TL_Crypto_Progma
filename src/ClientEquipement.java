import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientEquipement extends Thread{

	int ServerPort;
	String ServerName;
	Socket clientSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
	Equipement equipement;

	ClientEquipement(Equipement equipement, int serveurPort) throws Exception {
		this.equipement = equipement;
		this.ServerPort = serveurPort;
	}
	public void run()
	{
		this.startSpeaking();
	}
	

	public void startSpeaking() {
		// Creation de socket (TCP)
		Certificat serveur =null;
		try {
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
		// Emission du certificat du client
		try {
			oos.writeObject(this.equipement.monCertif()); 
			oos.flush();
			System.out.println("Le certificat du client est envoyé.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception du certificat du serveur
		try {
			serveur =  (Certificat) ois.readObject(); 
			System.out.println("Le client a recu le certificat du serveur.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception d’un certificat
		try {
			Certificat res = (Certificat) ois.readObject(); 
			System.out.println("Le client a reçu le certificat.");
			if(res.verifCertif(serveur.x509.getPublicKey()))
			{
				System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
				this.equipement.ajoutCA(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission d’un certificat sur la clé publique du serveur
		try {
			Certificat certifCdeS = new Certificat(this.equipement.monNom(), serveur.x509.getSubjectDN().getName().substring(3, serveur.x509.getSubjectDN().getName().length()), serveur.x509.getPublicKey(), this.equipement.maClePriv(), 10);
			oos.writeObject(certifCdeS); 
			oos.flush();
			System.out.println("Le certificat du client certifiant la clé publique du serveur est envoyé");
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