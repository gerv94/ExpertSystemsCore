package jFlexCup.parser;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.SymbolFactory;
import lexParser.models.DiscretElement;
import lexParser.models.GroupModel;
import lexParser.models.LiteralModel;
import lexParser.models.RuleModel;

/**
 * 
 * @author Germ�nEduardo
 *
 */
public abstract class ParserController {
	private static List<RuleModel> parsedRules = new ArrayList<>();

	protected static void addRule(RuleModel ruleModel) {
		parsedRules.add(ruleModel);
	}

	public static List<RuleModel> getParsedRules() {
		return parsedRules;
	}

	public static void parse(File file) throws Exception {
		parsedRules = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(file);
			JFLexScanner jfLexScanner = new JFLexScanner(fileReader);
			@SuppressWarnings("deprecation")
			SymbolFactory symbolFactory = new DefaultSymbolFactory();
			CupParser cupParser = new CupParser(jfLexScanner, symbolFactory);
			cupParser.parse();
			processRules();
		} catch (Exception e) {
			throw e;
		}
	}

	public static void parse(String string) throws Exception {
		parsedRules = new ArrayList<>();
		try {
			JFLexScanner jfLexScanner = new JFLexScanner(new StringReader(string));
			@SuppressWarnings("deprecation")
			SymbolFactory symbolFactory = new DefaultSymbolFactory();
			CupParser cupParser = new CupParser(jfLexScanner, symbolFactory);
			cupParser.parse();
			processRules();
		} catch (Exception e) {
			throw e;
		}
	}

	private static void processRules() throws Exception {
		List<RuleModel> tempList = new ArrayList<>();
		for (RuleModel ruleModel : parsedRules)
			ruleModel.process();
		for (RuleModel ruleModel : parsedRules) {
			if (ruleModel.getDirection() == 0) {
				tempList.add(new RuleModel(ruleModel.getLeft(), 1, ruleModel.getRight()));
				tempList.add(new RuleModel(ruleModel.getRight(), 1, ruleModel.getLeft()));
			} else if (ruleModel.getDirection() == 1) {
				tempList.add(new RuleModel(ruleModel.getLeft(), 1, ruleModel.getRight()));
			} else if (ruleModel.getDirection() == -1) {
				tempList.add(new RuleModel(ruleModel.getRight(), 1, ruleModel.getLeft()));
			}
		}
		parsedRules = tempList;
		tempList = new ArrayList<>();
		for (RuleModel ruleModel : parsedRules) {
			if(ruleModel.getLeft().isDNF()){
				for (DiscretElement discretElement : ruleModel.getLeft().getElements()) {
					if (discretElement instanceof GroupModel) {
						tempList.add(new RuleModel((GroupModel) discretElement, 1, ruleModel.getRight()));
					}else if(discretElement instanceof LiteralModel){
						tempList.add(new RuleModel((LiteralModel) discretElement, 1, ruleModel.getRight()));
					}
				}
			}else{
				tempList.add(ruleModel);
			}
		}
		parsedRules = tempList;
		tempList = new ArrayList<>();
		for (RuleModel ruleModel : parsedRules) {
			if(ruleModel.getRight().isAnd()){
				for (DiscretElement discretElement : ruleModel.getRight().getElements()) {
					if (discretElement instanceof LiteralModel) {
						tempList.add(new RuleModel(ruleModel.getLeft(), 1, discretElement));
					}
				}
			}else{
				//System.err.println("Error trying to proccess: " + ruleModel.toString());
				throw new Exception("Error trying to proccess: " + ruleModel.toString());
			}
		}
		parsedRules = tempList;
		System.out.println("List");
		for (RuleModel ruleModel : parsedRules)
			System.out.println(ruleModel);
	}

	public static void main(String[] args) {
		File file = new File("resources/inputTest.txt");
		try {
			ParserController.parse(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}