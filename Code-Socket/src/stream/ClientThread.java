package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientThread extends Thread {
	public static ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
	public static ArrayList<String> messageHistory = new ArrayList<String>();
	private Socket clientSocket;
	private BufferedReader socIn;
	private PrintStream socOut;
	private String clientUsername = "";

	public PrintStream getSocOut() {
		return socOut;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	ClientThread(Socket s) {
		this.clientSocket = s;
		try {
			this.socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.socOut = new PrintStream(clientSocket.getOutputStream());
			this.clientUsername = socIn.readLine();
			clientThreads.add(this);
			broadcastToAll(clientUsername + " has entered the chat.");
			
			for(String msg : messageHistory) {
				socOut.println(msg);
			}
			
		} catch (IOException e) {
			closeEverything();
		}
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
				if (messageFromClient.contains("STOP")) {
					broadcastToAll(clientUsername + " has left the chat.");
					clientThreads.remove(this);
					closeEverything();
					break;
				} else if(messageFromClient.contains("SAVE")) {
					save();
				} else {
					messageHistory.add(messageFromClient);
					System.out.println(messageFromClient);
					broadcastToAll(messageFromClient);
				}
			} catch (Exception e) {
				closeEverything();
			}
		}

	}

	public void broadcastToAll(String messageFromClient) {
		for (ClientThread ct : clientThreads) {
			try {
				if (!ct.clientUsername.equals(this.clientUsername))
					ct.getSocOut().println(messageFromClient);
			} catch (Exception e) {
				closeEverything();
			}

		}

	}
	
	public void save() {
		
		try {
			File fileHistory = new File("../doc/messageHistory.txt");
			fileHistory.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileHistory, true));
		    for(String msg : messageHistory) {
		    	writer.append(msg + "\n");
			}
		    writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void closeEverything() {
		try {
			File fileHistory = new File("./doc/messageHistory.txt");
			fileHistory.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileHistory, true));
		    for(String msg : messageHistory) {
		    	writer.append(msg + "\n");
			}
		    writer.close();
			
			
			
			
			socIn.close();
			socOut.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}