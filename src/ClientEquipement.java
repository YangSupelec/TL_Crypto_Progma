import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.HashSet;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

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
	InfoEquipement serveur =null;
	HashSet<InfoEquipement> dacaServeur=null;
	HashSet<InfoEquipement> dacaClient=null;
	SecureRandom nServeur=null;
	SecureRandom nClient=null;
	KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	SecretKey secretKey = null;
	
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
		this.startSpeaking();
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
	
	public boolean syncPossible()
	{
		boolean flagServeur = false;
		boolean flagClient = false;
		// Reception du flag du serveur
			try {
				SealedObject ciphered =(SealedObject) ois.readObject();
				Cipher aesCipher = Cipher.getInstance("AES");
				aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
				flagServeur =  (Boolean) ciphered.getObject(secretKey);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (flagServeur==true)
			{
				return true;
			}
			else
			{
				flagClient=this.equipement.estDansDACA(serveur);
				try {
					Cipher aesCipher = Cipher.getInstance("AES");
					aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
					SealedObject ciphered = new SealedObject(flagClient, aesCipher);
					
					oos.writeObject(ciphered);
					oos.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(flagClient==true)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
	}
	
	public void startSpeaking() 
	{
		// Emission de la clé publique du client
		try {
			oos.writeObject(this.equipement.mesInfos()); 
			oos.flush();
			System.out.println("La clé publique du client est envoyée.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception de la clé publique du serveur
		try {
			serveur =  (InfoEquipement) ois.readObject(); 
			System.out.println("Le client a recu la clé publique du serveur.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception de nServeur
		try {
			nServeur =  (SecureRandom) ois.readObject(); 
			System.out.println("Le client a recu nServeur.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission de nClient
		try {
			nClient = new SecureRandom();
			byte[] bytes = new byte[128];
			nClient.nextBytes(bytes);
			oos.writeObject(nClient); 
			oos.flush();
			System.out.println("Le nombre aléatoire du client est envoyé.");
			} catch (Exception e) {
			e.printStackTrace();
		}
		// Réception de la clé de session
		try {
			byte[] wrapped =(byte[]) ois.readObject();
			Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			rsaCipher.init(Cipher.UNWRAP_MODE, this.equipement.maClePriv());
			secretKey=(SecretKey) rsaCipher.unwrap(wrapped, "AES", Cipher.SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(this.mode==true || this.syncPossible())
		{
			// Reception d’un certificat
			try {
				SealedObject cipheredCert =(SealedObject) ois.readObject();
				Cipher aesCipherCert = Cipher.getInstance("AES");
				aesCipherCert.init(Cipher.ENCRYPT_MODE, secretKey);
				Certificat res = (Certificat) cipheredCert.getObject(secretKey);
				System.out.println("Le client a reçu le certificat.");
				if(res.verifCertif(serveur.maClePub()))
				{
					System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
					this.equipement.ajoutCA(serveur);
					this.equipement.supprimerDA(serveur);
					// Emission d’un certificat sur la clé publique du serveur
					try {
						Certificat certifCdeS = new Certificat(this.equipement.monNom(), serveur.monNom(), serveur.maClePub(), this.equipement.maClePriv(), 10);
						Cipher aesCipher = Cipher.getInstance("AES");
						aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
						SealedObject ciphered = new SealedObject(certifCdeS, aesCipher);
						oos.writeObject(ciphered); 
						oos.flush();
						System.out.println("Le certificat du client certifiant la clé publique du serveur est envoyé");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					// Reception du DA/CA
					try {
						SealedObject ciphered =(SealedObject) ois.readObject();
						Cipher aesCipher = Cipher.getInstance("AES");
						aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
						dacaServeur = (HashSet<InfoEquipement>) ciphered.getObject(secretKey);
						System.out.println("Le client a recu le DA/CA du serveur.");
						this.equipement.sync(dacaServeur);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// Emission du DA/CA
					try {
						dacaClient = new HashSet<InfoEquipement>();
						dacaClient.addAll(this.equipement.monCA());
						dacaClient.addAll(this.equipement.monDA());
						
						Cipher aesCipher = Cipher.getInstance("AES");
						aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
						SealedObject ciphered = new SealedObject(dacaClient, aesCipher);
						
						oos.writeObject(ciphered); 
						oos.flush();
						System.out.println("Le DA/CA du client est envoyé.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}