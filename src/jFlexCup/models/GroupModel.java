package jFlexCup.models;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author GermánEduardo
 *
 */
public class GroupModel implements DiscretElement {
	private List<DiscretElement> elements = new ArrayList<>();
	private GroupModel father = null;
	private boolean negated;

	/**
	 * Cada grupo debe ser uniforme en los operadores - Solo AND o solo OR
	 * (uniform)
	 * 
	 */

	public GroupModel() {
		negated = false;
	}

	public GroupModel(GroupModel father, boolean negated) {
		this.father = father;
		this.negated = negated;
	}

	public void addElement(DiscretElement discretElement) {
		elements.add(discretElement);
	}

	public DiscretElement getElement(int index) {
		return elements.get(index);
	}

	public void doDeMorgan() {
		if (negated) {
			for (DiscretElement discretElement : elements)
				discretElement.negate();
			negated = !negated;
		}
		for (DiscretElement discretElement : elements)
			if (discretElement instanceof GroupModel)
				((GroupModel) discretElement).doDeMorgan();
	}

	@Override
	public void negate() {
		negated = !negated;
	}

	@Override
	public String toString() {
		return negated ? "!" + elements.toString() : elements.toString();
	}

	public GroupModel getFather() {
		return father;
	}

	@Override
	public boolean isNegated() {
		return negated;
	}

	public void regularize() {
		int init = 0, count = 0;
		GroupModel temp = new GroupModel();
		boolean hasOr = false;
		for (; count < elements.size(); count++) {
			if (elements.get(count) instanceof OperatorModel)
				if (((OperatorModel) elements.get(count)).getOperator().equals("|")) {
					hasOr = true;
					if (temp.elements.size() > 1) {
						elements.set(init, temp);
						count--;
						do {
							elements.remove(count);
							count--;
						} while (count > init);
					}
					temp = new GroupModel();
					init = count + 1;
					continue;
				}
			if (elements.get(count) instanceof GroupModel)
				((GroupModel) elements.get(count)).regularize();
			temp.addElement(elements.get(count));
		}
		if (temp.elements.size() > 1 && hasOr) {
			elements.set(init, temp);
			count--;
			do {
				elements.remove(count);
				count--;
			} while (count > init);
		}
	}

	public boolean isRegular() {
		OperatorModel operatorModel = null;
		for (DiscretElement discretElement : elements)
			if (discretElement instanceof OperatorModel) {
				if (operatorModel == null)
					operatorModel = (OperatorModel) discretElement;
				if (!operatorModel.getOperator().equals(((OperatorModel) discretElement).getOperator()))
					return false;
			}
		return true;
	}

	public boolean isOr() {
		for (DiscretElement discretElement : elements) {
			if (discretElement instanceof OperatorModel)
				if (!((OperatorModel) discretElement).getOperator().equals("|"))
					return false;
			if (discretElement instanceof GroupModel)
				if (!((GroupModel) discretElement).isOr())
					return false;
		}
		return true;
	}

	public boolean isAnd() {
		for (DiscretElement discretElement : elements) {
			if (discretElement instanceof OperatorModel)
				if (!((OperatorModel) discretElement).getOperator().equals("&"))
					return false;
			if (discretElement instanceof GroupModel)
				if (!((GroupModel) discretElement).isAnd())
					return false;
		}
		return true;
	}

	public boolean isDNF() {
		for (DiscretElement discretElement : elements) {
			if (discretElement instanceof OperatorModel)
				if (!((OperatorModel) discretElement).getOperator().equals("|"))
					return false;
			if (discretElement instanceof GroupModel)
				if (!((GroupModel) discretElement).isAnd() && !((GroupModel) discretElement).isDNF())
					return false;
		}
		return true;
	}

	public boolean isCNF() {
		for (DiscretElement discretElement : elements) {
			if (discretElement instanceof OperatorModel)
				if (!((OperatorModel) discretElement).getOperator().equals("&"))
					return false;
			if (discretElement instanceof GroupModel)
				if (!((GroupModel) discretElement).isOr() && !((GroupModel) discretElement).isCNF())
					return false;
		}
		return true;
	}

	public void order() {
		int end = 0, i;
		GroupModel temp;
		for (DiscretElement discretElement : elements)
			if (discretElement instanceof GroupModel)
				((GroupModel) discretElement).order();
		if (isAnd() || isOr()) {// si el grupo es AND o OR es por que
								// absolutamente todas las literales dentro se
								// unen con el mismo operador
			for (int count = 0; count < elements.size(); count++) {
				if (elements.get(count) instanceof GroupModel) {
					temp = (GroupModel) elements.get(count);
					i = end = count;
					end += temp.size();
					do {
						if (count == i)
							elements.set(count, temp.getElement(count - i));
						else
							elements.add(count, temp.getElement(count - i));
						count++;
					} while (count < end);
				}
			}
		} else if (isDNF()) {
			for (int count = 0; count < elements.size(); count++) {
				if (elements.get(count) instanceof GroupModel) {
					temp = (GroupModel) elements.get(count);
					if (temp.isDNF()) {
						i = end = count;
						end += temp.size();
						do {
							if (count == i)
								elements.set(count, temp.getElement(count - i));
							else
								elements.add(count, temp.getElement(count - i));
							count++;
						} while (count < end);
					}
				}
			}
		} else if (isCNF()) {
			for (int count = 0; count < elements.size(); count++) {
				if (elements.get(count) instanceof GroupModel) {
					temp = (GroupModel) elements.get(count);
					if (temp.isCNF()) {
						i = end = count;
						end += temp.size();
						do {
							if (count == i)
								elements.set(count, temp.getElement(count - i));
							else
								elements.add(temp.getElement(count - i));
							count++;
						} while (count < end);
					}
				}
			}
		}
	}

	public void makeDNF() {
		if (isAnd() || isOr())
			return;
		while (!isDNF()) {
			for (DiscretElement discretElement : elements) {
				if (discretElement instanceof GroupModel)
					((GroupModel) discretElement).makeDNF();
			}
			for (int count = 0; count < elements.size(); count++) {
				if (elements.get(count) instanceof OperatorModel) {
					if (((OperatorModel) elements.get(count)).getOperator().equals("&")) {
						distribute(count);
					}
				}
			}
		}
		order();
	}
	
	public void makeCNF() {
		if (isAnd() || isOr())
			return;
		while (!isCNF()) {
			for (DiscretElement discretElement : elements) {
				if (discretElement instanceof GroupModel)
					((GroupModel) discretElement).makeCNF();
			}
			for (int count = 0; count < elements.size(); count++) {
				if (elements.get(count) instanceof OperatorModel) {
					if (((OperatorModel) elements.get(count)).getOperator().equals("|")) {
						distribute(count);
					}
				}
			}
		}
		order();
	}

	private void distribute(int index) {
		GroupModel res, temp;
		DiscretElement elem1 = elements.get(index - 1), elem2 = elements.get(index + 1);
		if (elem1 instanceof LiteralModel && elem2 instanceof LiteralModel)
			return;
		if (elem2 instanceof GroupModel || elem1 instanceof GroupModel) {
			GroupModel groupModel = (GroupModel) (elem2 instanceof GroupModel ? elem2 : elem1);
			DiscretElement stone = groupModel.equals(elem2) ? elem1 : elem2;
			res = new GroupModel();
			for (DiscretElement discretElement : groupModel.elements) {
				if (!(discretElement instanceof OperatorModel)) {
					temp = new GroupModel();
					temp.addElement(discretElement);
					temp.addElement(elements.get(index));
					temp.addElement(stone);
					res.addElement(temp);
				} else {
					res.addElement(new OperatorModel(elements.get(index).equals("|") ? "&" : "|"));
				}
			}
			elements.set(index - 1, res);
			elements.remove(index);
			elements.remove(index);
		}
	}

	public int size() {
		return elements.size();
	}

	public List<DiscretElement> getElements() {
		return elements;
	}
	
	public GroupModel clone(){
		GroupModel res = new GroupModel();
		for (DiscretElement discretElement : elements)
			res.addElement(discretElement);
		res.father = father;
		res.negated = negated;
		return res;
	}
}