import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class ServerEquipement extends Thread{

	ServerSocket serverSocket = null; 
	Socket NewServerSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;
	Equipement equipement;
	int port;
	boolean mode;// true pour insertion, false pour synchronisation uniquement
	InfoEquipement client=null;
	HashSet<InfoEquipement> dacaClient=null;
	HashSet<InfoEquipement> dacaServeur=null;
	int nServeur=0;
	int nClient=0;
	KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	SecretKey secretKey = null;
	
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
		this.startListening();
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
	public boolean syncPossible()
	{
		boolean flagServeur=false;
		boolean flagClient=false;
		flagServeur=this.equipement.estDansDACA(client);
			// Emission du flag
			try {
				Cipher aesCipher = Cipher.getInstance("AES");
				aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
				SealedObject ciphered = new SealedObject(flagServeur, aesCipher);
				
				oos.writeObject(ciphered); // envoie un boolean true au client pour l'informer que la synchronisation est possible
				oos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (flagServeur==true)
			{
				return true;
			}
			else
			{
				// Reception du flag du client
				try {
					SealedObject ciphered =(SealedObject) ois.readObject();
					Cipher aesCipher = Cipher.getInstance("AES");
					aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
					flagClient =  (Boolean) ciphered.getObject(secretKey);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(flagClient==true)
				{
					return true;
				}
				else
				{
					System.out.println("\n\t La synchronisation est impossible, aucun des équipements n'appartient au DA ou CA de l'autre.");
					return false;
				}
			}
	}
	public void startListening() 
	{
		// Reception du nom et de la clé publique du client
		try {
			client =  (InfoEquipement) ois.readObject(); 
			System.out.println("Le serveur a recu la clé publique du client.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission du nom et de la clé publique du serveur
		try {
			oos.writeObject(this.equipement.mesInfos()); 
			oos.flush();
			System.out.println("La clé publique du serveur est envoyé.");
			} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission de nServeur
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
		    rsaCipher.init(Cipher.ENCRYPT_MODE, client.maClePub());
			Random rn = new Random();
			nServeur = rn.nextInt(1000000000);
			byte[] cipherNumber = rsaCipher.doFinal(Integer.toString(nServeur).getBytes());
			oos.writeObject(cipherNumber); 
			oos.flush();
			System.out.println("Le nombre aléatoire du serveur est envoyé.");
			} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception de nClient
			try {
				byte [] cipherNumber =  (byte[]) ois.readObject(); 
				Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
				rsaCipher.init(Cipher.DECRYPT_MODE, this.equipement.maClePriv());
				byte[] dectyptedText = rsaCipher.doFinal(cipherNumber);
				nClient=Integer.parseInt(new String(dectyptedText));
				System.out.println("Le serveur a recu le nombre aléatoire du client.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		if(nClient==nServeur+1)
		{
			try {
				Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			    rsaCipher.init(Cipher.ENCRYPT_MODE, client.maClePub());
			    byte[] cipherNumber = rsaCipher.doFinal(Integer.toString(1).getBytes());
				oos.writeObject(cipherNumber); 
				oos.flush();
				System.out.println();
				} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission de la clé de session
			try
			{
				SecureRandom secRan = new SecureRandom();
				byte[] bytes = new byte[128];
				secRan.nextBytes(bytes);
				keyGen.init(secRan);
				secretKey = keyGen.generateKey();
				Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			    rsaCipher.init(Cipher.WRAP_MODE, client.maClePub());
		        byte[] wrapped = rsaCipher.wrap(secretKey);
				oos.writeObject(wrapped); 
				oos.flush();
				System.out.println("La clé publique du serveur est envoyé.");
				} catch (Exception e) {
				e.printStackTrace();
			}
			if(this.mode==true || this.syncPossible())
			{
				// Emission d’un certificat sur la clé publique du client
				try {
					Certificat certifSdeC = new Certificat(this.equipement.monNom(), client.monNom(), client.maClePub(), this.equipement.maClePriv(), 10);
					Cipher aesCipher = Cipher.getInstance("AES");
					aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
					SealedObject ciphered = new SealedObject(certifSdeC, aesCipher);
					oos.writeObject(ciphered); 
					oos.flush();
					System.out.println("Le certificat du serveur certifiant la clé publique du client est envoyé");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Reception d'un certificat
				try {
					SealedObject cipheredCert =(SealedObject) ois.readObject();
					Cipher aesCipherCert = Cipher.getInstance("AES");
					aesCipherCert.init(Cipher.ENCRYPT_MODE, secretKey);
					Certificat res = (Certificat) cipheredCert.getObject(secretKey);
					System.out.println("Le serveur a reçu le certificat.");
					if(res.verifCertif(client.maClePub()))
					{
						System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
						this.equipement.ajoutCA(client);
						this.equipement.supprimerDA(client);
						
						
						// Emission du DA/CA
						try {
							dacaServeur = new HashSet<InfoEquipement>();
							dacaServeur.addAll(this.equipement.monCA());
							dacaServeur.addAll(this.equipement.monDA());
							
							
							Cipher aesCipher = Cipher.getInstance("AES");
							aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
							SealedObject ciphered = new SealedObject(dacaServeur, aesCipher);
							
							aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
							HashSet<InfoEquipement> resultat = (HashSet<InfoEquipement>) ciphered.getObject(secretKey);
							
							oos.writeObject(ciphered); 
							oos.flush();
							System.out.println("Le DA/CA du serveur est envoyé.");
						} catch (Exception e) {
							e.printStackTrace();
						}
						// Reception du DA/CA
						try {
							SealedObject ciphered =(SealedObject) ois.readObject();
							Cipher aesCipher = Cipher.getInstance("AES");
							aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
							dacaClient = (HashSet<InfoEquipement>) ciphered.getObject(secretKey);
							System.out.println("Le serveur a recu le DA/CA du client, synchronisation.");
							System.out.println("Fin.");
							this.equipement.sync(dacaClient); // synchronisation du serveur avec le DA/CA du client
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}
		else
		{
			try {
				Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			    rsaCipher.init(Cipher.ENCRYPT_MODE, client.maClePub());
			    byte[] cipherNumber = rsaCipher.doFinal(Integer.toString(0).getBytes());
				oos.writeObject(cipherNumber); 
				oos.flush();
				System.out.println();
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

}