
package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AllClientsThread extends Thread {
	private ArrayList<ClientInThread> clientsIn = new ArrayList<ClientInThread>();
	private ArrayList<ClientOutThread> clientsOut = new ArrayList<ClientOutThread>();
	
	AllClientsThread() {
		
	}
	
	public ArrayList<ClientInThread> getClientsIn() {
		return clientsIn;
	}

	public void setClientsIn(ArrayList<ClientInThread> clientsIn) {
		this.clientsIn = clientsIn;
	}

	public void updateClients(ArrayList<ClientInThread> clientsIn,ArrayList<ClientOutThread> clientsOut) {
		this.clientsIn = new ArrayList<ClientInThread>(clientsIn);
		this.clientsOut = new ArrayList<ClientOutThread>(clientsOut);
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void run() {
		try {
			while (true) {
				System.out.println("-");
				for (ClientInThread clIn : clientsIn) {
					System.out.println(clIn.getId() + "newMsg : "+clIn.getNewMsg());
					if (clIn.getNewMsg()) {
						System.out.println("newMsg");
						for (ClientOutThread clOut : clientsOut) {
							System.out.println("Sending to :" + clOut.getId());
							clOut.send(clIn.getLine());
						}
						clIn.setNewMsg(false);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

}