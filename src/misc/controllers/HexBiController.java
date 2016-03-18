package misc.controllers;

/**
 * 
 * @author GermánEduardo
 *
 */
public abstract class HexBiController {

	/**
	 * 
	 * @param integer
	 * @return
	 */
	public static final String intToHexBi(Integer integer) {
		String res = "";
		for (int remain, j = integer + 1; j > 0; j /= 26) {
			remain = j % 26;
			res = ((char) (remain + 64)) + res;
		}
		return res;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static final Integer hexBiToInteger(String string) {
		string = string.toUpperCase();
		int res = 0;
		for (int i = 0; i < string.length(); i++)
			res += Math.pow(26, i) * (string.charAt(string.length() - (i + 1)) - 64);
		res--;
		return res;
	}
	
	public static void main(String[] args) {
		System.out.println(intToHexBi(120));		//d= 3 q=16 -> 
		System.out.println(hexBiToInteger("DQ"));	//
	}
}
