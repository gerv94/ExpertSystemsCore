package main.controller;

import java.util.List;
import java.util.regex.Matcher;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import main.ConsoleController;
import main.HexBiController;
import main.model.entities.DictionaryEntity;
import regex.parser.RegExController;

public final class DictionaryController {
	private static List<DictionaryEntity> propositions;

	// Necesarios para las peticiones a la Base de Datos
	Transaction tx = null;
	Session session = null;

	public String getTextOf(int index) {
		for (DictionaryEntity dictionaryEntity : propositions)
			if (dictionaryEntity.getCodeId() == index)
				return dictionaryEntity.getText();
		return null;
	}

	public List<DictionaryEntity> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<DictionaryEntity> propositions) {
		DictionaryController.propositions = propositions;
	}

	@SuppressWarnings("unchecked")
	public int retrieve() {
		session = ConsoleController.sessionFactory.openSession();
		tx = null;
		try {
			tx = session.beginTransaction();
			propositions = session.createQuery("from DictionaryEntity").list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		} finally {
			if (session != null)
				session.close();
		}
		session = null;
		return propositions.size();
	}

	public DictionaryEntity addPropostion(String text) throws Exception {
		DictionaryEntity entity = null;
		session = ConsoleController.sessionFactory.openSession();
		tx = null;
		String code = "", proposition = "";
		Matcher matcher = RegExController.dictionaryRegExPattern.matcher(text);
		if (matcher.matches()) {
			code = matcher.group(1);
			proposition = matcher.group(2);
		}
		if (code.equals("") || proposition.equals(""))
			throw new Exception("Bad input: dictionaryEntry has not matched");

		try {
			tx = session.beginTransaction();
			entity = new DictionaryEntity(code, proposition);
			if (propositions.contains(entity)) {
				throw new EntityExistsException("The proposition: " + entity + " already exists");
			}
			session.save(entity);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			return null;
		} finally {
			if (session != null)
				session.close();
		}
		session = null;
		propositions.add(entity);
		return entity;
	}

	public DictionaryEntity updatePropostion(String code, String text) throws Exception {
		Session session = ConsoleController.sessionFactory.openSession();
		Transaction tx = null;
		int index = -1;
		DictionaryEntity de = null;
		for (DictionaryEntity dictionaryEntity : propositions)
			if (dictionaryEntity.getCodeId() == HexBiController.hexBiToInteger(code)){
				index = dictionaryEntity.getCodeId();
				de = dictionaryEntity;
			}
		if (index < 0)
			throw new Exception("Bad code: dictionaryEntry has not founded");

		try {
			tx = session.beginTransaction();
			DictionaryEntity entity = (DictionaryEntity) session.get(DictionaryEntity.class, index);
			entity.setText(text);
			session.update(entity);
			tx.commit();
			de.setText(text);
			return entity;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		session = null;
		return null;
	}

	public boolean deletePropostion(String code) throws Exception {
		int index = -1;
		DictionaryEntity de = null;
		for (DictionaryEntity dictionaryEntity : propositions)
			if (dictionaryEntity.getCodeId() == HexBiController.hexBiToInteger(code)){
				index = dictionaryEntity.getCodeId();
				de = dictionaryEntity;
			}
		if (index < 0)
			throw new Exception("Bad code: dictionaryEntry has not founded");
		Session session = ConsoleController.sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DictionaryEntity entity = (DictionaryEntity) session.get(DictionaryEntity.class, index);
			session.delete(entity);
			tx.commit();
			propositions.remove(de);
			return true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			session.close();
			session = null;
		}
	}

	@Override
	public String toString() {
		return propositions.toString();
	}

}
