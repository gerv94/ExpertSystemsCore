package main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.controller.StaticController;
import main.model.entities.RulesHeaderEntity;

public class RulesFrame extends JFrame implements Runnable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JList<RulesHeaderEntity> list;
	private JButton btnAdd;
	private JButton btnDel;
	private JButton btnMod;
	private JLabel lblO;
	private Thread thread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RulesFrame frame = new RulesFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RulesFrame() {
		setTitle("Rules");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 359, 405);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		textField.setActionCommand("addRule");
		panel.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new GridLayout(0, 5, 0, 0));

		JButton btnAnd = new JButton(StaticController.uAND);
		btnAnd.addActionListener(this);
		btnAnd.setBackground(Color.WHITE);
		panel_2.add(btnAnd);

		JButton btnOr = new JButton(StaticController.uOR);
		btnOr.addActionListener(this);
		btnOr.setBackground(Color.WHITE);
		panel_2.add(btnOr);

		JButton btnNot = new JButton(StaticController.uNEGATION);
		btnNot.addActionListener(this);
		btnNot.setBackground(Color.WHITE);
		panel_2.add(btnNot);

		JButton btnConditional = new JButton(StaticController.uCONDITIONAL);
		btnConditional.addActionListener(this);
		btnConditional.setBackground(Color.WHITE);
		panel_2.add(btnConditional);

		JButton btnBiconditional = new JButton(StaticController.uBICONDITIONAL);
		btnBiconditional.addActionListener(this);
		btnBiconditional.setBackground(Color.WHITE);
		panel_2.add(btnBiconditional);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new GridLayout(0, 3, 0, 0));

		btnAdd = new JButton("Agregar");
		btnAdd.setActionCommand("addRule");
		btnAdd.setBackground(Color.WHITE);
		panel_3.add(btnAdd);

		btnDel = new JButton("Eliminar");
		btnDel.setActionCommand("deleteRule");
		btnDel.setBackground(Color.WHITE);
		panel_3.add(btnDel);

		btnMod = new JButton("Modificar");
		btnMod.setActionCommand("updateRule");
		btnMod.setBackground(Color.WHITE);
		panel_3.add(btnMod);

		lblO = new JLabel(" \u25CF ");
		lblO.setForeground(new Color(0, 255, 0));
		panel.add(lblO, BorderLayout.WEST);

		list = new JList<RulesHeaderEntity>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() > 1)
					textField.setText(convertConsoleToUnicode(list.getSelectedValue().getText()));
			}
		});
		contentPane.add(list, BorderLayout.CENTER);

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		textField.requestFocus();
	}

	public Object getSelected() {
		return list.getSelectedValue();
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	public void setList(DefaultListModel<RulesHeaderEntity> rulesModel) {
		list.setModel(rulesModel);
	}

	public String getText() {
		return convertUnicodeToConsole(textField.getText());
	}

	public void addActionListener(ActionListener actionListener) {
		btnAdd.addActionListener(actionListener);
		btnDel.addActionListener(actionListener);
		btnMod.addActionListener(actionListener);
		textField.addActionListener(actionListener);
	}

	public String convertConsoleToUnicode(String string) {
		string = string.toUpperCase();
		string = string.replace(StaticController.AND, StaticController.uAND);
		string = string.replace(StaticController.OR, StaticController.uOR);
		string = string.replace(StaticController.NEGATION, StaticController.uNEGATION);
		string = string.replace(StaticController.BICONDITIONAL, StaticController.uBICONDITIONAL);
		string = string.replace(StaticController.CONDITIONAL, StaticController.uCONDITIONAL);
		return string;
	}

	public String convertUnicodeToConsole(String string) {
		string = string.toUpperCase();
		string = string.replace(StaticController.uAND, StaticController.AND);
		string = string.replace(StaticController.uOR, StaticController.OR);
		string = string.replace(StaticController.uNEGATION, StaticController.NEGATION);
		string = string.replace(StaticController.uBICONDITIONAL, StaticController.BICONDITIONAL);
		string = string.replace(StaticController.uCONDITIONAL, StaticController.CONDITIONAL);
		return string;
	}

	@Override
	public void run() {
		int selectionEnd, selectionStart;
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			selectionEnd = textField.getCaretPosition();
			selectionStart = textField.getSelectionStart() < selectionEnd ? textField.getSelectionStart() : textField.getSelectionEnd();
			textField.setText(convertConsoleToUnicode(textField.getText()));
			textField.setCaretPosition(
					selectionStart < textField.getText().length() ? selectionStart : textField.getText().length());
			textField.moveCaretPosition(
					selectionEnd < textField.getText().length() ? selectionEnd : textField.getText().length());
			if (getText().matches(StaticController.ruleValidator)) {
				lblO.setForeground(Color.GREEN);
			} else {
				lblO.setForeground(Color.RED);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textField.setText(textField.getText() + e.getActionCommand());
		textField.requestFocus();
	}

	public void clearText() {
		textField.setText("");
	}
}
