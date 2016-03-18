/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.ceti.memoriaTrabajo;

import java.util.Scanner;

import main.ConsoleController;
import mx.ceti.proposicion.ProposicionAtomica;

/**
 *
 * @author oliva
 */
public class Question {
     
    public static String YES = "si", NO="no", NO_SE ="no se";
    
    
    
    public static int asking(ProposicionAtomica pa){
        Scanner scanner = new Scanner(System.in);
        int value= 0;
		String opc = "";
		do {
			System.out.println(pa.getNombre() + "? (s/n/p)");
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
    
    
}
