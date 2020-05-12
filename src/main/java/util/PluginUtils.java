package util;

import java.nio.charset.Charset;
import java.util.concurrent.ThreadLocalRandom;

public class PluginUtils {

	public static String capitalize(String input, String split) {
		String output = "";
		for (String s : input.split(split)) {
			output += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
		}
		return output.substring(0, output.length() - 1);
	}

	public static String centreText(String textToCentre) {
		return null;
	}

	public static String getAlphaNumericString(int n) {
		// length is bounded by 256 Character
		byte[] array = new byte[256];
		ThreadLocalRandom.current().nextBytes(array);

		String randomString = new String(array, Charset.forName("UTF-8"));

		// Create a StringBuffer to store the result
		StringBuffer r = new StringBuffer();

		// Append first 20 alphanumeric characters
		// from the generated random String into the result
		for (int k = 0; k < randomString.length(); k++) {

			char ch = randomString.charAt(k);

			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {

				r.append(ch);
				n--;
			}
		}

		// return the resultant string
		return r.toString();
	}

}