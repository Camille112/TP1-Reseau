/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.IOException;
import java.net.*;

public class EchoServerMultiThreaded {

	/**
	 * main method
	 * 
	 * @param EchoServer port
	 * 
	 **/

	public static void main(String args[]) {
		ServerSocket connectionSocket;
		Socket clientSocket = null;
		try {
			connectionSocket = new ServerSocket(11); // port
			System.out.println("Server ready...");
			
			while (!connectionSocket.isClosed()) {
				clientSocket = connectionSocket.accept();
				
				ClientThread ct = new ClientThread(clientSocket);
				ct.start();
				

			}
		} catch (Exception e) {
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.err.println("Error in EchoServer:" + e);
		}
	}
}
