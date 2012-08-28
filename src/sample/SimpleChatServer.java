/**
 * 
 */
package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author gHc
 *
 */
public class SimpleChatServer {

	ArrayList clientOutputStreams;
	
	public class ClientHandler implements Runnable {

		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String message;
			try {
				while((message=reader.readLine())!=null) {
					System.out.println("server read "+message);
					tellEveryone(message);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SimpleChatServer().go();
	}

	public void go() {
		clientOutputStreams = new ArrayList();
		
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void tellEveryone(String message) {
		// TODO Auto-generated method stub
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext()) {
			PrintWriter writer = (PrintWriter)it.next();
			writer.println(message);
			writer.flush();
		}
	}

}
