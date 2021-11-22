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
	private Controller controller;

	public Chat(Controller controller, String username) {
		this.controller = controller;
		controller.createEchoListener(username);

		Dimension dim = new Dimension(750, 600);
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
		JButton buttonSend = new JButton("Send Message");
		JTextArea messageArea = new JTextArea();

		labelChat.setLayout(null);
		chatArea.setLayout(null);
		messageArea.setLayout(null);
		buttonSend.setLayout(null);

		labelChat.setSize(200, 40);
		labelChat.setLocation(10, 10);
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

		Border border = BorderFactory.createLineBorder(Color.BLACK);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		messageArea
				.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		add(labelChat);
		add(scrollPane);
		add(messageArea);
		add(buttonSend);
		addWindowListener(this);

		buttonSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String message = messageArea.getText();
				messageArea.setText("");
				chatArea.append("\n" + username + " (you) : " + message);
				chatArea.setCaretPosition(chatArea.getText().length());
				controller.sendMessage(message);
			}
		});

	}

	public void receiveMessage(String message) {
		chatArea.append("\n" + message);
		chatArea.setCaretPosition(chatArea.getText().length());
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
