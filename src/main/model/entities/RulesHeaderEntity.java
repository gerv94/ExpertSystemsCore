package main.model.entities;

import java.util.Iterator;
import java.util.Set;

import main.MainClass;
import main.controller.StaticController;

public class RulesHeaderEntity {
	private int id;
	private int consecuent;
	private boolean negated;

	private Set<RulesItemEntity> antecedents;

	public RulesHeaderEntity() {
		// TODO Auto-generated constructor stub
	}

	public RulesHeaderEntity(int consecuent, boolean negated) {
		this.consecuent = consecuent;
		this.negated = negated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getConsecuent() {
		return consecuent;
	}

	public void setConsecuent(int consecuent) {
		this.consecuent = consecuent;
	}

	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	public Set<RulesItemEntity> getAntecedents() {
		return antecedents;
	}

	public void setAntecedents(Set<RulesItemEntity> antecedents) {
		this.antecedents = antecedents;
	}

	public String getText() {
		String string = "";
		for (Iterator<RulesItemEntity> iterator = antecedents.iterator(); iterator.hasNext();) {
			RulesItemEntity ruleItem = (RulesItemEntity) iterator.next();
			string += (ruleItem.isNegated() ? StaticController.NEGATION : "")
					+ MainClass.dictionaryController.getCodeOf(ruleItem.getAntecedent());
			if (iterator.hasNext())
				string += " " + StaticController.AND + " ";
		}
		string += " " + StaticController.CONDITIONAL + " " + (negated ? StaticController.NEGATION : "")
				+ MainClass.dictionaryController.getCodeOf(consecuent);
		return string;
	}

	@Override
	public String toString() {
		String string = "";
		for (Iterator<RulesItemEntity> iterator = antecedents.iterator(); iterator.hasNext();) {
			RulesItemEntity ruleItem = (RulesItemEntity) iterator.next();
			string += (ruleItem.isNegated() ? StaticController.uNEGATION : "")
					+ MainClass.dictionaryController.getCodeOf(ruleItem.getAntecedent());
			if (iterator.hasNext())
				string += " " + StaticController.uAND + " ";
		}
		string += " " + StaticController.uCONDITIONAL + " " + (negated ? StaticController.uNEGATION : "")
				+ MainClass.dictionaryController.getCodeOf(consecuent);
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		RulesHeaderEntity rulesHeaderEntity;
		if (obj instanceof RulesHeaderEntity) {
			rulesHeaderEntity = (RulesHeaderEntity)obj;
			if(consecuent == rulesHeaderEntity.getConsecuent()){
				System.out.println("same consecuent");
			}
			res = antecedents.equals(rulesHeaderEntity.getAntecedents()) && consecuent == rulesHeaderEntity.getConsecuent() && negated == rulesHeaderEntity.isNegated();
		}
		return res;
	}
}
