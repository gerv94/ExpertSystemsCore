package regex.parser;

import java.util.regex.Pattern;

public abstract class RegExController {
	public static final String ruleBest = "^ *(?:(\\!?[a-zA-Z]+)( ?\\& *\\!?[a-zA-Z]+)* *\\> *(\\!?[a-zA-Z]+)) *$";
	public static final Pattern ruleBestPattern = Pattern.compile(ruleBest);
	public static final String ruleProp = "(\\!?)([a-zA-Z]+)";
	public static final Pattern rulePropPattern = Pattern.compile(ruleProp);
	
	public static final String dictionaryRegEx = "^([a-zA-Z]+) *\\: *((?!no )(?:(?! no )[\\w ])+)$";
	public static final Pattern dictionaryRegExPattern = Pattern.compile(dictionaryRegEx);
}
