package main.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import main.MainClass;
import main.model.entities.RulesHeaderEntity;
import main.model.entities.RulesItemEntity;

public class RulesController {

	
	private static List<RulesHeaderEntity> rules;
	Transaction tx = null;
	Session session = null;

	public List<RulesHeaderEntity> getRules() {
		return rules;
	}

	public void setRules(List<RulesHeaderEntity> rules) {
		RulesController.rules = rules;
	}

	@SuppressWarnings("unchecked")
	public int retrieve() {
		session = MainClass.sessionFactory.openSession();
		tx = null;
		try {
			tx = session.beginTransaction();
			rules = session.createQuery("from RulesHeaderEntity").list();
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
		return rules.size();
	}

	@Override
	public String toString() {
		return rules.toString();
	}

	public String[] getLargeStringRules() {
		String[] strArray = new String[rules.size()];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = rules.get(i).getId() + " | ";
			for (Iterator<RulesItemEntity> iterator = rules.get(i).getAntecedents().iterator(); iterator.hasNext();) {
				RulesItemEntity ruleItem = (RulesItemEntity) iterator.next();
				strArray[i] += (ruleItem.isNegated() ? "NO " : "SI ")
						+ MainClass.dictionaryController.getTextOf(ruleItem.getAntecedent());
				if (iterator.hasNext())
					strArray[i] += " Y ";
			}
			strArray[i] += " IMPLICA QUE " + (rules.get(i).isNegated() ? "NO " : "SI ")
					+ MainClass.dictionaryController.getTextOf(rules.get(i).getConsecuent());
		}
		return strArray;
	}

	public String[] getShortStringRules() {
		String[] strArray = new String[rules.size()];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = rules.get(i).getId() + " | ";
			for (Iterator<RulesItemEntity> iterator = rules.get(i).getAntecedents().iterator(); iterator.hasNext();) {
				RulesItemEntity ruleItem = (RulesItemEntity) iterator.next();
				strArray[i] += (ruleItem.isNegated() ? StaticController.uNEGATION : "")
						+ MainClass.dictionaryController.getCodeOf(ruleItem.getAntecedent());
				if (iterator.hasNext())
					strArray[i] += StaticController.uAND;
			}
			strArray[i] += " " + StaticController.uCONDITIONAL + " " + (rules.get(i).isNegated() ? StaticController.uNEGATION : "")
					+ MainClass.dictionaryController.getCodeOf(rules.get(i).getConsecuent());
		}
		return strArray;
	}

	public RulesHeaderEntity addRule(String rule) throws Exception {
		RulesHeaderEntity rulesHeaderEntity = null;
//		rule = "!a&b>!c";
		rule = rule.replace(" ", "");
		Matcher matcher = StaticController.ruleValidatorPattern.matcher(rule);
		/*
		 * a | b -> c good a | b -> c bad, convert to two rules: a -> c, b -> c
		 * a -> b & c bad, convert to two rules: a -> b, a -> c a <-> b bad,
		 * convert to two rules: a -> b, b -> a
		 */
		if (!rule.matches(StaticController.ruleValidator)) {
			throw new Exception("Bad input: validatorPattern has not matched");
		}

		for (int i = 0; i <= matcher.groupCount(); i++) {
//			System.out.println(matcher.group(i) + " - " + matcher.start(i) + ":" + matcher.end(i));
		}

		matcher = StaticController.ruleBestPattern.matcher(rule);
		if (matcher.find()) {
			System.out.println("Is a good case");
			RulesItemEntity lastElement = null;
			Set<RulesItemEntity> antecedents = new HashSet<RulesItemEntity>();
			matcher = StaticController.rulePropPattern.matcher(rule);
			while (matcher.find()) {
				System.out.println((matcher.group(1).equals("!") ? "not " : "") + matcher.group(2));
				lastElement = new RulesItemEntity(MainClass.dictionaryController.getIndexOf(matcher.group(2)),
						matcher.group(1).equals("!"));
				antecedents.add(lastElement);
			}
			antecedents.remove(lastElement);
			rulesHeaderEntity = new RulesHeaderEntity(lastElement.getAntecedent(), lastElement.isNegated());
			rulesHeaderEntity.setAntecedents(antecedents);
			System.out.println(rulesHeaderEntity);
			return addRule(rulesHeaderEntity);
		}

		return rulesHeaderEntity;
	}
	
	public boolean deleteRule(int id) {
		Session session = MainClass.sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			RulesHeaderEntity entity = (RulesHeaderEntity) session.get(RulesHeaderEntity.class, id);
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

	private RulesHeaderEntity addRule(RulesHeaderEntity rulesHeaderEntity) {
		session = MainClass.sessionFactory.openSession();
		tx = null;
		if (rules.contains(rulesHeaderEntity)) {
			throw new EntityExistsException("The rule: " + rulesHeaderEntity + " already exists");
		}
		try {
			tx = session.beginTransaction();
			session.save(rulesHeaderEntity);
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
		rules.add(rulesHeaderEntity);
		return rulesHeaderEntity;
	}
	
	private RulesHeaderEntity updateRule(RulesHeaderEntity rulesHeaderEntity) {
		Session session = MainClass.sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(rulesHeaderEntity);
			tx.commit();
			return rulesHeaderEntity;
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

	public RulesHeaderEntity updateRule(int id, String text) {
		RulesHeaderEntity rulesHeaderEntity = null;
		Matcher matcher;
		if(StaticController.ruleBestPattern.matcher(text).find()){
			RulesItemEntity lastElement = null;
			Set<RulesItemEntity> antecedents = new HashSet<RulesItemEntity>();
			matcher = StaticController.rulePropPattern.matcher(text);
			while (matcher.find()) {
				System.out.println((matcher.group(1).equals("!") ? "not " : "") + matcher.group(2));
				lastElement = new RulesItemEntity(MainClass.dictionaryController.getIndexOf(matcher.group(2)),
						matcher.group(1).equals("!"));
				antecedents.add(lastElement);
			}
			antecedents.remove(lastElement);
			rulesHeaderEntity = new RulesHeaderEntity(lastElement.getAntecedent(), lastElement.isNegated());
			rulesHeaderEntity.setAntecedents(antecedents);
			System.out.println(rulesHeaderEntity);
			return updateRule(rulesHeaderEntity);
		}
		return null;
	}
}