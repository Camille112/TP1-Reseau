package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import controller.Controller;


public class LandingPage {

	private Controller controller;
	private JFrame page = new JFrame();
	private JLabel labelUsername = new JLabel("Enter your username :");
	private JTextArea messageArea = new JTextArea();
	private JButton buttonOk = new JButton("OK");
	private String username = "";
	
	public LandingPage(Controller controller) {
		this.controller = controller;
		Dimension dim = new Dimension(100, 200);
		page.setSize(dim);
		page.setPreferredSize(dim);
		page.setMinimumSize(dim);
		page.setMaximumSize(dim);
		page.setResizable(false);
		page.setLocationRelativeTo(null);
		page.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		page.setTitle("UserName");
		page.setVisible(true);
		page.setLayout(null);
 
		labelUsername.setLayout(null);
		messageArea.setLayout(null);
		buttonOk.setLayout(null);
 
		labelUsername.setSize(200, 40);
		labelUsername.setLocation(10, 10);
		messageArea.setEditable(true);
		messageArea.setSize(100, 40);
		messageArea.setLocation(25, 50);
		buttonOk.setSize(100, 50);
		buttonOk.setLocation(25, 100);
		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				username = messageArea.getText();
				controller.createChat(username);
				controller.closeLandingPage();
			}
		});
 
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		messageArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));		
 
		page.add(labelUsername);
		page.add(messageArea);
		page.add(buttonOk);
	}
	
	public JFrame getPage() {
		return page;
	}

}
