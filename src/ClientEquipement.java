import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ClientEquipement extends Thread{

	int ServerPort;
	String ServerName;
	Socket clientSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
	Equipement equipement;
	boolean mode; // true pour insertion, false pour synchronisation

	ClientEquipement(Equipement equipement, int serveurPort, boolean mode) throws Exception {
		this.equipement = equipement;
		this.ServerPort = serveurPort;
		this.mode = mode;
	}
	public void run()
	{
		// Creation de socket (TCP)
		
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
		this.startSpeaking(this.mode);
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
	

	public void startSpeaking(boolean mode) {
		NomPubKey serveur =null;
		boolean flagServeur = false;
		boolean flagClient = false;
		HashSet<NomPubKey> dacaServeur=null;
		HashSet<NomPubKey> dacaClient=null;
		if (mode==true) // insertion
		{
			// Emission du certificat du client
			try {
				oos.writeObject(this.equipement.mesInfos()); 
				oos.flush();
				System.out.println("Le certificat du client est envoyé.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Reception du certificat du serveur
			try {
				serveur =  (NomPubKey) ois.readObject(); 
				System.out.println("Le client a recu le certificat du serveur.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Reception d’un certificat
			try {
				Certificat res = (Certificat) ois.readObject(); 
				System.out.println("Le client a reçu le certificat.");
				if(res.verifCertif(serveur.maClePub()))
				{
					System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
					this.equipement.ajoutCA(serveur);
					this.equipement.supprimerDA(serveur);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission d’un certificat sur la clé publique du serveur
			try {
				Certificat certifCdeS = new Certificat(this.equipement.monNom(), serveur.monNom(), serveur.maClePub(), this.equipement.maClePriv(), 10);
				oos.writeObject(certifCdeS); 
				oos.flush();
				System.out.println("Le certificat du client certifiant la clé publique du serveur est envoyé");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Reception du DA/CA
			try {
				dacaServeur = (HashSet<NomPubKey>) ois.readObject(); 
				System.out.println("Le client a recu le DA/CA du serveur.");
				this.equipement.sync(dacaServeur);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission du DA/CA
			try {
				dacaClient = new HashSet<NomPubKey>();
				dacaClient.addAll(this.equipement.monCA());
				dacaClient.addAll(this.equipement.monDA());
				oos.writeObject(dacaClient); 
				oos.flush();
				System.out.println("Le DA/CA du client est envoyé.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		else
		{
			// Emission du DA du client
			try {
				dacaClient = new HashSet<NomPubKey>();
				dacaClient.addAll(this.equipement.monCA());
				dacaClient.addAll(this.equipement.monDA());
				oos.writeObject(dacaClient); 
				oos.flush();
				System.out.println("Le DA du client est envoyé.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Reception du flagdu serveur
			try {
				flagServeur =  (Boolean) ois.readObject(); 
				System.out.println("Le client a recu le flag du serveur.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (flagServeur == true)
			{
				this.mode=true;
				this.startSpeaking(true);
			}
			else
			{
				// Reception du DA certificat du serveur
				try {
					dacaServeur =  (HashSet<NomPubKey>) ois.readObject(); 
					System.out.println("Le serveur a recu le DA du client.");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				boolean test = false;
				for(NomPubKey inf : dacaServeur)
				{
					if(inf.equals(this.equipement.mesInfos()))
					{
						test=true;
					}
				}
				if (test)
				{
					flagClient=true;
					try {
						oos.writeObject(flagClient); 
						oos.flush();
						System.out.println("Le flag du client est envoyé.");
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.mode=true;
					this.startSpeaking(true);
				}
				else
				{
					flagClient=false;
					try {
						oos.writeObject(flagClient); 
						oos.flush();
						System.out.println("Le flag du client est envoyé.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
}