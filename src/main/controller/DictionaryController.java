package main.controller;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import main.MainClassFromGUI;
import main.model.entities.DictionaryEntity;

public final class DictionaryController {
	private static List<DictionaryEntity> propositions;
	
	//Necesarios para las peticiones a la Base de Datos
	Transaction tx = null;
	Session session = null;

	public String getTextOf(int index) {
		for (DictionaryEntity dictionaryEntity : propositions)
			if (dictionaryEntity.getId() == index)
				return dictionaryEntity.getText();
		return null;
	}

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
		session = MainClassFromGUI.sessionFactory.openSession();
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
		session = MainClassFromGUI.sessionFactory.openSession();
		tx = null;
		if(!text.matches(StaticController.dictionaryValidator)){
			throw new Exception("Bad input: dictionaryEntry has not matched");
		}
		
		try {
			tx = session.beginTransaction();
			entity = new DictionaryEntity(text);
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

	public boolean deletePropostion(int id) {
		Session session = MainClassFromGUI.sessionFactory.openSession();
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
		Session session = MainClassFromGUI.sessionFactory.openSession();
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

	/**
	 * 
	 * @param index
	 * @return Devuelve el string equivalente al codigo en la lista del elemento con el indice index
	 */
	public String getCodeOf(int index) {
		String res = "";
		for (int i = 0; i < propositions.size(); i++)
			if (propositions.get(i).getId() == index)
				res = convertHexbiToDec(i);
		return res;
	}

	/**
	 * 
	 * @param i
	 * @return Devuelve el valor en string resultado de la conversion de un numero decimal (i) a HexaBiDecimal
	 */
	public String convertHexbiToDec(int i) {
		String res = "";
		for (int remain, j = i + 1; j > 0; j /= 26) {
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
