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

/**
 * LandingPage contains a form to add a group.
 * 
 * A groupForm is composed by :
 * <ul>
 * <li>A Controller to communicate with the EchoClient.</li>
 * <li>A JFrame page that contains the components.</li>
 * <li>A String that is the username of the user.</li>
 * </ul>
 * 
 * 
 * @author Camille Migozzi and Karina Du
 * 
 * @see controller.Controller
 */

public class LandingPage {

	private Controller controller;
	JFrame page = new JFrame();
	String username = "";

	/**
	 * Class constructor.
	 * Create the landingPage with all its components.
	 * 
	 * @param controller Controller that communicates between GUI and back end.
	 * 
	 * @see controller.Controller
	 */
	public LandingPage(Controller controller) {
		this.controller = controller;
		
		JLabel labelUsername = new JLabel("Enter your username :");
		JTextArea messageArea = new JTextArea();
		JButton buttonOk = new JButton("OK");
		
		Dimension dim = new Dimension(200, 200);
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
		labelUsername.setLocation(30, 10);
		messageArea.setEditable(true);
		messageArea.setSize(100, 40);
		messageArea.setLocation(40, 50);
		buttonOk.setSize(100, 50);
		buttonOk.setLocation(40, 100);
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
	
	/**
	 * Attribute page getter.
	 * 
	 * @return JFrame page.
	 *
	 */
	
	public JFrame getPage() {
		return page;
	}

}
