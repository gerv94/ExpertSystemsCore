package main.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import main.MainClass;
import main.model.entities.DictionaryEntity;

public final class DictionaryController {
	private static List<DictionaryEntity> propositions;
	Transaction tx = null;
	Session session = null;

	public String getTextOf(int index) {
		for (DictionaryEntity dictionaryEntity : propositions)
			if (dictionaryEntity.getId() == index)
				return dictionaryEntity.getText();
		return null;
	}

	// public Integer getIndexOf(int index) {
	// for (DictionaryEntity dictionaryEntity : propositions)
	// if(dictionaryEntity.getId() == index)
	// return dictionaryEntity.getId();
	// return null;
	// }

	public Integer getIndexOf(String code) {
		code = code.toUpperCase();
		int convert = 0;
		for (int i = 0; i < code.length(); i++)
			convert += Math.pow(26, i) * (code.charAt(i) - 64);
		convert--;
		return propositions.get(convert).getId();
	}

	public List<DictionaryEntity> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<DictionaryEntity> propositions) {
		DictionaryController.propositions = propositions;
	}

	@SuppressWarnings("unchecked")
	public int retrieve() {
		session = MainClass.sessionFactory.openSession();
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
		session = MainClass.sessionFactory.openSession();
		tx = null;
		if(!text.matches(StaticController.dictionaryValidator)){
			throw new Exception("Bad input: dictionaryEntry has not matched");
		}
		try {
			tx = session.beginTransaction();
			entity = new DictionaryEntity(text);
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

	public boolean deletePropostion(int id) {
		Session session = MainClass.sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DictionaryEntity entity = (DictionaryEntity) session.get(DictionaryEntity.class, id);
			session.delete(entity);
			tx.commit();
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

	public DictionaryEntity updatePropostion(int id, String text) {
		Session session = MainClass.sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			DictionaryEntity entity = (DictionaryEntity) session.get(DictionaryEntity.class, id);
			entity.setText(text);
			session.update(entity);
			tx.commit();
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

	public String getCodeOf(int index) {
		String res = "";
		for (int i = 0; i < propositions.size(); i++)
			if (propositions.get(i).getId() == index)
				res = convertHexbiToDec(i);
		return res;
	}

	public String convertHexbiToDec(int index) {
		String res = "";
		for (int remain, j = index + 1; j > 0; j /= 26) {
			remain = j % 26;
			res = ((char) (remain + 64)) + res;
		}
		return res;
	}

	@Override
	public String toString() {
		return propositions.toString();
	}

}
