package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hibernate.models.entities.DictionaryEntity;
import hibernate.models.entities.RulesHeaderEntity;
import jFlexCup.parser.ParserController;
import lexParser.models.RuleModel;
import main.controller.DictionaryController;
import main.controller.RulesController;

/**
 * 
 * @author GermánEduardo
 *
 */
public class ConsoleController {

	public static SessionFactory sessionFactory = null;
	public static DictionaryController dictionaryController = new DictionaryController();
	public static RulesController rulesController = new RulesController();

	private static void configureSessionFactory() {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable thr) {
			System.err.println("Failed to create the SessionFactory object" + thr);
			throw new ExceptionInInitializerError(thr);
		}
	}

	public ConsoleController() {
		configureSessionFactory();
		dictionaryController.retrieve();
		rulesController.retrieve();
		dictionaryController.toString();
	}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::DictionaryController
	public DictionaryEntity addProposition(String string) {
		try {
			return dictionaryController.addPropostion(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private DictionaryEntity updateProposition(String opc, String string) {
		try {
			return dictionaryController.updatePropostion(opc, string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private boolean deleteProposition(String string1) {
		try {
			return dictionaryController.deletePropostion(string1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void printPropositions() {
		System.out.println(dictionaryController.toString());
	}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::RulesController
	public void readRulesFromFile(String strFile) {
		try {
			ParserController.parse(new File(strFile));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		addParsedRules();
	}

	public void readRuleFromString(String string) {
		try {
			ParserController.parse(string);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		addParsedRules();
	}

	private void addParsedRules() {
		List<RuleModel> parsedRules = ParserController.getParsedRules();
		String rule;
		System.out.println();
		System.out.println("Generated rules");
		for (RuleModel ruleModel : parsedRules) {
			rule = ruleModel.stringWithOutGroups();
			System.out.println(rule);
			try {
				rulesController.addRule(rule);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void printRules() {
		for (RulesHeaderEntity rulesHeaderEntity : rulesController.getRules())
			System.out.println(rulesHeaderEntity.toString());
	}

	public static void main(String[] args) {
		int opc = 0;
		String string1, string2;
		Scanner scanner = new Scanner(System.in);
		ConsoleController consoleController = new ConsoleController();
		do {
			System.out
					.println("::::::::::::::::::::::::::::::::::::::::\n" + "Insterfáz de consola del sistema experto\n"
							+ "1 - Menú Diccionario\n" + "2 - Menú Reglas\n" + "3 - Procesar\n" + "0 - Salir");
			opc = scanner.nextInt();
			switch (opc) {
			case 1:
				do {
					System.out.println(":::::::::::::::::::\n" + "Menú de Diccionario\n" + "1 - Ingresar Proposición\n"
							+ "2 - Modificar Proposición\n" + "3 - Eliminar Proposición\n"
							+ "4 - Mostrar Proposiciónes\n" + "5 - Leer Archivo\n" + "0 - Regresar");
					opc = scanner.nextInt();
					switch (opc) {
					case 1:
						scanner.nextLine();
						System.out.println("Ingresa la proposición a agregar (CODIGO:TEXTO)");
						string1 = scanner.nextLine();
						consoleController.addProposition(string1);
						break;
					case 2:
						scanner.nextLine();
						System.out.println("Ingresa el codigo de la proposición a modificar");
						string1 = scanner.nextLine();
						System.out.println("Ingresa el nuevo texto");
						string2 = scanner.nextLine();
						consoleController.updateProposition(string1, string2);
						break;
					case 3:
						scanner.nextLine();
						System.out.println("Ingresa el codigo de la proposición a eliminar");
						string1 = scanner.nextLine();
						consoleController.deleteProposition(string1);
						break;
					case 4:
						consoleController.printPropositions();
						break;
					case 5:
						System.out.println("To be implemented");
						break;
					default:
						break;
					}
				} while (opc != 0);
				opc = -1;
				break;
			case 2:
				do {
					System.out.println("::::::::::::::\n" + "Menú de Reglas\n" + "1 - Ingresar Regla\n"
							+ "2 - Modificar Regla\n" + "3 - Eliminar Regla\n" + "4 - Mostrar Reglas\n"
							+ "5 - Leer Archivo\n" + "0 - Regresar");
					opc = scanner.nextInt();
					switch (opc) {
					case 1:
						scanner.nextLine();
						System.out.println("Ingresa la regla a agregar");
						string1 = scanner.nextLine();
						consoleController.readRuleFromString(string1);
						break;
					case 2:
						System.out.println("To be implemented");
						break;
					case 3:
						System.out.println("To be implemented");
						break;
					case 4:
						consoleController.printRules();
						break;
					case 5:
						consoleController.readRulesFromFile("sources/inputTest.txt");
						break;
					default:
						break;
					}
				} while (opc != 0);
				opc = -1;
				break;
			case 3:

				break;
			default:
				break;
			}
		} while (opc != 0);
		scanner.close();
		System.exit(0);
	}

}
