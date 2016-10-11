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

import core.Equipement;

public class ClientEquipement extends Equipement{

	int ServerPort;
	String ServerName;
	Socket clientSocket = null; 
	InputStream NativeIn = null; 
	ObjectInputStream ois = null; 
	OutputStream NativeOut = null; 
	ObjectOutputStream oos = null;

	ClientEquipement(String nom, int port) throws Exception {
		super(nom, port);
		this.ServerPort = port;
	}

	ClientEquipement(String nom, int port, String serverName) throws Exception {
		this(nom, port);
		this.ServerName = serverName;
	}

	public void startSpeaking() {
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
		}
	}
		// Creation des flux natifs et evolues
		/*try {
			NativeOut = clientSocket.getOutputStream(); oos = new ObjectOutputStream(NativeOut); NativeIn = clientSocket.getInputStream(); ois = new ObjectInputStream(NativeIn);
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
		// Reception d’un String
		try {
			String res = (String) ois.readObject(); 
			System.out.println(res);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// Fermeture des flux evolues et natifs
		try {
			ois.close();
			oos.close(); 
			NativeIn.close(); 
			NativeOut.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		// Fermeture de la connexion
		try {
			clientSocket.close(); 
		} catch (IOException e) {
			System.out.println(e.toString());
		}*/
}