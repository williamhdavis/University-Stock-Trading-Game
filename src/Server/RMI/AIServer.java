/**
 * Created by William on 26/11/2016.
 */
package Server.RMI;

import Data.Card;
import Data.Config;
import Data.Share;
import Data.Stock;
import Server.Game;
import Server.Player;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AIServer extends UnicastRemoteObject implements Runnable, AIRemote
{
	/**
	 * The players instance variable is used to store the list of players in the game.
	 */
	private Player[] players;
	/**
	 * The game instance variable is used to store the game object.
	 */
	private Game game;

	/**
	 * The AIServer constructor is used to create a new instance of the server for AI players.
	 * @param game - The game object that is being played.
	 * @throws RemoteException - If there is no server connection.
     */
	public AIServer(Game game) throws RemoteException
	{
		System.out.println("Opening RMI registry server.");
		try
		{
			LocateRegistry.createRegistry(Config.RMIRegistryPort);
			System.out.println("RMI registry open.");
		}
		catch(RemoteException ex)
		{
			System.out.println("RMI registry could not be started.");
		}
		
		this.players = game.getPlayers();
		this.game = game;
	}

	/**
	 * The run method is used to run the server.
	 */
	public void run()
	{
		System.out.println("Starting RMI server for AI players.");
		try
		{
			Naming.rebind(Config.RMIServiceURL, this);
		}
		catch(RemoteException ex)
		{
			System.out.println("RMI server for AI players unable to start.");
		}
		catch(MalformedURLException ex)
		{
			System.out.println("RMI url is malformed. Please check the configuration.");
		}
	}

	/**
	 * The close method is used to close the server.
	 */
	public void close()
	{
		System.out.println("Closing RMI server for AI players.");
		try
		{
			Naming.unbind(Config.RMIServiceURL);
		}
		catch(RemoteException ex)
		{
			System.out.println("Unable to close RMI server for AI players.");
		}
		catch(NotBoundException ex)
		{
			System.out.println("RMI server for AI players is already closed.");
		}
		catch(MalformedURLException ex)
		{
			System.out.println("RMI url is malformed. Please check the configuration.");
		}
	}

	/**
	 * The createPlayer instance method is used to create a new ai player.
	 * @return - The player position in the games player list.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int createPlayer() throws RemoteException
	{
		if(!this.game.isInProgress())
		{
			int i = 0;
			while(i < this.players.length)
			{
				if(this.players[i] == null)
				{
					break;
				}
				++i;
			}
			if(i < this.players.length)
			{
				System.out.println("Player connected.");
				this.players[i] = new AIPlayer(i);
				return i;
			}
			else
			{
				System.out.println("Connection refused: No more player spaces.");
				return -2;
			}
		}
		else
		{
			System.out.println("Connection refused: Game in progress.");
			return -1;
		}
	}

	/**
	 * The namePlayer instance method is used to set a players name.
	 * @param player - The position of the player in the games player list to set the name of.
	 * @param name - The name of the player to be set.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public void namePlayer(int player, String name) throws RemoteException
	{
		this.players[player].setName(name);
	}

	/**
	 * The destroyPlayer instance method is used to remove a player from the game.
	 * @param player - The position of the player in the games player list.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public void destroyPlayer(int player) throws RemoteException
	{
		this.players[player] = null;
	}

	/**
	 * The resetGame instance method is used to reset the game back to its default state.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public void resetGame() throws RemoteException
	{
		this.game.resetGame();
	}

	/**
	 * The readyUp instance method is used to set a player as ready.
	 * @param player - The position of the player in the games player list.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public void readyUp(int player) throws RemoteException
	{
		this.players[player].setReady(true);
		this.game.trigger();
	}

	/**
	 * The isReady instance method is used to check if a player is marked as ready.
	 * @param player - The position of the player in the games player list.
	 * @return - The status of the player.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public boolean isReady(int player) throws RemoteException
	{
		return this.players[player].isReady();
	}

	/**
	 * The getMoney instance method is used to get the money the player has.
	 * @param player - The position of the player in the games player list.
	 * @return - The amount of money the player has.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int getMoney(int player) throws RemoteException
	{
		return this.players[player].getMoney();
	}

	/**
	 * The getShares instance method is used to get the list of shares the player owns.
	 * @param player - The position of the player in the games player list.
	 * @return - The shares the player owns.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public List<Share> getShares(int player) throws RemoteException
	{
		return this.players[player].getShares();
	}

	/**
	 * The buyStock instance method is used to buy stock for the player.
	 * @param player - The position of the player in the games player list.
	 * @param stock - The stock that should be bought.
	 * @param price - The price of the stock to be bought.
	 * @param quantity - The number of shares to be bought.
	 * @return - A status code on if the buy worked.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int buyStock(int player, Stock stock, int price, int quantity) throws RemoteException
	{
		return this.players[player].buyStock(stock, price, quantity);
	}

	/**
	 *	The sellStock instance method is used to buy stock for the player.
	 * @param player - The position of the player in the games player list.
	 * @param stock - The stock that should be sold.
	 * @param price - The price the stock is sold for.
	 * @param quantity - The number of shares to be sold.
	 * @return - A status code on if the sell worked.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int sellStock(int player, Stock stock, int price, int quantity) throws RemoteException
	{
		return this.players[player].sellStock(stock, price, quantity);
	}

	/**
	 * The vote instance method is used to place a vote on a card that is on the table.
	 * @param player - The position of the player in the games player list.
	 * @param position - The position of the card in the card list.
	 * @param yes - If the vote is a yes or no.
	 * @return - A status code on if the vote worked.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int vote(int player, int position, boolean yes) throws RemoteException
	{
		return this.players[player].vote(position, yes);
	}

	/**
	 * The checkForMessages instance method is used to check if there are any messages waiting for the player.
	 * @param player - The position of the player in the games player list.
	 * @return - If there are messages waiting.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public boolean checkForMessages(int player) throws RemoteException
	{
		return ((AIPlayer)this.players[player]).checkForMessages();
	}

	/**
	 * The getMessages instance method is used to get the waiting messages for the player.
	 * @param player - The position of the player in the games player list.
	 * @return - The messages that were waiting.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public String[] getMessages(int player) throws RemoteException
	{
		return ((AIPlayer)this.players[player]).getMessages();
	}

	/**
	 * The getPrice instance method is used to get the price of a specific stock.
	 * @param stock - The stock to get the price for.
	 * @return - The price of the stock.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int getPrice(Stock stock) throws RemoteException
	{
		return this.game.getPrice(stock);
	}

	/**
	 * The getPrices instance method is used to get all the prices of the stocks available.
	 * @return - The list of prices.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public List<Share> getPrices() throws RemoteException
	{
		return this.game.getPrices();
	}

	/**
	 * The getRound instance method is used to get the current round number.
	 * @return - The current round number.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int getRound() throws RemoteException
	{
		return this.game.getRound();
	}

	/**
	 * The getPhase instance method is used to get the current phase.
	 * @return - The current phase.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public int getPhase() throws RemoteException
	{
		return this.game.getPhase();
	}

	/**
	 *	The isGameRunning instance method is used to check if a game is running.
	 * @return - The status of the game.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public boolean isGameRunning() throws RemoteException
	{
		return this.game.isInProgress();
	}

	/**
	 * The cardsOnTable instance method is used to get the list of cards on the table.
	 * @return - The list of cards on the table.
	 * @throws RemoteException - If there is no server connection.
	 */
	@Override
	public List<Card> cardsOnTable() throws RemoteException
	{
		return this.game.cardsOnTable();
	}
}
