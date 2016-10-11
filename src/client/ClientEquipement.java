package client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

<<<<<<< HEAD:src/client/ClientEquipement.java
import core.Equipement;

public class ClientEquipement extends Equipement{
=======
public class ClientEquipement extends Thread{
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/ClientEquipement.java

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
<<<<<<< HEAD:src/client/ClientEquipement.java

	ClientEquipement(String nom, int port, String serverName) throws Exception {
		this(nom, port);
		this.ServerName = serverName;
=======
	public void run()
	{
		this.startSpeaking();
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/ClientEquipement.java
	}
	

	public void startSpeaking() {
<<<<<<< HEAD:src/client/ClientEquipement.java
		while (true) {    
			try {  
				// Creation de socket (TCP)
				clientSocket = new Socket(ServerName, ServerPort);    

				// data received from server   
				DataInputStream input = new DataInputStream(clientSocket.getInputStream());    
				// send to server    
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());    
				System.out.print("please input : \t");    
				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();    
				out.writeUTF(str);    

				String ret = input.readUTF();     
				System.out.println("content replyed from server : " + ret);    
				// close connection when "OK" receiveed    
				if ("OK".equals(ret)) {    
					System.out.println("client will close connection");    
					Thread.sleep(500);    
					break;    
				}    

				out.close();  
				input.close();  
			} catch (Exception e) {  
				System.out.println("client exception :" + e.getMessage());   
			} finally {  
				if (clientSocket != null) {  
					try {  
						clientSocket.close();  
					} catch (IOException e) {  
						clientSocket = null;   
						System.out.println("client finally exception :" + e.getMessage());   
					}  
				}  
			} 
=======
		// Creation de socket (TCP) 
		try {
			clientSocket = new Socket(ServerName,ServerPort);
		} 
		catch (Exception e) {
			e.printStackTrace();
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/ClientEquipement.java
		}
	}
		// Creation des flux natifs et evolues
<<<<<<< HEAD:src/client/ClientEquipement.java
		/*try {
			NativeOut = clientSocket.getOutputStream(); oos = new ObjectOutputStream(NativeOut); NativeIn = clientSocket.getInputStream(); ois = new ObjectInputStream(NativeIn);
=======
		try {
			NativeOut = clientSocket.getOutputStream(); 
			oos = new ObjectOutputStream(NativeOut); 
			NativeIn = clientSocket.getInputStream(); 
			ois = new ObjectInputStream(NativeIn);
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/ClientEquipement.java
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Emission d’un String
		try {
			oos.writeObject(this.equipement.monNom()); 
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
			System.out.println("Reception Client : "+res);
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
<<<<<<< HEAD:src/client/ClientEquipement.java
			System.out.println(e.toString());
		}*/
}
=======
			e.printStackTrace();
		}
	}
}
>>>>>>> 3a36c70121eca4083a21c55c06fcf49a0e1a1c27:src/ClientEquipement.java
