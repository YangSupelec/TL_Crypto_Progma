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
		this.startListening();
		
	}

	public void startListening() {
		if (mode==true)
		{
			// Creation de socket (TCP)
			Certificat client=null;
			HashSet<Certificat> dacaClient=null;
			HashSet<Certificat> dacaServeur=null;
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
			// Reception d'un certificat
			try {
				Certificat res = (Certificat) ois.readObject(); 
				System.out.println("Le serveur a reçu le certificat.");
				if(res.verifCertif(client.x509.getPublicKey()))
				{
					System.out.println("Le serveur a bien certifié la clé publique du client, le client ajoute le serveur à son CA");
					this.equipement.ajoutCA(client);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Emission du DA/CA
			try {
				dacaServeur = new HashSet<Certificat>();
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
				dacaClient = (HashSet<Certificat>) ois.readObject(); 
				System.out.println("Le serveur a recu le DA/CA du client.");
				this.equipement.sync(dacaClient);
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
			Certificat client=null;
			HashSet<Certificat> dacaClient=null;
			HashSet<Certificat> dacaServeur=null;
			HashSet<Certificat> daClient=null;
			boolean flagServeur=false;
			boolean flagClient=false;
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
			// Reception du DA certificat du client
			try {
				daClient =  (HashSet<Certificat>) ois.readObject(); 
				System.out.println("Le serveur a recu le DA du client.");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean deja =false;
			for (Certificat certifDA : daClient)
			{
				if(this.equipement.monCertif().equals(certifDA))
				{
					deja=true;
				}
			}
			if (deja==true)
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
				// Reception d'un certificat
				try {
					Certificat res = (Certificat) ois.readObject(); 
					System.out.println("Le serveur a reçu le certificat.");
					if(res.verifCertif(client.x509.getPublicKey()))
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
					dacaServeur = this.equipement.monCA();
					dacaServeur.addAll(this.equipement.monDA());
					oos.writeObject(dacaServeur); 
					oos.flush();
					System.out.println("Le DA/CA du serveur est envoyé.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Reception du DA/CA
				try {
					dacaClient = (HashSet<Certificat>) ois.readObject(); 
					System.out.println("Le serveur a recu le DA/CA du client.");
					this.equipement.sync(dacaClient);
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
				flagServeur=false;
				try {
					oos.writeObject(flagServeur); 
					oos.flush();
					System.out.println("Le flag du serveur est envoyé.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					oos.writeObject(this.equipement.monDA()); 
					oos.flush();
					System.out.println("Le DA du serveur est envoyé.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Reception du flagdu client
				try {
					flagClient =  (Boolean) ois.readObject(); 
					System.out.println("Le client a recu le flag du serveur.");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (flagClient==true)
				{
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
					// Reception d'un certificat
					try {
						Certificat res = (Certificat) ois.readObject(); 
						System.out.println("Le serveur a reçu le certificat.");
						if(res.verifCertif(client.x509.getPublicKey()))
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
						dacaServeur = this.equipement.monCA();
						dacaServeur.addAll(this.equipement.monDA());
						oos.writeObject(dacaServeur); 
						oos.flush();
						System.out.println("Le DA/CA du serveur est envoyé.");
					} catch (Exception e) {
						e.printStackTrace();
					}
					// Reception du DA/CA
					try {
						dacaClient = (HashSet<Certificat>) ois.readObject(); 
						System.out.println("Le serveur a recu le DA/CA du client.");
						this.equipement.sync(dacaClient);
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
					System.out.println("\n\t La synchronisation impossible, aucun des équipements n'appartient au DA de l'autre.");
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
			
			
		}
	}

}