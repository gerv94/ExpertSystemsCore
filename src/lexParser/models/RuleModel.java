package lexParser.models;

/**
 * 
 * @author GermánEduardo
 *
 */
public class RuleModel {
	private GroupModel left = new GroupModel();
	private GroupModel right = new GroupModel();
	private int direction;

	public GroupModel getLeft() {
		return left;
	}

	public void setLeft(GroupModel left) {
		this.left = left;
	}

	public GroupModel getRight() {
		return right;
	}

	public void setRight(GroupModel right) {
		this.right = right;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Indica si esta en Forma Normal Disjuntiva
	 * 
	 * @return boolean
	 */
	public boolean isDNF() {
		return false;
	}

	/**
	 * Indica si esta en Forma Normal Conjuntiva
	 * 
	 * @return boolean
	 */
	public boolean isCNF() {
		return false;
	}

	@Override
	public String toString() {
		String res = "";
		res += left.toString();
		switch (direction) {
		case 1:
			res += " > ";
			break;
		case 0:
			res += " <> ";
			break;
		case -1:
			res += " < ";
			break;
		}
		res += right.toString();
		return res;
	}

	public void process() {
		// TODO Resolve identities
		left.regularize();
		right.regularize();
		System.out.println("After regularize");
		System.out.println(toString());
		left.doDeMorgan();
		right.doDeMorgan();
		System.out.println("After DeMorgan");
		System.out.println(toString());
		left.order();
		right.order();
		System.out.println("After Order");
		System.out.println(toString());
		left.makeDNF();
		right.makeDNF();
		System.out.println("After make DNF");
		System.out.println(toString());
		System.out.println("left DNF " + left.isDNF());
		System.out.println("left CNF " + left.isCNF());
		System.out.println("left And " + left.isAnd());
		System.out.println("left Or " + left.isOr());
	}
}
