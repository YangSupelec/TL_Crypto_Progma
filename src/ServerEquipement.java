import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

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
		// Creation de socket (TCP)
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
		this.startListening(this.mode);
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

	public void startListening(boolean mode) {
		NomPubKey client=null;
		HashSet<NomPubKey> dacaClient=null;
		HashSet<NomPubKey> dacaServeur=null;
		boolean flagServeur=false;
		boolean flagClient=false;
		if (mode==true)
		{
			// Reception du certificat du client
			try {
				client =  (NomPubKey) ois.readObject(); 
				System.out.println("Le serveur a recu le certificat du client.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission du certificat
			try {
				oos.writeObject(this.equipement.mesInfos()); 
				oos.flush();
				System.out.println("Les infos du serveur est envoyé.");
				} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission d’un certificat sur la clé publique du client
			try {
				Certificat certifSdeC = new Certificat(this.equipement.monNom(), client.monNom(), client.maClePub(), this.equipement.maClePriv(), 10);
				oos.writeObject(certifSdeC); 
				oos.flush();
				System.out.println("Le certificat du serveur certifiant la clé publique du client est envoyé");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Reception d'un certificat
			try {
				Certificat res = (Certificat) ois.readObject(); 
				System.out.println("Le serveur a reçu le certificat.");
				if(res.verifCertif(client.maClePub()))
				{
					System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
					this.equipement.ajoutCA(client);
					this.equipement.supprimerDA(client);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission du DA/CA
			try {
				dacaServeur = new HashSet<NomPubKey>();
				dacaServeur.addAll(this.equipement.monCA());
				dacaServeur.addAll(this.equipement.monDA());
				oos.writeObject(dacaServeur); 
				oos.flush();
				System.out.println("Le DA/CA du serveur est envoyé.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Reception du DA/CA
			try {
				dacaClient = (HashSet<NomPubKey>) ois.readObject(); 
				System.out.println("Le serveur a recu le DA/CA du client.");
				this.equipement.sync(dacaClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			// Reception du DA certificat du client
			try {
				dacaClient =  (HashSet<NomPubKey>) ois.readObject(); 
				System.out.println("Le serveur a recu le DA du client.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean test = false;
			for(NomPubKey inf : dacaClient)
			{
				if(inf.equals(this.equipement.mesInfos()))
				{
					test=true;
				}
			}
			if (test)
			{
				flagServeur=true;
				// Emission du flag
				try {
					oos.writeObject(flagServeur); 
					oos.flush();
					System.out.println("Le flag du serveur est envoyé.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.mode=true;
				this.startListening(true);
			}
			else
			{
				flagServeur=false;
				try {
					oos.writeObject(flagServeur); 
					oos.flush();
					System.out.println("Le flag du serveur est envoyé.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					dacaServeur = new HashSet<NomPubKey>();
					dacaServeur.addAll(this.equipement.monCA());
					dacaServeur.addAll(this.equipement.monDA());
					oos.writeObject(dacaServeur); 
					oos.flush();
					System.out.println("Le DA du serveur est envoyé.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Reception du flag du client
				try {
					flagClient =  (Boolean) ois.readObject(); 
					System.out.println("Le client a recu le flag du serveur.");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (flagClient==true)
				{
					this.mode=true;
					this.startListening(true);
				}
				else
				{
					System.out.println("\n\t La synchronisation impossible, aucun des équipements n'appartient au DA de l'autre.");
				}
			}
			
			
		}
	}

}