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
		AllClientsThread allClients = new AllClientsThread();
		allClients.start();
		try {
			connectionSocket = new ServerSocket(11); // port
			System.out.println("Server ready...");
			while (true) {
				Socket clientSocket = connectionSocket.accept();
				// System.out.println("Connexion from:" + clientSocket.getInetAddress());
				ClientInThread ctIn = new ClientInThread(clientSocket);
				clientsIn.add(ctIn);
				ClientOutThread ctOut = new ClientOutThread(clientSocket);
				clientsOut.add(ctOut);
				allClients.updateClients(clientsIn, clientsOut);
				
				for(ClientInThread cIn : allClients.getClientsIn()) {
					System.out.println("Id thread : "+cIn.getId());
				}
					
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
