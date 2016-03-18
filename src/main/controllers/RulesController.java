package main.controllers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import javax.persistence.EntityExistsException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate.models.entities.RulesHeaderEntity;
import hibernate.models.entities.RulesItemEntity;
import main.ConsoleController;
import misc.controllers.HexBiController;
import misc.controllers.RegExController;

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
		session = ConsoleController.sessionFactory.openSession();
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
						+ ConsoleController.dictionaryController.getTextOf(ruleItem.getAntecedent());
				if (iterator.hasNext())
					strArray[i] += " Y ";
			}
			strArray[i] += " IMPLICA QUE " + (rules.get(i).isNegated() ? "NO " : "SI ")
					+ ConsoleController.dictionaryController.getTextOf(rules.get(i).getConsecuent());
		}
		return strArray;
	}

	public String[] getShortStringRules() {
		String[] strArray = new String[rules.size()];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = rules.get(i).getId() + " | ";
			for (Iterator<RulesItemEntity> iterator = rules.get(i).getAntecedents().iterator(); iterator.hasNext();) {
				RulesItemEntity ruleItem = (RulesItemEntity) iterator.next();
				strArray[i] += (ruleItem.isNegated() ? "!" : "") + HexBiController.intToHexBi(ruleItem.getAntecedent());
				if (iterator.hasNext())
					strArray[i] += "&";
			}
			strArray[i] += " > " + (rules.get(i).isNegated() ? "!" : "")
					+ HexBiController.intToHexBi(rules.get(i).getConsecuent());
		}
		return strArray;
	}

	public RulesHeaderEntity addRule(String rule) throws Exception {
		RulesHeaderEntity rulesHeaderEntity = null;
		// rule = "!a&b>!c";
		rule = rule.replace(" ", "");
		Matcher matcher;
		/*
		 * a | b -> c good a | b -> c bad, convert to two rules: a -> c, b -> c
		 * a -> b & c bad, convert to two rules: a -> b, a -> c a <-> b bad,
		 * convert to two rules: a -> b, b -> a
		 */

		matcher = RegExController.ruleBestPattern.matcher(rule);
		if (matcher.find()) {
			System.out.println("Is a good case");
			RulesItemEntity lastElement = null;
			Set<RulesItemEntity> antecedents = new HashSet<RulesItemEntity>();
			matcher = RegExController.rulePropPattern.matcher(rule);
			while (matcher.find()) {
				System.out.println((matcher.group(1).equals("!") ? "not " : "") + matcher.group(2));
				lastElement = new RulesItemEntity(HexBiController.hexBiToInteger(matcher.group(2)),
						matcher.group(1).equals("!"));
				antecedents.add(lastElement);
			}
			antecedents.remove(lastElement);
			rulesHeaderEntity = new RulesHeaderEntity(lastElement.getAntecedent(), lastElement.isNegated());
			rulesHeaderEntity.setAntecedents(antecedents);
			System.out.println(rulesHeaderEntity);
			return addRule(rulesHeaderEntity);
		} else {
			throw new Exception("Bad rule: error matching the rule");
		}
	}

	public boolean deleteRule(int id) {
		Session session = ConsoleController.sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			RulesHeaderEntity entity = (RulesHeaderEntity) session.get(RulesHeaderEntity.class, id);
			session.delete(entity);
			rules.remove(entity);
			tx.commit();
			return true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
//			e.printStackTrace();
			throw e;
		} finally {
			session.close();
			session = null;
		}
	}

	private RulesHeaderEntity addRule(RulesHeaderEntity rulesHeaderEntity) {
		session = ConsoleController.sessionFactory.openSession();
		tx = null;
		if (rules.contains(rulesHeaderEntity)) {
			throw new EntityExistsException("The rule: " + rulesHeaderEntity + " already exists");
		}
		try {
			tx = session.beginTransaction();
			session.save(rulesHeaderEntity);
			tx.commit();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
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
		Session session = ConsoleController.sessionFactory.openSession();
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
		// Rules
		return null;
	}
}
