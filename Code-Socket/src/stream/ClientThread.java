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
	public static ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
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
			
			File fileHistory = new File("../doc/messageHistory.txt");
            fileHistory.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(fileHistory));
            String msg = reader.readLine();
            while(msg != null) {
                this.socOut.println(msg);
                msg = reader.readLine();
            }
            reader.close();
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
				} else {
					String [] messageParts = messageFromClient.split(" ");
					if (messageParts.length>2) {
						String from = messageParts[0];
						if (messageParts.length>=5&& messageParts[2].equals("/wh")) {
							broadcastToOne(from+" (private) : "+messageParts[4],messageParts[3]);
						}else {
							save(messageFromClient);
							System.out.println(messageFromClient);
							broadcastToAll(messageFromClient);
						}
					}
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
				System.err.println("Exception in ClientThread : "+e);
			}

		}

	}
	
	public void broadcastToOne(String messageFromClient,String username) {
		boolean sent = false;
		for (ClientThread ct : clientThreads) {
			try {
				if (ct.clientUsername.equals(username)) {
					ct.getSocOut().println(messageFromClient);
					sent = true;
				}
			} catch (Exception e) {
				closeEverything();
				System.err.println("Exception in ClientThread : "+e);
			}
		}
		if (!sent) {
			this.socOut.println(username+" is not connected.");
		}

	}
	
	public void save(String msg) {

        try {
            File fileHistory = new File("../doc/messageHistory.txt");
            fileHistory.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileHistory, true));
            writer.append(msg + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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