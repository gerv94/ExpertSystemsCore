package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hibernate.models.entities.DictionaryEntity;
import hibernate.models.entities.RulesHeaderEntity;
import inferenceEngine.InferenceEngine;
import jFlexCup.models.RuleModel;
import jFlexCup.parser.ParserController;
import main.controllers.DictionaryController;
import main.controllers.RulesController;

/**
 * 
 * @author GermánEduardo
 *
 */
public class ConsoleController {

	public static SessionFactory sessionFactory = null;
	public static DictionaryController dictionaryController = new DictionaryController();
	public static RulesController rulesController = new RulesController();
	public static InferenceEngine inferenceEngine = new InferenceEngine();
	private boolean workWithDB = false;

	private static void configureSessionFactory() {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable thr) {
			System.err.println("Failed to create the SessionFactory object" + thr);
			throw new ExceptionInInitializerError(thr);
		}
	}

	public ConsoleController(boolean b) {
		if (b) {
			configureSessionFactory();
			dictionaryController.retrieve();
			rulesController.retrieve();
			dictionaryController.toString();
			inferenceEngine.fillElements();
		}
		workWithDB = b;
	}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::DictionaryController
	public DictionaryEntity addProposition(String string) {
		if (!workWithDB) {
			System.err.println("We are not working with data base");
			return null;
		}
		try {
			return dictionaryController.addPropostion(string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private DictionaryEntity updateProposition(String opc, String string) {
		if (!workWithDB) {
			System.err.println("We are not working with data base");
			return null;
		}
		try {
			return dictionaryController.updatePropostion(opc, string);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private boolean deleteProposition(String string1) {
		if (!workWithDB) {
			System.err.println("We are not working with data base");
			return false;
		}
		try {
			return dictionaryController.deletePropostion(string1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void printPropositions() {
		if (!workWithDB) {
			System.err.println("We are not working with data base");
			return;
		}
		for (DictionaryEntity dictionaryEntity : dictionaryController.getPropositions())
			System.out.println(dictionaryEntity);
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
				if (workWithDB)
					rulesController.addRule(rule);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!workWithDB)
			System.err.println("We are not working with data base");
	}

	private void printRules() {
		if (!workWithDB) {
			System.err.println("We are not working with data base");
			return;
		}
		for (RulesHeaderEntity rulesHeaderEntity : rulesController.getRules())
			System.out.println(rulesHeaderEntity.getText());
	}

	private void deleteRule(int id) {
		if (!workWithDB) {
			System.err.println("We are not working with data base");
			return;
		}
		rulesController.deleteRule(id);
	}

	public static void main(String[] args) {
		int opc = 0;
		String string1, string2;
		Scanner scanner = new Scanner(System.in);
		ConsoleController consoleController = new ConsoleController(true);
		do {
			System.out
					.println("::::::::::::::::::::::::::::::::::::::::\n" + "Insterfáz de consola del sistema experto\n"
							+ "1 - Menú Diccionario\n" + "2 - Menú Reglas\n" + "3 - Motor de inferencia\n" + "0 - Salir");
			opc = scanner.nextInt();
			switch (opc) {
			case 1:
				do {
					// scanner.nextLine();
					System.out.println(":::::::::::::::::::\n" + "Menú de Diccionario\n" + "1 - Ingresar Proposición\n"
							+ "2 - Modificar Proposición\n" + "3 - Eliminar Proposición\n"
							+ "4 - Mostrar Proposiciónes\n" + "5 - Leer Archivo\n"
							+ "6 - Retrieve Dictionary Controller\n" + "0 - Regresar");
					try {
						opc = scanner.nextInt();
					} catch (Exception e) {
						opc = -1;
					}
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
					case 6:
						dictionaryController.retrieve();
						break;
					default:
						break;
					}
				} while (opc != 0);
				opc = -1;
				break;
			case 2:
				do {
					scanner.nextLine();
					System.out.println("::::::::::::::\n" + "Menú de Reglas\n" + "1 - Ingresar Regla\n"
							+ "2 - Modificar Regla\n" + "3 - Eliminar Regla\n" + "4 - Mostrar Reglas\n"
							+ "5 - Leer Archivo\n" + "6 - Retrieve Rules Controller\n" + "0 - Regresar");
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
						scanner.nextLine();
						System.out.println("Ingresa el indice de la regla a eliminar");
						opc = scanner.nextInt();
						consoleController.deleteRule(opc);
						break;
					case 4:
						consoleController.printRules();
						break;
					case 5:
						consoleController.readRulesFromFile("sources/inputTest.txt");
						break;
					case 6:
						rulesController.retrieve();
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
