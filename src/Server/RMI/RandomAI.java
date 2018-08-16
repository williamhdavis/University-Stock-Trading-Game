/**
 * Created by William on 26/11/2016.
 */
package Server.RMI;

import Data.Card;
import Data.Config;
import Data.Share;
import Data.Stock;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIClient
{
	/**
	 * The random instance variable is used to store a random number generator.
	 */
	private Random random;

	/**
	 * The RandomAI constructor is used to create a new random bot that resets the game at the end.
	 */
	public RandomAI()
	{
		super("Random AI", true);
		this.random = new Random();
	}

	/**
	 * The RandomAI constructor is used to create a new random bot while specifying if it can reset the game at the end.
	 * @param allowReset - If the bot can reset the game.
     */
	public RandomAI(boolean allowReset)
	{
		super("Random AI", allowReset);
		this.random = new Random();
	}

	/**
	 * The run instance method is used to run the random AI main loop.
	 */
	public void run()
	{
		this.running = true;
		while(this.running)
		{
			try
			{
				if(this.server.checkForMessages(this.playerNumber))
				{
					System.out.println("Waiting messages from server:");
					this.printMessages();
				}
				if(!this.server.isGameRunning() && this.server.getRound() == 0 && !this.server.isReady(this.playerNumber))
				{
					System.out.println("Readying up.");
					this.server.readyUp(this.playerNumber);
				}
				else if(!this.server.isGameRunning() && this.server.getRound() > 0)
				{
					if(allowReset)
					{
						System.out.println("Resetting game.");
						System.out.println("");
						this.server.resetGame();
					}
				}
				else if(!this.server.isReady(this.playerNumber))
				{
					int phase = this.server.getPhase();
					if(Config.Phases[phase].equals(Config.TradePhaseName))
					{
						System.out.println("Selling stock.");
						List<Share> shares = this.server.getShares(this.playerNumber);
						// Sell all shares in a random number of stocks.
						int count = this.getRandomNumber(1, (int) Math.ceil(shares.size() / 2.0));
						int i = 0;
						while(i < count)
						{
							int sell = this.getRandomNumber(0, shares.size());
							this.trade(shares.get(sell).getStock(), shares.get(sell).getShares(), false);
							++i;
						}
						System.out.println("");
						System.out.println("Buying stock.");
						List<Share> prices = this.server.getPrices();
						// But a random number of stocks, spending up to half the money held each time.
						count = this.getRandomNumber(1, (int) Math.ceil(shares.size() / 2.0) + 1);
						i = 0;
						while(i < count)
						{
							Stock stock = shares.get(this.getRandomNumber(0, shares.size())).getStock();
							int spend = this.server.getMoney(this.playerNumber) / 2;
							int quantity = (int) Math.floor(spend / (double) (this.server.getPrice(stock) + Config.StockBuyPrice));
							this.trade(stock, quantity, true);
							++i;
						}
						System.out.println("Trading finished.");
						System.out.println("");
					}
					else if(Config.Phases[phase].equals(Config.VotePhaseName))
					{
						System.out.println("Voting:");
						// Vote on a random number of cards.
						int votes = this.getRandomNumber(0, Config.NumberOfVotes + 1);
						int i = 0;
						while(i < votes)
						{
							List<Card> cards = this.server.cardsOnTable();
							int selected = this.getRandomNumber(0, cards.size());
							boolean yes = this.getRandomNumber(0, 2) == 1;
							this.vote(selected, yes);
							++i;
						}
						System.out.println("");
					}
					System.out.println("Readying up.");
					System.out.println("");
					this.server.readyUp(this.playerNumber);
				}
				try
				{
					Thread.sleep(1000 * Config.AILoopDelay);
				}
				catch(InterruptedException ex)
				{
					System.out.println("Failed to sleep.");
				}
			}
			catch(RemoteException ex)
			{
				System.out.println("Failed to run command. Please check the server connection.");
			}
		}
	}

	/**
	 * The getRandomNumber instance method is used to get a random integer between two given values.
	 * @param min - The lowest value (inclusive).
	 * @param max - The highest value (exclusive).
     * @return - The random number that was generated.
     */
	private int getRandomNumber(int min, int max)
	{
		return this.random.nextInt(max - min) + min;
	}

	/**
	 * The main class method is used to start a new random ai.
	 * @param args - Program arguments.
     */
	public static void main(String[] args)
	{
		new RandomAI().run();
	}
}
