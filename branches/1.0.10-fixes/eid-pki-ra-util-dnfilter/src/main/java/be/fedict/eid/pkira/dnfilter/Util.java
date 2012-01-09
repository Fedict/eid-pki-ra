package be.fedict.eid.pkira.dnfilter;

class Util {

	public static String escape(String value, String specialChars) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (specialChars.indexOf(ch) != -1) {
				valueBuilder.append('\\');
			}
			valueBuilder.append(ch);
		}

		return valueBuilder.toString();
	}
	
	public static String unescape(String value) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch != '\\') {
				valueBuilder.append(ch);
			} else {
				ch = value.charAt(i + 1);
				valueBuilder.append(ch);
				i++;
			}
		}

		return valueBuilder.toString();
	}
}
