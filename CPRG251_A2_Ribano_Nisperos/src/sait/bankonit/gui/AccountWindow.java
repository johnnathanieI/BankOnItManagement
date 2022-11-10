package sait.bankonit.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.bankonit.exceptions.*;
import ca.bankonit.manager.*;
import ca.bankonit.models.*;

/**
 * Renders the account window.
 * 
 * @author Nick Hamnett
 * @version Aug 3, 2021
 */
public class AccountWindow extends JFrame {
	static final long serialVersionUID = 1L;
	private Account account;

	/**
	 * Initializes the account window
	 * 
	 * @param account Account to manage
	 */
	public AccountWindow(Account account) {
		super("Bank On It Account");

		// Store account as field.
		this.account = account;

		// Set size to 600x500
		this.setSize(600, 500);

		// Add Card # and Balance
		JPanel panel = new JPanel();
		JLabel cardNum = new JLabel("Card #" + this.account.getCardNumber(), SwingConstants.CENTER);
		JLabel balance = new JLabel();

		cardNum.setFont(new Font("Arial", Font.PLAIN, 24));
		cardNum.setAlignmentX(CENTER_ALIGNMENT);
		balance.setFont(new Font("Arial", Font.PLAIN, 15));
		balance.setAlignmentX(CENTER_ALIGNMENT);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(cardNum);
		panel.add(balance);

		// Add transactions
		JTextArea transactions = new JTextArea();
		JScrollPane scroll = new JScrollPane(transactions);

		transactions.setEditable(false);
		transactions.setFont(new Font("Arial", Font.BOLD, 12));

		panel.add(scroll);

		// Add bottom
		JPanel bottom = new JPanel();
		JLabel type = new JLabel("Type:");
		JRadioButton deposit = new JRadioButton("Deposit");
		JRadioButton withdraw = new JRadioButton("Withdraw ");
		JLabel amountText = new JLabel("Amount:");
		JTextField amount = new JTextField();
		ButtonGroup buttons = new ButtonGroup();
		JButton submit = new JButton("Submit");

		amount.setColumns(10);

		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));

		type.setFont(new Font("Arial", Font.PLAIN, 16));
		deposit.setFont(new Font("Arial", Font.PLAIN, 16));
		withdraw.setFont(new Font("Arial", Font.PLAIN, 16));
		amountText.setFont(new Font("Arial", Font.PLAIN, 16));
		amount.setFont(new Font("Arial", Font.PLAIN, 16));

		buttons.add(deposit);
		buttons.add(withdraw);

		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				BankManager manager = BankManagerBroker.getInstance();

				try {
					if (deposit.isSelected()) {
						double toDeposit = Double.parseDouble(amount.getText());
						if (toDeposit >= 0) {
							manager.deposit(account, toDeposit);
						} else {
							JOptionPane.showMessageDialog(panel, "Amount must be greater than or equal to 0");
						}
					} else if (withdraw.isSelected()) {
						double toWithdraw = Double.parseDouble(amount.getText());
						if (toWithdraw >= 0) {
							manager.withdraw(account, toWithdraw);
						} else {
							JOptionPane.showMessageDialog(panel, "Amount must be greater than or equal to 0");
						}
					} else {
						JOptionPane.showMessageDialog(panel, "Please select an option.");
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(panel, "Please enter a valid number.");
				} catch (InvalidAccountException e) {
					JOptionPane.showMessageDialog(panel, "How did you manage to get here without a valid account?");
				} finally {
					populateTransactions(transactions, balance, panel);
				}
			}
		});

		bottom.add(type);
		bottom.add(deposit);
		bottom.add(withdraw);
		bottom.add(amountText);
		bottom.add(amount);
		bottom.add(submit);

		panel.add(bottom);

		// Add Signout Button
		JButton signout = new JButton("Signout");
		signout.setPreferredSize(new Dimension(130, 30));
		signout.setAlignmentX(CENTER_ALIGNMENT);
		signout.setFont(new Font("Arial", Font.PLAIN, 16));

		signout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				BankManagerBroker.getInstance().persist();
				JOptionPane.showMessageDialog(panel, "Goodbye.");
				closeWindow();
			}
		});

		panel.add(signout);

		this.add(panel);
		populateTransactions(transactions, balance, panel);
	}

	/**
	 * Clears and re-populates transactions as well as updates balance.
	 */
	private void populateTransactions(JTextArea transactions, JLabel balance, JPanel panel) {
		BankManager manager = BankManagerBroker.getInstance();
		try {
			double total = 0;
			for (Transaction transaction : manager.getTransactionsForAccount(account)) {
				double amount = transaction.getAmount()
						* (transaction.getTransactionType() == Transaction.TYPE_DEPOSIT ? 1 : -1);
				total += amount;
				total = (double) Math.round(total * 100) / 100;

				transactions.append(transaction.toString() + "\n");
			}
			balance.setText(String.format("Balance: $%.2f", total));
		} catch (InvalidAccountException e) {
			JOptionPane.showMessageDialog(panel, "How did you manage to get here without a valid account?");
		}
	}

	private void closeWindow() {
		this.dispose();
	}
}