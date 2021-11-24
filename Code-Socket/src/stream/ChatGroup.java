package stream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ChatGroup is the class that defines a group chat with members that can send each others messages
 * 
 * A ChatGroup is composed of:
 * <ul>
 * <li>A ArrayList of ClientThread : all the ClientThreads connected to the Server</li>
 * <li>A ArrayList of ChatGroup : all the existent ChatGroups on the Server</li>
 * <li>A ArrayList of String : lists the usernames of the members of the chat</li>
 * <li>A File : file where the history of the messages is saved</li>
 * <li>A String : the group name</li>
 * </ul>
 * 
 * @author Camille MIGOZZI & Karina DU
 */
public class ChatGroup {
	public static ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
	public static ArrayList<ChatGroup> allGroups = new ArrayList<ChatGroup>();
	private ArrayList<String> members = new ArrayList<String>();
	private File historyFile = null;
	private String groupName = "";

	/**
	 * Class constructor.
	 * Initializes : groupName, members, historyFile
	 */
	public ChatGroup(String groupName, ArrayList<String> usernames) throws IOException {
		this.groupName = groupName;

		this.members = usernames;
		historyFile = new File("../doc/" + groupName + "GroupChatHistory.txt");
		historyFile.createNewFile();
		allGroups.add(this);
	}

	/**
	 * Attribute allGroups getter.
	 * @return ArrayList<ChatGroup>.
	 */
	public ArrayList<ChatGroup> getAllGroups() {
		return allGroups;
	}

	/**
	 * Attribute members getter.
	 * @return ArrayList<String>.
	 */
	public ArrayList<String> getMembers() {
		return members;
	}

	/**
	 * Attribute groupName getter.
	 * @return String.
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * This method adds a new member to the chat group.
	 * @param username The username of the new member.
	 */
	public void addMember(String username) {
		members.add(username);
	}

	/**
	 * This method adds a ClientThread when it just connected
	 * @param ct The new ClientThread
	 */
	public void addClientThread(ClientThread ct) {
		clientThreads.add(ct);
	}

	/**
	 * This method removes a ClientThread
	 * @param ct The ClientThread
	 */
	public void removeClientThread(ClientThread ct) {
		clientThreads.remove(ct);
	}
	
	/**
	 * This method sends a message to all the members of the ChatGroup (except the sender)
	 * @param messageFromClient The message to send
	 * @param senderUsername The username of the sender
	 * @param definition Indication for the Client of the type of message
	 */
	public void broadcastToMembers(String messageFromClient, String senderUsername, String definition) {
		if(definition.equals("GROUPMESSAGE")) {
			save("#" + senderUsername + "#" + messageFromClient);
		}
		
		for (ClientThread ct : clientThreads) {
			try {
				if (members.contains(ct.getClientUsername()) && !ct.getClientUsername().equals(senderUsername))
					ct.getSocOut()
							.println("#"+definition+"#" + groupName + "#" + senderUsername + "#" + messageFromClient);
			} catch (Exception e) {
				//System.err.println("Exception in ClientThread : " + e);
			}
		}
	}

	/**
	 * This method sends a message to only one client
	 * @param messageFromClient The message to send
	 * @param usernameSender The username of the sender
	 * @param usernameReceiver The username of the receiver
	 * @return boolean : true if the message has been sent (if the client is connected)
	 */
	public boolean broadcastToOne(String messageFromClient, String usernameSender, String usernameReceiver) {
		boolean sent = false;
		for (ClientThread ct : clientThreads) {
			try {
				if (ct.getClientUsername().equals(usernameReceiver)) {
					ct.getSocOut().println("#PRIVATEMESSAGE#" + usernameSender + "#" + messageFromClient);
					sent = true;
				}
			} catch (Exception e) {
				//System.err.println("Exception in ClientThread : " + e);
			}
		}
		return sent;
	}
	
	/**
	 * This method sends the information of the ChatGroup to all the Client that have been added
	 * @param senderUsername The username of the creator of the group
	 */
	public void sendInvite(String senderUsername) {
		String s = "#GROUPINFORMATION#" + groupName + "#";
		for(String username : members) {
			s = s + username + "#";
		}
		
		for (ClientThread ct : clientThreads) {
			try {
				if (members.contains(ct.getClientUsername()) && !ct.getClientUsername().equals(senderUsername)) {
					ct.getSocOut().println(s);
					ct.addGroup(this);
				}
			} catch (Exception e) {
				//System.err.println("Exception in ClientThread : " + e);
			}
		}
	}
	
	/**
	 * This method sends to a newly connected client all the previous messages of the chat
	 * @param client The new ClientThread
	 */
	public void sendHistory(ClientThread client) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(historyFile));
		String msg = reader.readLine();
		while (msg != null) {
				client.getSocOut().println("#HISTORY#" + groupName + msg);
				msg = reader.readLine();
		}
		reader.close();
	}

	/**
	 * This method saves a message in the historyFile
	 * @param msg The message
	 */
	public void save(String msg) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true));
			writer.append(msg + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}