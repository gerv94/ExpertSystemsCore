package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import main.controller.DictionaryController;
import main.controller.RulesController;
import main.model.entities.DictionaryEntity;
import main.model.entities.RulesHeaderEntity;
import main.view.DictionaryFrame;
import main.view.RulesFrame;

public class MainClassFromGUI implements ActionListener {
	public static SessionFactory sessionFactory = null;
	public static DictionaryController dictionaryController = new DictionaryController();
	public static RulesController rulesController = new RulesController();
	private DictionaryFrame dicionaryFrame;
	private RulesFrame rulesFrame;
	private DefaultListModel<DictionaryEntity> dictionaryModel;
	private DefaultListModel<RulesHeaderEntity> rulesModel;

	private static void configureSessionFactory() {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable thr) {
			System.err.println("Failed to create the SessionFactory object" + thr);
			throw new ExceptionInInitializerError(thr);
		}
	}

	public MainClassFromGUI() {
		configureSessionFactory();
		dictionaryController.retrieve();
		rulesController.retrieve();
		System.out.println(dictionaryController.toString());
		System.out.println(rulesController.toString());

		dictionaryModel = new DefaultListModel<DictionaryEntity>();
		for (DictionaryEntity dictionaryEntity : dictionaryController.getPropositions())
			dictionaryModel.addElement(dictionaryEntity);
		dicionaryFrame = new DictionaryFrame();
		dicionaryFrame.setList(dictionaryModel);
		dicionaryFrame.addActionListener(this);
		dicionaryFrame.setVisible(true);

		rulesModel = new DefaultListModel<RulesHeaderEntity>();
		for (RulesHeaderEntity rulesHeaderEntity : rulesController.getRules())
			rulesModel.addElement(rulesHeaderEntity);
		rulesFrame = new RulesFrame();
		rulesFrame.setList(rulesModel);
		rulesFrame.addActionListener(this);
		rulesFrame.setVisible(true);

		// System.out.println(dictionaryController.getIndexOf("a"));
		// System.out.println(dictionaryController.getCodeOf(2));
		// System.exit(0);
	}

	public static void main(String[] args) {
		new MainClassFromGUI();
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		Object selected;
		switch (actionEvent.getActionCommand()) {
		case "addProposition":
			try {
				dictionaryModel.addElement(dictionaryController.addPropostion(dicionaryFrame.getText()));
				dicionaryFrame.clearText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "deleteProposition":
			selected = dicionaryFrame.getSelected();
			if (selected != null) {
				if (dictionaryController.deletePropostion(((DictionaryEntity) selected).getId()))
					dictionaryModel.remove(dicionaryFrame.getSelectedIndex());
				else
					System.out.println("Error trying to delete: " + selected);
			}
			break;
		case "updateProposition":
			selected = dicionaryFrame.getSelected();
			DictionaryEntity dictionaryEntity;
			if (selected != null) {
				try {
					dictionaryEntity = dictionaryController.updatePropostion(((DictionaryEntity) selected).getId(),
							dicionaryFrame.getText());
					if (dictionaryEntity != null){
						dictionaryModel.set(dicionaryFrame.getSelectedIndex(), dictionaryEntity);
						dicionaryFrame.clearText();
					}else
						System.out.println("Error trying to update: " + selected);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case "addRule":
			try {
				rulesModel.addElement(rulesController.addRule(rulesFrame.getText()));
				rulesFrame.clearText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "deleteRule":
			selected = rulesFrame.getSelected();
			if (selected != null) {
				if (rulesController.deleteRule(((RulesHeaderEntity) selected).getId()))
					rulesModel.remove(rulesFrame.getSelectedIndex());
				else
					System.out.println("Error trying to delete: " + selected);
			}
			break;
		case "updateRule":
			selected = dicionaryFrame.getSelected();
			RulesHeaderEntity rulesHeaderEntity;
			if (selected != null) {
				try {
					rulesHeaderEntity = rulesController.updateRule(((DictionaryEntity) selected).getId(),
							rulesFrame.getText());
					if (rulesHeaderEntity != null)
						rulesModel.set(rulesFrame.getSelectedIndex(), rulesHeaderEntity);
					else
						System.out.println("Error trying to delete: " + selected);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}
}
