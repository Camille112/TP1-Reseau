package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * ClientThread is the class that defines a client on the server side
 * 
 * A ClientThread is composed of:
 * <ul>
 * <li>A Socket : socket to which the client is connected and on which messages between the server and the client are exchanged</li>
 * <li>A BufferedReader socIn : connected to the socket and receives data in form of Strings</li>
 * <li>A PrintStream socOut : connected to the socket and sends data in form of Strings</li>
 * <li>A String : username of the Client</li>
 * <li>A ChatGroup : the general ChatGroup on which all Clients are connected</li>
 * <li>A ArrayList of ChatGroup : list of the groups of which the client is a member</li>
 * </ul>
 * 
 * ClientThread inherits Thread
 * 
 * @author Camille MIGOZZI and Karina DU
 */
public class ClientThread extends Thread {
	private Socket clientSocket;
	private BufferedReader socIn;
	private PrintStream socOut;
	private String clientUsername = "";
	public static ChatGroup general = null;
	private ArrayList<ChatGroup> myGroups = new ArrayList<ChatGroup>();

	/**
	 * Class constructor.
	 * Initializes : Socket, socIn, socOut, clientUsername, myGroups
	 */
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

	/**
	 * Attribute socOut getter.
	 * @return PrintStream.
	 */
	public PrintStream getSocOut() {
		return socOut;
	}

	/**
	 * Attribute clientUsername getter.
	 * @return String.
	 */
	public String getClientUsername() {
		return clientUsername;
	}

	/**
	 * This method receives a request from client, depending the request the action differs
	 **/
	public void run() {
		while (clientSocket.isConnected()) {
			try {
				String messageFromClient = socIn.readLine();
				System.out.println("received : " + messageFromClient);
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
								s = s + username + "#";
							}
							socOut.println(s);
						}
						for (ChatGroup cg : myGroups) {
							cg.sendHistory(this);
						}
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
						for (int i = 3 ; i<msg.length ; i++) {
								members.add(msg[i]);
						}
						ChatGroup cg = new ChatGroup(msg[2], members);
						cg.sendInvite(clientUsername);
						myGroups.add(cg);
					}
				}else {
					break;
				}
			} catch (Exception e) {
				closeEverything();
			}
		}
	}
	
	/**
	 * This method adds a group to myGroups
	 * @param group The ChatGroup
	 */
	public void addGroup(ChatGroup group) {
        myGroups.add(group);
    }

	/**
	 * This method closes the Socket, the BufferedReader and the PrintStream
	 */
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