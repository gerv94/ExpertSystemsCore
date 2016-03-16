package jFlexCup.parser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.SymbolFactory;
import lexParser.models.RuleModel;

/**
 * 
 * @author GermánEduardo
 *
 */
public abstract class ParserController {
	private static List<RuleModel> parsedRules = new ArrayList<>();

	protected static void addRule(RuleModel ruleModel) {
		parsedRules.add(ruleModel);
	}

	public static List<RuleModel> getParsedRules(){
		return parsedRules;
	}

	public static void parse(File file) {
		parsedRules = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(file);
			JFLexScanner jfLexScanner = new JFLexScanner(fileReader);
			@SuppressWarnings("deprecation")
			SymbolFactory symbolFactory = new DefaultSymbolFactory();
			CupParser cupParser = new CupParser(jfLexScanner, symbolFactory);
			cupParser.parse();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		File file = new File("resources/inputTest.txt");
		ParserController.parse(file);
	}

}