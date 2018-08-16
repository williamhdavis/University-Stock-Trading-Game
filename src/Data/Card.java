package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Card implements Comparable<Card>, Serializable {
 
	public static final int[] EFFECTS = new int[] { -20, -10, -5, +5, +10, +20 };

	public Stock stock;
	public int effect;

	public Card(Stock stock, int effect) {
		this.stock = stock;
		this.effect = effect;
	}

	public String toString() {
		return stock.symbol + (effect > 0 ? "+" : "") + effect;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return stock == other.stock && effect == other.effect;
	}

	@Override
	public int compareTo(Card other) {
		int result = stock.compareTo(other.stock);
		if (result == 0)
			result = Integer.compare(effect, other.effect);
		return result;
	}

	public static Card parse(String s) {
		Stock stock = Stock.parse(s.charAt(0));
		int effect = Integer.parseInt(s.substring(1));
		return new Card(stock, effect);
	}

	public static List<Card> createDeck() {
		List<Card> result = new ArrayList<>();
		for (Stock stock : Stock.values()) {
			for (int effect : EFFECTS) {
				result.add(new Card(stock, effect));
			}
		}
		return result;
	}

	public static void main(String[] args) {
		List<Card> deck = createDeck();
		System.out.println(deck);
	}
}
