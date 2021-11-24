/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class EchoServerMultiThreaded {
	public static void main(String args[]) {
		ServerSocket connectionSocket;
		Socket clientSocket = null;
		try {
			connectionSocket = new ServerSocket(11); // port
			System.out.println("Server ready...");
			
			ChatGroup generalChat = new ChatGroup("general",new ArrayList<String>());
			
			while (!connectionSocket.isClosed()) {
				clientSocket = connectionSocket.accept();
				
				ClientThread ct = new ClientThread(clientSocket, generalChat);
				generalChat.addClientThread(ct);
				generalChat.addMember(ct.getClientUsername());
				ct.start();
			}
		} catch (Exception e) {
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.err.println("Error in EchoServer:" + e);
		}
	}
}
