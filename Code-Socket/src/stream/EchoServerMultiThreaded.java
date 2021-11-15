/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class EchoServerMultiThreaded {

	/**
	 * main method
	 * 
	 * @param EchoServer port
	 * 
	 **/

	public static void main(String args[]) {
		ServerSocket connectionSocket;
		ArrayList<ClientInThread> clientsIn = new ArrayList<ClientInThread>();
		ArrayList<ClientOutThread> clientsOut = new ArrayList<ClientOutThread>();

		try {
			connectionSocket = new ServerSocket(11); // port
			System.out.println("Server ready...");
			while (true) {
				System.out.println("----1");
				for (ClientInThread clIn : clientsIn) {
					System.out.println("----2");
					if (clIn.getNewMsg()) {
						System.out.println("newMsg");
						for (ClientOutThread clOut : clientsOut) {
							clOut.send(clIn.getLine());
						}
						clIn.setNewMsg(false);
					}
				}
				Socket clientSocket = connectionSocket.accept();
				// System.out.println("Connexion from:" + clientSocket.getInetAddress());
				ClientInThread ctIn = new ClientInThread(clientSocket);
				clientsIn.add(ctIn);
				ClientOutThread ctOut = new ClientOutThread(clientSocket);
				clientsOut.add(ctOut);
				ctIn.start();
				ctOut.start();

			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

	/*
	 * public void sendToAll(String line) { for(ClientOutThread ctOut : clientsOut)
	 * { ctOut.send(line); } }
	 */
}
