package lexParser.lexParser;

import java_cup.runtime.Symbol;

%%
%{
    public void println(String str){
    	System.out.println(str);
    }
%}

/*DIRECTIVAS JLEX*/
%cupsym Sym 
%class JFLexScanner
%unicode
%public
%char
%line
%cup
%eofval{
  return new Symbol(Sym.EOF);
%eofval}
%eofclose
%ignorecase	
	
ENTER = \n+
SPACE = [\t\r\f ]+
CODE = [a-zA-Z]+
NOT = \!
OR = \|
AND = \&
CONDITIONAL = \>
BICONDITIONAL = \<{SPACE}?\>
REVERSECONDITIONAL = \<
STARTPARENTHESIS = \(
STOPPARENTHESIS = \)


%%
{SPACE} 				{//println("SPACE");
							}
{ENTER} 				{//println("ENTER"); System.out.println();		
							return new Symbol(Sym.Enter, new String(yytext()));
							}
{NOT} 					{//println("NOT:" +  yytext());					
							return new Symbol(Sym.Not, new String(yytext()));
							}
{CODE} 					{//println("CODE:" +  yytext());				
							return new Symbol(Sym.Code, new String(yytext()));
							}
{AND} 					{//println("AND:" + yytext());					
							return new Symbol(Sym.And, new String(yytext()));
							}
{OR} 					{//println("OR:" + yytext());					
							return new Symbol(Sym.Or, new String(yytext()));
							}
{CONDITIONAL} 			{//println("CONDITIONAL:" + yytext());			
							return new Symbol(Sym.Conditional, new String(yytext()));
							}
{BICONDITIONAL} 		{//println("BICONDITIONAL:" + yytext());		
							return new Symbol(Sym.Biconditional, new String(yytext()));
							}
{REVERSECONDITIONAL} 	{//println("REVERSECONDITIONAL:" + yytext());	
							return new Symbol(Sym.Reverseconditional, new String(yytext()));
							}
{STARTPARENTHESIS}		{//println("STARTPARENTHESIS:" + yytext());		
							return new Symbol(Sym.Startparenthesis, new String(yytext()));
							}
{STOPPARENTHESIS}		{//println("STOPPARENTHESIS:" + yytext());		
							return new Symbol(Sym.Stopparenthesis, new String(yytext()));
							}
. 						{System.out.println("ERROR:" +  yytext());	
							return new Symbol(Sym.error, new String(yytext()));
							}
