package inferenceEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hibernate.models.entities.RulesHeaderEntity;
import hibernate.models.entities.RulesItemEntity;
import main.ConsoleController;

public class InferenceEngineModel {

	private int value;
	/*
	 * 1 = true 0 = pending -1= false
	 */
	private int code;
	private List<InferenceEngineModel> antecedents;

	public InferenceEngineModel(int code) {
		antecedents = new ArrayList<>();
		value = 0;
		this.code = code;
	}

	public int getValue() {
		if (antecedents.size() == 0)
			return value;
		else
			for (InferenceEngineModel inferenceEngineModel : antecedents)
				if (inferenceEngineModel.value == -1)
					return -1;
		for (InferenceEngineModel inferenceEngineModel : antecedents)
			if (inferenceEngineModel.value == 0)
				return 0;
		return 1;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int ask() {
		Scanner scanner = new Scanner(System.in);
		String opc = "";
		do {
			System.out.println(ConsoleController.dictionaryController.getTextOf(code) + "? (s/n/p)");
			opc = scanner.nextLine();
			switch (opc) {
			case "s":
			case "S":
				value = 1;
				break;
			case "n":
			case "N":
				value = -1;
				break;
			case "p":
			case "P":
				value = 0;
				break;
			default:
				opc = "";
				break;
			}
		} while (!opc.equals(""));
		scanner.close();
		return value;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof InferenceEngineModel)
			return ((InferenceEngineModel) obj).getCode() == this.code;
		return false;
	}

	@Override
	public String toString() {
		return ConsoleController.dictionaryController.getTextOf(code) + "("
				+ (value == 1 ? "VERDADERO" : (value == 0 ? "PENDENTE" : "FALSO")) + ")";
	}

}
