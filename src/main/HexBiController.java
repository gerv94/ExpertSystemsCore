package main;

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
			res += Math.pow(26, i) * (string.charAt(i) - 64);
		res--;
		return res;
	}
}
