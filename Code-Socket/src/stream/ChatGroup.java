package stream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ChatGroup {
	public static ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
	public static ArrayList<ChatGroup> allGroups = new ArrayList<ChatGroup>();
	private ArrayList<String> members = new ArrayList<String>();
	private File historyFile = null;
	private String groupName = "";

	public ChatGroup(String groupName, ArrayList<String> usernames) throws IOException {
		this.groupName = groupName;

		this.members = usernames;
		/*
		historyFile = new File("../doc/" + groupName + "GroupChatHistory.txt");
		historyFile.createNewFile();
		 */
		allGroups.add(this);
	}

	public ArrayList<ChatGroup> getAllGroups() {
		return allGroups;
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public String getGroupName() {
		return groupName;
	}

	public void addMember(String username) {
		members.add(username);
	}

	public void addClientThread(ClientThread ct) {
		clientThreads.add(ct);
	}

	public void removeClientThread(ClientThread ct) {
		clientThreads.remove(ct);
	}
	
	public void broadcastToMembers(String messageFromClient, String senderUsername, String definition) {
		if(definition.equals("GROUPMESSAGE")) {
			//save("#" + senderUsername + "#" + messageFromClient);
		}
		
		for (ClientThread ct : clientThreads) {
			try {
				if (members.contains(ct.getClientUsername()) && !ct.getClientUsername().equals(senderUsername))
					ct.getSocOut()
							.println("#"+definition+"#" + groupName + "#" + senderUsername + "#" + messageFromClient);
			} catch (Exception e) {
				System.err.println("Exception in ClientThread : " + e);
			}
		}
	}

	public boolean broadcastToOne(String messageFromClient, String usernameSender, String usernameReceiver) {
		boolean sent = false;
		for (ClientThread ct : clientThreads) {
			try {
				if (ct.getClientUsername().equals(usernameReceiver)) {
					ct.getSocOut().println("#PRIVATEMESSAGE#" + usernameSender + "#" + messageFromClient);
					sent = true;
				}
			} catch (Exception e) {
				System.err.println("Exception in ClientThread : " + e);
			}
		}
		return sent;
	}
	
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
				System.err.println("Exception in ClientThread : " + e);
			}
		}
	}
	
	public void sendHistory(ClientThread client) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(historyFile));
		String msg = reader.readLine();
		while (msg != null) {
				client.getSocOut().println("#HISTORY#" + groupName + msg);
				msg = reader.readLine();
		}
		reader.close();
	}

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