/**
 * Created by William on 26/11/2016.
 */
package Server.RMI;

import Data.Card;
import Data.Config;
import Data.Stock;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public abstract class AIClient implements Runnable
{
	/**
	 * The server instance variable is used to store the server to connect through.
	 */
	protected AIRemote server;
	/**
	 * The playerNumber instance variable is used to store the player number of this player in the game.
	 */
	protected int playerNumber;
	/**
	 * The allowReset instance variable is used to track if the bot should reset the server at the end of games.
	 */
	protected boolean allowReset;
	/**
	 * The running instance variable is used to track if the client run loop should continue.
	 */
	protected boolean running;

	/**
	 * The AIClient constructor is used to create a new instance of an AI client.
	 * @param name - The name of the player.
	 * @param allowReset - Whether the bot should reset the game at the end.
     */
	public AIClient(String name, boolean allowReset)
	{
		System.out.println("Starting AI Player: " + name + ".");
		try
		{
			this.server = (AIRemote)Naming.lookup(Config.RMIServiceURL);
			this.playerNumber = this.server.createPlayer();
			this.server.namePlayer(this.playerNumber, name);
			this.allowReset = allowReset;
			this.running = false;
		}
		catch(RemoteException ex)
		{
			System.out.println("RMI client was unable to start.");
		}
		catch(NotBoundException ex)
		{
			System.out.println("RMI server is not running.");
		}
		catch(MalformedURLException ex)
		{
			System.out.println("RMI url is malformed. Please check the configuration.");
		}
	}

	/**
	 * The close instance method is used to close the AI client.
	 */
	public void close()
	{
		System.out.println("Closing AI Player: Player " + this.playerNumber + ".");
		try
		{
			this.server.destroyPlayer(this.playerNumber);
			this.running = false;
		}
		catch(RemoteException ex)
		{
			System.out.println("Failed to close AI player.");
		}
	}

	/**
	 * The run instance method is used to run the client.
	 */
	public abstract void run();

	/**
	 * The trade instance method is used to buy and sell shares.
	 * @param stock - The stock to trade shares in.
	 * @param quantity - The number of stocks to trade.
     * @param buy - If the trade is buy or sell.
     */
	protected void trade(Stock stock, int quantity, boolean buy)
	{
		String buyLabel = "sell";
		if(buy)
		{
			buyLabel = "buy";
		}
		String s = "";
		if(quantity == 1)
		{
			s = "s";
		}
		System.out.println("Attempting to " + buyLabel + " " + quantity + " " + stock + " stock" + s + ".");
		try
		{
			if(this.server.isGameRunning() && Config.Phases[this.server.getPhase()].equals(Config.TradePhaseName))
			{
				int price = this.server.getPrice(stock);
				if(price > -1)
				{
					if(buy)
					{
						int buyStatus = this.server.buyStock(this.playerNumber, stock, price, quantity);
						if(buyStatus == -1)
						{
							System.out.println("Buy failed. Not enough money to make this purchase.");
						}
						else if(buyStatus == -2)
						{
							System.out.println("Buy failed. No price found for given stock.");
						}
						else
						{
							System.out.println("Successfully bought " + quantity + " " + stock + " stock.");
						}
					}
					else
					{
						int sellStatus = this.server.sellStock(this.playerNumber, stock, price, quantity);
						if(sellStatus == -1)
						{
							System.out.println("Sell failed. Can't sell more stock than owned.");
						}
						else if(sellStatus == -2)
						{
							System.out.println("Sell failed. No price found for given stock.");
						}
						else
						{
							System.out.println("Successfully sold " + quantity + " " + stock + " stock.");
						}
					}
				}
				else
				{
					System.out.println("Buy failed. No price found for given stock.");
				}
			}
		}
		catch(RemoteException ex)
		{
			System.out.println("Trade failed. Please check the connection to the server.");
		}
	}

	/**
	 * The vote instance method is used to vote for a card position.
	 * @param position - The position of the card.
	 * @param yes - If the vote is a yes or no.
     */
	protected void vote(int position, boolean yes)
	{
		try
		{
			List<Card> cards = this.server.cardsOnTable();
			if(position < cards.size())
			{
				String cardName = cards.get(position).stock + cards.get(position).toString().substring(1);
				String vote = "NO";
				if(yes)
				{
					vote = "YES";
				}
				System.out.println("Attempting to vote " + vote + " on " + cardName + ".");
				int voteResult = this.server.vote(this.playerNumber, position, yes);
				if(voteResult == 0)
				{
					System.out.println("Vote accepted!");
				}
				else if(voteResult == -1)
				{
					System.out.println("Vote failed. Card already voted for.");
				}
				else if(voteResult == -2)
				{
					System.out.println("Vote failed. No valid card found.");
				}
				else
				{
					System.out.println("Vote failed. Out of votes.");
				}
			}
			else
			{
				System.out.println("Vote failed. No valid card found.");
			}
		}
		catch(RemoteException ex)
		{
			System.out.println("Vote failed. Please check the connection to the server.");
		}
	}

	/**
	 * The printMessages instance method is used to print any waiting messages to the screen.
	 * @throws RemoteException - If there is no server connection.
     */
	protected void printMessages() throws RemoteException
	{
		for(String message: this.server.getMessages(this.playerNumber))
		{
			System.out.println(message);
		}
		System.out.println("");
	}
}
