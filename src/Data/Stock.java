package Data;

public enum Stock {
	APPLE("A"), BP("B"), CISCO("C"), DELL("D"), ERICSSON("E");

	Stock(String symbol) { 
		this.symbol = symbol;
	}

	public String symbol;

	public static Stock parse(Character c) {
		switch (Character.toUpperCase(c)) {
		case 'A':
			return APPLE;
		case 'B':
			return BP;
		case 'C':
			return CISCO;
		case 'D':
			return DELL;
		case 'E':
			return ERICSSON;
		}
		throw new RuntimeException("Stock parsing failed"); 
	}

	public static Stock parse(String s) {
		return parse(s.charAt(0));
	} 
	
}
