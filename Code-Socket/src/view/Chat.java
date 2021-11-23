package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import controller.Controller;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Chat extends JFrame implements WindowListener {

	// https://www.sourcecodester.com/tutorials/java/9097/chat-system-chat-gui.html
	
	private JTextArea chatArea = new JTextArea("Welcome to the chat room!\n");
	private JTextArea groupsArea = new JTextArea("");
	private Controller controller;
	private GroupForm groupForm;

	public Chat(Controller controller, String username) {
		this.controller = controller;
		Dimension dim = new Dimension(1000, 650);
		setSize(dim);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Chat");
		setVisible(true);
		setLayout(null);

		JLabel labelChat = new JLabel("Chat :");
		JLabel labelGroups = new JLabel("List of groups:");
		JButton buttonNewGroup = new JButton("Add a group");
		JButton buttonSend = new JButton("Send Message");
		JTextArea messageArea = new JTextArea();

		labelChat.setLayout(null);
		labelGroups.setLayout(null);
		groupsArea.setLayout(null);
		chatArea.setLayout(null);
		messageArea.setLayout(null);
		buttonSend.setLayout(null);
		buttonNewGroup.setLayout(null);

		labelChat.setSize(200, 40);
		labelChat.setLocation(10, 10);
		labelGroups.setSize(200, 40);
		labelGroups.setLocation(800, 10);
		groupsArea.setEditable(false);
		JScrollPane scrollPaneGroup = new JScrollPane(groupsArea);
		scrollPaneGroup.setSize(200, 370);
		scrollPaneGroup.setLocation(750, 40);
		scrollPaneGroup.setViewportView(groupsArea);
		scrollPaneGroup.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setSize(700, 370);
		scrollPane.setLocation(10, 40);
		scrollPane.setViewportView(chatArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		messageArea.setEditable(true);
		messageArea.setSize(700, 60);
		messageArea.setLocation(10, 440);
		buttonSend.setSize(200, 40);
		buttonSend.setLocation(250, 520);
		buttonNewGroup.setSize(200, 40);
		buttonNewGroup.setLocation(750, 450);

		Border border = BorderFactory.createLineBorder(Color.BLACK);
		scrollPaneGroup.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		scrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		messageArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		add(labelChat);
		add(labelGroups);
		add(scrollPaneGroup);
		add(scrollPane);
		add(messageArea);
		add(buttonSend);
		add(buttonNewGroup);
		addWindowListener(this);
			
		controller.createEchoListener(username);
		//controller.sendMessage("#INIT");
		
		buttonSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String message = messageArea.getText();
				messageArea.setText("");
				String [] messageParts = message.split(" ");
				String receiver = "";
				String group = "";
				String messageModified = "";
				if (messageParts.length>=2 && messageParts[0].equals("/wh")) { 
					receiver = messageParts[1];
					message = "";
					for (int i=2; i<messageParts.length;i++) {
						message = message+messageParts[i]+" ";
					}
					chatArea.append("\n" + username + " (you) to "+ receiver +" : " + message);
					messageModified = ("#SENDMESSAGEPRIVATE#"+receiver+"#"+message);
				}else if (messageParts.length>=2 && messageParts[0].equals("/gr")) {
					group = messageParts[1];
					message = "";
					for (int i=2; i<messageParts.length;i++) {
						message = message+messageParts[i]+" ";
					}
					chatArea.append("\n" + username + " (you) to "+ group +" (group) : " + message);
					messageModified = ("#SENDMESSAGE#"+group+"#"+message);
				}else {
					chatArea.append("\n" + username + " (you) : " + message);
					messageModified = ("#SENDMESSAGE#general#"+message);
				}
				chatArea.setCaretPosition(chatArea.getText().length());
				controller.sendMessage(messageModified);
			}
		});
		
		buttonNewGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				groupForm = new GroupForm(controller, username);
			}
		});

	}

	public GroupForm getGroupForm() {
		return groupForm;
	}
	
	
	public void receiveMessage(String message) {
		String [] arrayMessage = message.split("#");
		if (arrayMessage.length>0 && arrayMessage[1].equals("GROUPMESSAGE")) {
			if (arrayMessage[2].equals("general")) {
				chatArea.append("\n" + arrayMessage[3]+" : "+arrayMessage[4]);
			} else {
				chatArea.append("\n" + arrayMessage[3]+ " ( "+arrayMessage[2]+" ) : "+arrayMessage[4]);
			}
		}else if (arrayMessage[1].equals("PRIVATEMESSAGE")){
			chatArea.append("\n" + arrayMessage[2]+ " ( private ) : "+arrayMessage[3]);
		} else if (arrayMessage[1].equals("HISTORY")){
			if (arrayMessage[2].equals("general")) {
				chatArea.append("\n" + arrayMessage[3]+ " : "+arrayMessage[4]);
			}else {
				chatArea.append("\n" + arrayMessage[3]+ " ( "+arrayMessage[2]+" ) : "+arrayMessage[4]);
			}
		} else if (arrayMessage[1].equals("ENTERINGMESSAGE") || arrayMessage[1].equals("LEAVINGMESSAGE") ) {
			chatArea.append("\n" + arrayMessage[4]);
		} else if (arrayMessage[1].equals("ERROR")){
			chatArea.append("\n" + arrayMessage[2]);
		} else if (arrayMessage[1].equals("GROUPINFORMATION")){
			if (!arrayMessage[2].equals("general")) {
				String members = "";
				for (int i=3; i<arrayMessage.length; i++) {
					members+=arrayMessage[i]+"\n";
				}
				addGroup("\n" + arrayMessage[2],members);
			}
		} else {
			System.out.println("Problem ...");
		}
		chatArea.setCaretPosition(chatArea.getText().length());

	}

	public void addGroup(String newGroupName, String newGroupMembers) {
		groupsArea.append("\n\n" + newGroupName+" : ");
		groupsArea.append("\n" + newGroupMembers);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		controller.leaveChat();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



}
