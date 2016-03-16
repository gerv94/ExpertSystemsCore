package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import jFlexCup.parser.ParserController;
import lexParser.models.RuleModel;

/**
 * 
 * @author GermánEduardo
 *
 */
public class ConsoleController {


	public ConsoleController() {

	}

	public void readFromFile(String strFile) {
		try {
			ParserController.parse(new File(strFile));
			ParserController.getParsedRules();
			processRules(ParserController.getParsedRules());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void processRules(List<RuleModel> rules) {
		for (RuleModel ruleModel : rules) {
			ruleModel.process();
//			System.out.println(ruleModel.toString());
		}
	}

	public static void main(String[] args) {
		int opc = 0;
		Scanner scanner = new Scanner(System.in);
		ConsoleController consoleController = new ConsoleController();
		do {
			System.out.println("::::::::::::::::::::::::::::::::::::::::");
			System.out.println("Insterfáz de consola del sistema experto");
			System.out.println("1 - Menú Diccionario");
			System.out.println("2 - Menú Reglas");
			System.out.println("0 - Salir");
			opc = scanner.nextInt();
			switch (opc) {
			case 1:

				break;
			case 2:
				do {
					System.out.println("::::::::::::::");
					System.out.println("Menú de Reglas");
					System.out.println("1 - Ingresar Regla");
					System.out.println("2 - Modificar Regla");
					System.out.println("3 - Eliminar Regla");
					System.out.println("4 - Mostrar Regla");
					System.out.println("5 - Leer Archivo");
					System.out.println("0 - Regresar");
					opc = scanner.nextInt();
					switch (opc) {
					case 1:

						break;
					case 2:

						break;
					case 3:

						break;
					case 4:

						break;
					case 5:
						consoleController.readFromFile("sources/inputTest.txt");
						break;
					default:
						break;
					}
				} while (opc != 0);
				opc = -1;
				break;
			default:
				break;
			}
		} while (opc != 0);
		scanner.close();
	}
}
