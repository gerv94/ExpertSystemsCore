package main.controller;

import java.util.regex.Pattern;

public class StaticController {
	public static final String ruleValidator = "^ *(?:(\\!?[a-zA-Z]+)( *[\\&\\|] *\\!?[a-zA-Z]+)* *(\\<?\\>) *(\\!?[a-zA-Z]+))( *\\& *\\!?[a-zA-Z]+)* *$";
	public static final String ruleBest = "^ *(?:(\\!?[a-zA-Z]+)( ?\\& *\\!?[a-zA-Z]+)* *\\> *(\\!?[a-zA-Z]+)) *$";
	public static final String ruleProp = "(\\!?)([a-zA-Z]+)";
	public static final String dictionaryValidator = "^(?!no )(?:(?! no )[ \\w])+$";
	public static final Pattern ruleValidatorPattern = Pattern.compile(ruleValidator);
	public static final Pattern ruleBestPattern = Pattern.compile(ruleBest);
	public static final Pattern rulePropPattern = Pattern.compile(ruleProp);
	public static final Pattern dictionaryValidatorPattern = Pattern.compile(dictionaryValidator);

	public static final String uAND = "\u2227";
	public static final String uOR = "\u2228";
	public static final String uNEGATION = "\u00AC";
	public static final String uCONDITIONAL = "\u2192";
	public static final String uBICONDITIONAL = "\u2194";

	public static final String AND = "&";
	public static final String OR = "|";
	public static final String NEGATION = "!";
	public static final String CONDITIONAL = ">";
	public static final String BICONDITIONAL = "<>";
	
	//jflex - kup
}
