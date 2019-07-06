package simplyhired.utility;

import java.util.Set;
import java.util.StringTokenizer;

public class CleanText {

	public static String clean(String text) {
		StringTokenizer tokens = new StringTokenizer(text, " ");
		StringBuilder stringBuilder = new StringBuilder();
		while (tokens.hasMoreTokens()) {
			String newStr = cleanText(tokens.nextToken());
			if (newStr.equalsIgnoreCase(""))
				continue;
			else
				stringBuilder.append(newStr + " ");
		}
		return stringBuilder.toString().trim();
	}

	public static String simpleCleanText(String text) {
		text = clearSymbols(text);
		text = caseFold(text);
		return text.trim();
	}

	private static String cleanText(String text) {
		text = clearSymbols(text);
		text = caseFold(text);
		text = removeStopWords(text);
		return text;
	}

	private static String clearSymbols(String text) {
		return text.replaceAll("[~\\[!$%^&*(={}.|?><\\.,•)/:'\\]]", " ").trim();
	}

	private static String caseFold(String content) {
		return content.toLowerCase();
	}

	private static String removeStopWords(String content) {
		String newContent = content;
		Set<String> stopWords = StreamIO.getInstance().getStopWords();
		if(stopWords.contains(content.toLowerCase()))
			newContent = "";
		return newContent;
	}

	public static String removeLegalEntityTypes(String companyName) {
		Set<String> legalEntityTypes = StreamIO.getInstance().getLegalEntityTypes();
		for (String str : companyName.split(""))
			if(legalEntityTypes.contains(str.toLowerCase()))
				companyName = companyName.replace(str, "");
		return companyName;
	}

	public static String capitailizeWord(String str) {
		str = str.toLowerCase();
		StringBuffer s = new StringBuffer();

		// Declare a character of space
		// To identify that the next character is the starting of a new word
		char ch = ' ';
		for (int i = 0; i < str.length(); i++) {

			// If previous character is space and current
			// character is not space then it shows that
			// current letter is the starting of the word
			if (ch == ' ' && str.charAt(i) != ' ')
				s.append(Character.toUpperCase(str.charAt(i)));
			else
				s.append(str.charAt(i));
			ch = str.charAt(i);
		}
		return s.toString().trim();
	}
}
