package hibernate.models.entities;

public class RulesItemEntity {
	private int id;
	private int antecedent;
	private boolean negated;

	public RulesItemEntity() {
		// TODO Auto-generated constructor stub
	}

	public RulesItemEntity(int antecedent, boolean negated) {
		this.antecedent = antecedent;
		this.negated = negated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAntecedent() {
		return antecedent;
	}

	public void setAntecedent(int antecedent) {
		this.antecedent = antecedent;
	}

	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	@Override
	public boolean equals(Object obj) {
		boolean res = false;
		RulesItemEntity rulesItemEntity;
		if (obj instanceof RulesItemEntity){
			rulesItemEntity = (RulesItemEntity) obj;
			res = rulesItemEntity.getAntecedent() == antecedent && rulesItemEntity.isNegated() == negated;
		}
		return res;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return id + "|antecedent: " + (negated ? "!" : "") + antecedent;
	}
}
