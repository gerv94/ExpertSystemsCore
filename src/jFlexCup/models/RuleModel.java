package jFlexCup.models;

/**
 * 
 * @author GermánEduardo
 *
 */
public class RuleModel {
	private GroupModel left = new GroupModel();
	private GroupModel right = new GroupModel();
	private int direction;

	public RuleModel() {
		// TODO Auto-generated constructor stub
	}

	public RuleModel(GroupModel left, int direction, GroupModel right) {
		this.left = left;
		this.direction = direction;
		this.right = right;
	}

	public RuleModel(GroupModel left, int direction, DiscretElement discretElement) {
		this.left = left;
		this.direction = direction;
		this.right = new GroupModel();
		this.right.addElement(discretElement);
	}

	public RuleModel(LiteralModel discretElement, int direction, GroupModel right) {
		this.left = new GroupModel();
		this.direction = direction;
		this.right = right;
		this.left.addElement(discretElement);
	}

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
		System.out.println("::LEFT  |" + (left.isDNF() ? " DNF " : "") + (left.isCNF() ? " CNF " : "")
				+ (left.isAnd() ? " AND " : "") + (left.isOr() ? " OR " : ""));
		System.out.println("::RIGHT |" + (right.isDNF() ? " DNF " : "") + (right.isCNF() ? " CNF " : "")
				+ (right.isAnd() ? " AND " : "") + (right.isOr() ? " OR " : ""));
		System.out.println();
	}

	public String stringWithOutGroups() {
		String res = "";
		for (DiscretElement discretElement : left.getElements()) {
			res += discretElement.toString();
		}
		switch (direction) {
		case 1:
			res += ">";
			break;
		case 0:
			res += "<>";
			break;
		case -1:
			res += "<";
			break;
		}
		for (DiscretElement discretElement : right.getElements()) {
			res += discretElement.toString();
		}
		// TODO Auto-generated method stub
		return res;
	}
}
