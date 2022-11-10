package sait.bankonit.gui;

import java.awt.*;

import java.awt.event.*;
import java.util.Scanner;

import javax.security.auth.x500.X500Principal;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import ca.bankonit.exceptions.InvalidAccountException;
import ca.bankonit.exceptions.InvalidCardNumberException;
import ca.bankonit.manager.*;
import ca.bankonit.models.*;

/**
 * Renders the login window
 * @author Nick Hamnett
 * @version Aug 3, 2021
 */
public class LoginWindow extends JFrame implements ActionListener {
	
	public JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	
	/**
	 * Initializes the login window.
	 */
	public LoginWindow() throws NumberFormatException {
		super("Bank On It Login");
		
		// Set window size to 500x150
		this.setSize(500, 150);
		
		// Cause process to exit when X is clicked.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Center login window in screen
		this.setLocationRelativeTo(null);
		
		initialize();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String x = textField.getText();
		String y = textField_1.getText();
		long cardNo = Long.parseLong(x);
		short pin = Short.parseShort(y);
		
		try {
		Account account = BankManagerBroker.getInstance().login(cardNo, pin);
			AccountWindow accWindow = new AccountWindow(account);
			accWindow.setVisible(true);
		}
		catch (NullPointerException ex) {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid Card No. or PIN\n Please try again.", "Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Bank On It Login");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		
		JLabel lblNewLabel_1 = new JLabel("Card Number:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("PIN:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.addActionListener(this);
		
		btnNewButton.setVerticalAlignment(SwingConstants.BOTTOM);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(37))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(154)
					.addComponent(lblNewLabel)
					.addContainerGap(166, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(211)
					.addComponent(btnNewButton)
					.addContainerGap(208, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
}
