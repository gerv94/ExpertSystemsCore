package jFlexCup.models;

/**
 * 
 * @author GermánEduardo
 *
 */
public class LiteralModel implements DiscretElement {
	private boolean negated;
	private String atomic;

	public LiteralModel(boolean negated, String atomic) {
		this.negated = negated;
		this.atomic = atomic.toUpperCase();
	}

	@Override
	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	public String getAtomic() {
		return atomic;
	}

	public void setAtomic(String atomic) {
		this.atomic = atomic;
	}

	@Override
	public void negate() {
		negated = !negated;
	}

	@Override
	public String toString() {
		return negated ? "!" + atomic : atomic;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LiteralModel)
			return ((LiteralModel) obj).atomic.equals(atomic) && ((LiteralModel) obj).negated == negated;
		return false;
	}
}
