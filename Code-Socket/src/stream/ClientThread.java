/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientThread extends Thread {
	private Socket clientSocket;
	private BufferedReader socIn;
	private PrintStream socOut;
	private String clientUsername = "";
	public static ChatGroup general = null;
	private ArrayList<ChatGroup> myGroups = new ArrayList<ChatGroup>();

	ClientThread(Socket s, ChatGroup generalChat) {
		this.clientSocket = s;
		general = generalChat;
		try {
			this.socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.socOut = new PrintStream(clientSocket.getOutputStream());
			this.clientUsername = socIn.readLine();

			myGroups.add(general);
			for (ChatGroup cg : general.getAllGroups()) {
				if (cg.getMembers().contains(clientUsername)) {
					myGroups.add(cg);
				}
			}

			general.broadcastToMembers(clientUsername + " has entered the chat.", clientUsername, "ENTERINGMESSAGE");
		} catch (IOException e) {
			closeEverything();
		}
	}

	public PrintStream getSocOut() {
		return socOut;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void run() {
		while (clientSocket.isConnected()) {
			try {
				String messageFromClient = socIn.readLine();
				System.out.println("---" + messageFromClient);
				if (messageFromClient != null) {
					String[] msg = messageFromClient.split("#");
					if (msg[1] != null && msg[1].equals("DISCONNECT")) {
						general.broadcastToMembers(clientUsername + " has left the chat.", clientUsername,
								"LEAVINGMESSAGE");
						general.removeClientThread(this);
						closeEverything();
						break;
					} else if (msg[1] != null && msg[1].equals("INIT")) {
						for (ChatGroup cg : myGroups) {
							String s = "#GROUPINFORMATION#" + cg.getGroupName() + "#";
							for (String username : cg.getMembers()) {
								s = s + username + "\n";
							}
							socOut.println(s);
						}/*
						for (ChatGroup cg : myGroups) {
							cg.sendHistory(this);
						}*/
					} else if (msg[1] != null && msg[1].equals("SENDMESSAGE")) {
						boolean groupFound = false;
						for (ChatGroup cg : myGroups) {
							if (cg.getGroupName().equals(msg[2])) {
								cg.broadcastToMembers(msg[3], clientUsername, "GROUPMESSAGE");
								groupFound = true;
							}
						}
						if (!groupFound) {
							this.socOut.println("#ERROR#" + msg[2] + " does not exist.");
						}
					} else if (msg[1] != null && msg[1].equals("SENDMESSAGEPRIVATE")) {
						if (!general.broadcastToOne(msg[3], clientUsername, msg[2])) {
							this.socOut.println("#ERROR#" + msg[2] + " is not connected.");
						}
					} else if (msg[1] != null && msg[1].equals("CREATEGROUP")) {
						ArrayList<String> members = new ArrayList<String>();
						String[] membersTab = msg[3].split("\n");
						for (String s : membersTab) {
							members.add(s);
						}
						ChatGroup cg = new ChatGroup(msg[2], members);
					}
				}else {
					break;
				}
			} catch (Exception e) {
				closeEverything();
			}
		}
	}

	public void closeEverything() {
		try {
			socIn.close();
			socOut.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}