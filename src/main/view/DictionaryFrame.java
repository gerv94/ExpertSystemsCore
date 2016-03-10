package main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import main.controller.StaticController;
import main.model.entities.DictionaryEntity;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class DictionaryFrame extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JList<DictionaryEntity> list = new JList<DictionaryEntity>();
	private JButton btnAdd;
	private JButton btnDel;
	private JButton btnMod;
	private JPanel panel_2;
	private JLabel lblO;
	private Thread thread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryFrame frame = new DictionaryFrame();
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
	public DictionaryFrame() {
		setTitle("Dictionary");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 332);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() > 1)
					textField.setText(list.getSelectedValue().getText());
			}
		});

		panel.add(list, BorderLayout.CENTER);
		
		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
				panel_2.setLayout(new BorderLayout(0, 0));
		
				textField = new JTextField();
				panel_2.add(textField);
				textField.setActionCommand("addProposition");
				textField.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 3, 0, 0));

		btnAdd = new JButton("Agregar");
		btnAdd.setActionCommand("addProposition");
		btnAdd.setBackground(Color.WHITE);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_1.add(btnAdd);

		btnDel = new JButton("Eliminar");
		btnDel.setActionCommand("deleteProposition");
		btnDel.setBackground(Color.WHITE);
		panel_1.add(btnDel);

		btnMod = new JButton("Modificar");
		btnMod.setActionCommand("updateProposition");
		btnMod.setBackground(Color.WHITE);
		panel_1.add(btnMod);
		
		lblO = new JLabel(" \u25CF ");
		panel_2.add(lblO, BorderLayout.WEST);
		
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

	public void setList(ListModel<DictionaryEntity> model) {
		list.setModel(model);
	}

	public void addActionListener(ActionListener actionListener) {
		btnAdd.addActionListener(actionListener);
		btnDel.addActionListener(actionListener);
		btnMod.addActionListener(actionListener);
		textField.addActionListener(actionListener);
	}

	public String getText() {
		return textField.getText();
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
			textField.setText(textField.getText());
			textField.setCaretPosition(
					selectionStart < textField.getText().length() ? selectionStart : textField.getText().length());
			textField.moveCaretPosition(
					selectionEnd < textField.getText().length() ? selectionEnd : textField.getText().length());
			if (getText().matches(StaticController.dictionaryValidator)) {
				lblO.setForeground(Color.GREEN);
			} else {
				lblO.setForeground(Color.RED);
			}
		}
	}

	public void clearText() {
		textField.setText("");
	}
}
