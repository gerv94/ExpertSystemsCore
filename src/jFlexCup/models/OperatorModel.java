package jFlexCup.models;

/**
 * 
 * @author GermánEduardo
 *
 */
public class OperatorModel implements DiscretElement {
	private String operator;

	public String getOperator() {
		return operator;
	}

	public OperatorModel(String operator) {
		if (operator.equals("|") || operator.equals("&"))
			this.operator = operator;
	}

	@Override
	public void negate() {
		operator = operator.equals("|") ? "&" : "|";
	}
	
	@Override
	public String toString() {
		return operator;
	}

	@Override
	public boolean isNegated() {
		return false;
	}
}
