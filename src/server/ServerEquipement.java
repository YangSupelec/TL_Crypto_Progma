package server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import core.Equipement;

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
	
	public void init(int port) {    
        try {    
            serverSocket = new ServerSocket(port);    
            while (true) {    
                // 一旦有堵塞, 则表示服务器与客户端获得了连接    
                Socket client = serverSocket.accept();    
                // 处理这次连接    
                new HandlerThread(client);    
            }    
        } catch (Exception e) {    
            System.out.println("server exception: " + e.getMessage());    
        }  
    } 
	
	private class HandlerThread implements Runnable {    
        private Socket socket;    
        public HandlerThread(Socket client) {    
            socket = client;    
            new Thread(this).start();    
        }    
    
        public void run() {    
            try {    
                // read data from client    
                DataInputStream input = new DataInputStream(socket.getInputStream());  
                String clientInputStr = input.readUTF();   
                System.out.println("content from client :" + clientInputStr);    
    
                // reponse to client    
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
                System.out.print("please input :\t");    
                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();    
                out.writeUTF(s);    
                  
                out.close();    
                input.close();    
            } catch (Exception e) {    
                System.out.println("server run exception: " + e.getMessage());    
            } finally {    
                if (socket != null) {    
                    try {    
                        socket.close();    
                    } catch (Exception e) {    
                        socket = null;    
                        System.out.println("server端 finally exception:" + e.getMessage());    
                    }    
                }    
            }   
        }    
    }  
}
