/**
 * Created by William on 18/11/2016.
 */
package Server;

import Data.Card;
import Data.Config;
import Data.Share;
import Data.Stock;

import java.util.*;

public class Game
{
	/**
	 * The players instance variable is used to store the list of players for the game.
	 */
	private Player[] players;
	/**
	 * The inProgress instance variable is used to store if a game is in progress.
	 */
	private boolean inProgress;
	/**
	 * The round instance variable is used to store the current round.
	 */
	private int round;
	/**
	 * The phase instance variable is used to store the current phase.
	 */
	private int phase;
	/**
	 * The prices instance variable is used to store the current prices for each stock.
	 */
	private List<Share> prices;
	/**
	 * The deck instance variable is used to store the list of cards still in the deck.
	 */
	private List<Card> deck;
	/**
	 * The onTable instance variable is used to store the list of cards currently on the table.
	 */
	private List<Card> onTable;

	/**
	 * The Game constructor is used to create a new game instance to track game progress.
	 */
	public Game()
	{
		this.players = new Player[Config.PlayerLimit];
		this.resetGame();
	}

	/**
	 * The resetGame instance method is used to reset the game object back to its default state.
	 */
	public void resetGame()
	{
		this.inProgress = false;
		this.round = 0;
		this.phase = 0;
		this.prices = Share.generate();
		int i = 0;
		while(i < this.prices.size())
		{
			this.prices.get(i).addShares(Config.StartingStockPrice);
			++i;
		}
		int p = 0;
		while(p < Config.PlayerLimit)
		{
			if(this.players[p] != null)
			{
				this.players[p].reset();
			}
			++p;
		}
		this.deck = Card.createDeck();
		this.onTable = new LinkedList<>();
		Collections.shuffle(this.deck);
		this.dealToTable();
	}

	/**
	 * The dealToTable instance method is used to deal fresh cards from the deck to the table.
	 */
	private void dealToTable()
	{
		this.onTable.clear();
		if(this.deck.size() >= Config.TableCards)
		{
			this.onTable.addAll(this.deck.subList(0, Config.TableCards));
			this.deck = this.deck.subList(Config.TableCards, this.deck.size());
		}
	}

	/**
	 * The cardsOnTable instance method is used to get the list of cards currently on the table.
	 * @return - The list of cards on the table.
     */
	public List<Card> cardsOnTable()
	{
		return this.onTable;
	}

	/**
	 * The trigger method is used to respond to a player readying up.
	 */
	public void trigger()
	{
		if(!this.inProgress && this.round >= Config.RoundLimit)
		{
			this.sendMessages(new String[]{"The game has already ended."});
		}
		else
		{
			int count = this.playerCount();
			if(count > 1 && count - this.readyCount() == 0)
			{
				List<String> messages = new LinkedList<>();
				if(this.inProgress)
				{
					if(Config.Phases[this.phase].equals(Config.VotePhaseName))
					{
						messages.add("Voting ended. Counting votes.");
						// Sum all the votes from each player.
						int[] votes = new int[Config.TableCards];
						for(Player player : this.players)
						{
							if(player != null)
							{
								int[] playerVotes = player.getVotes();
								int i = 0;
								while(i < playerVotes.length)
								{
									if(playerVotes[i] != 0)
									{
										votes[i] += playerVotes[i];
									}
									++i;
								}
								player.resetVotes();
							}
						}
						// Pick the cards with more yes votes than no votes.
						List<Card> picked = new LinkedList<>();
						int pos = 0;
						while(pos < this.onTable.size())
						{
							if(votes[pos] > 0)
							{
								Card card = this.onTable.get(pos);
								picked.add(card);
								for(Share price : this.prices)
								{
									if(price.getStock().equals(card.stock))
									{
										price.addShares(card.effect);
										break;
									}
								}
							}
							++pos;
						}
						// Prepare the output string for the picked cards.
						String message = "";
						boolean isNotFirst = false;
						for(Card card : picked)
						{
							if(isNotFirst)
							{
								message += ", ";
							}
							else
							{
								isNotFirst = true;
							}
							message += card.stock;
							if(card.effect > 0)
							{
								message += "+";
							}
							message += card.effect;
						}
						this.dealToTable();
						messages.add("Picked influence cards: " + message);
						messages.add("");
					}
					++this.phase;
					if(this.phase >= Config.Phases.length)
					{
						this.phase = 0;
						++this.round;
						if(this.round >= Config.RoundLimit)
						{
							this.stopGame();
							Share[] prices = this.prices.toArray(new Share[0]);
							for(Player player : this.players)
							{
								if(player != null)
								{
									player.sellAll(prices);
								}
							}
							messages.add("Game over. Scores:");
							// Copy and sort the players list by total money.
							Player[] winners = Arrays.copyOf(this.players, this.players.length);
							Arrays.sort(winners, (Player o1, Player o2) -> {
								if(o1 == null && o2 == null)
								{
									return 0;
								}
								else if(o1 == null)
								{
									return 1;
								}
								else if(o2 == null)
								{
									return -1;
								}
								else
								{
									return o2.getMoney() - o1.getMoney();
								}
							});

							int lastScore = winners[0].getMoney();
							int num = 0;
							int i = 0;
							while(i < winners.length)
							{
								if(winners[i] == null)
								{
									break;
								}
								else
								{
									if(lastScore > winners[i].getMoney())
									{
										num = i;
									}
									messages.add(Config.PlayerPositions[num] + ": Player " + winners[i].getPlayerNumber() + ", " + winners[i].getName() + " - " + Config.Currency + winners[i].getMoney());
								}
								++i;
							}
						}
						else
						{
							messages.add("Round " + (this.round + 1) + " of " + Config.RoundLimit + ".");
							messages.add(Config.Phases[this.phase] + " phase started.");
						}
					}
					else
					{
						messages.add(Config.Phases[this.phase] + " phase started.");
					}

				}
				else
				{
					this.inProgress = true;
					messages.add("Game started. Round " + (this.round + 1) + " of " + Config.RoundLimit + ".");
					messages.add(Config.Phases[this.phase] + " phase started.");
				}
				this.sendMessages(messages.toArray(new String[0]));
				for(Player player : this.players)
				{
					if(player != null)
					{
						player.setReady(false);
					}
				}
			}
		}
	}

	/**
	 * The playerCount instance method is used to get the number of currently connected players.
	 * @return - The number of connected players.
     */
	public int playerCount()
	{
		int count = 0;
		for(Player player: this.players)
		{
			if(player != null)
			{
				++count;
			}
		}
		return count;
	}

	/**
	 * The readyCount instance method is used to get the number of players marked as ready.
	 * @return - The number of players marked as ready.
     */
	public int readyCount()
	{
		int count = 0;
		for(Player player: this.players)
		{
			if(player != null)
			{
				if(player.isReady())
				{
					++count;
				}
			}
		}
		return count;
	}

	/**
	 * The getPrice instance method is used to get the current price of a stock.
	 * @param stock - The stock to get the price of.
     * @return - The price of the stock.
     */
	public int getPrice(Stock stock)
	{
		for(Share share: this.prices)
		{
			if(share.getStock().equals(stock))
			{
				return share.getShares();
			}
		}
		return -1;
	}

	/**
	 * The getPrices instance method is used to get the full list of current prices.
	 * @return - The list of prices.
     */
	public List<Share> getPrices()
	{
		return this.prices;
	}

	/**
	 * The getPlayers instance method is used to get the list of currently connected players.
	 * @return - The list of connected players.
     */
	public Player[] getPlayers()
	{
		return this.players;
	}

	/**
	 * The getRound instance method is used to get the current round.
	 * @return - The current round number.
     */
	public int getRound()
	{
		return this.round;
	}

	/**
	 * The getPhase instance method is used to get the current phase.
	 * @return - The current phase.
     */
	public int getPhase()
	{
		return this.phase;
	}

	/**
	 * The isInProgress instance method is used to check if a game is in progress.
	 * @return - If the game is in progress.
     */
	public boolean isInProgress()
	{
		return this.inProgress;
	}

	/**
	 * The stopGame instance method is used to end the game.
	 */
	public void stopGame()
	{
		this.inProgress = false;
		if(this.round == 0)
		{
			++this.round;
		}
	}

	/**
	 * The sendMessages instance method is used to send a list of messages to all connected players.
	 * @param messages - The messages to send.
     */
	public void sendMessages(String[] messages)
	{
		for(Player player: this.players)
		{
			if(player != null)
			{
				player.sendMessages(messages);
			}
		}
	}
}
