/**
 * Created by William on 18/11/2016.
 */
package Server;

import Data.Config;
import Data.Share;
import Data.Stock;

import java.util.List;
import java.util.Random;

public abstract class Player
{
	/**
	 * The name instance variable is used to store the players name.
	 */
	private String name;
	/**
	 * The playerNumber instance variable is used to store the players number in the game.
	 */
	private int playerNumber;
	/**
	 * The ready instance variable is used to store if the player is ready for the next phase.
	 */
	private boolean ready;
	/**
	 * The money instance variable is used to store the amount of money that the player is holding.
	 */
	private int money;
	/**
	 * The shares instance variable is used to store the shares that the player is holding.
	 */
	private List<Share> shares;
	/**
	 * The votes instance variable is used to store the players votes on cards that are on the table.
	 */
	private int[] votes;

	/**
	 * The Player constructor is used to create a new player.
	 * @param playerNumber - The number of the player in the player list of the game.
     */
	public Player(int playerNumber)
	{
		this.name = "";
		this.playerNumber = playerNumber + 1;
		this.reset();
	}

	/**
	 * The reset instance method is used to reset the player back to starting conditions.
	 */
	public void reset()
	{
		this.ready = false;
		this.money = Config.StartingMoney;
		this.shares = Share.generate();
		this.votes = new int[Config.TableCards];
		Random r = new Random();
		int i = 0;
		while(i < Config.StartingStock)
		{
			this.shares.get(r.nextInt(this.shares.size())).addShares(1);
			++i;
		}
	}

	/**
	 * The setName instance method is used to set the players name.
	 * @param name - The players name.
     */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * The getName instance method is used to get the players name.
	 * @return - The players name.
     */
	public String getName()
	{
		return this.name;
	}

	/**
	 * The getPlayerNumber instance method is used to get the players number in the game.
	 * @return - The players number.
     */
	public int getPlayerNumber()
	{
		return this.playerNumber;
	}

	/**
	 * The getMoney instance method is used to get the money the player has.
	 * @return - The money the player has.
     */
	public int getMoney()
	{
		return this.money;
	}

	/**
	 * The getShares instance method is used to get the shares the player owns.
	 * @return - The list of shares.
     */
	public List<Share> getShares()
	{
		return this.shares;
	}

	/**
	 * The buyStock instance method is used to buy a number of shares of a stock.
	 * @param stock - The stock to buy shares in.
	 * @param price - The price the shares cost.
	 * @param quantity - The number to buy.
     * @return - A status code on if the buy worked.
     */
	public int buyStock(Stock stock, int price, int quantity)
	{
		if(this.money < (price + Config.StockBuyPrice) * quantity)
		{
			return -1;
		}
		else
		{
			for(Share s: this.shares)
			{
				if(s.getStock().equals(stock))
				{
					this.money -= (price + Config.StockBuyPrice) * quantity;
					s.addShares(quantity);
					return 0;
				}
			}
			return -2;
		}
	}

	/**
	 * The sellStock instance method is used to sell a number of shares of a stock.
	 * @param stock - The stock to sell shares in.
	 * @param price - The price that the shares get.
	 * @param quantity - The number to sell.
     * @return - A status code on if the sale worked.
     */
	public int sellStock(Stock stock, int price, int quantity)
	{
		for(Share s: this.shares)
		{
			if(s.getStock().equals(stock))
			{
				if(s.getShares() >= quantity)
				{
					s.addShares(quantity * -1);
					this.money += quantity * price;
					return 0;
				}
				else
				{
					return -1;
				}
			}
		}
		return -2;
	}

	/**
	 * The sellAll instance method is used to sell all the held stocks.
	 * @param prices - The list of prices for the stocks.
     */
	public void sellAll(Share[] prices)
	{
		for(Share s: this.shares)
		{
			for(Share p: prices)
			{
				if(s.getStock().equals(p.getStock()))
				{
					this.money += s.getShares() * p.getShares();
					s.addShares(s.getShares() * -1);
					break;
				}
			}
		}
	}

	/**
	 * The vote instance method is used to place a vote on a cards position.
	 * @param position - The position of the card.
	 * @param positive - If the vote is a yes or no vote.
     * @return - A status code on if the vote worked.
     */
	public int vote(int position, boolean positive)
	{
		if(countVotes() < Config.NumberOfVotes)
		{
			if(position < this.votes.length)
			{
				if(this.votes[position] == 0)
				{
					if(positive)
					{
						this.votes[position] = 1;
					}
					else
					{
						this.votes[position] = -1;
					}
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -2;
			}
		}
		else
		{
			return -3;
		}
	}

	/**
	 * The countVotes instance method is used count the votes the player has made this round.
	 * @return - The number of votes the player has made.
     */
	public int countVotes()
	{
		int count = 0;
		for(int vote: this.votes)
		{
			if(vote != 0)
			{
				++count;
			}
		}
		return count;
	}

	/**
	 * The getVotes instance method is used to get the list of votes the player has made.
	 * @return - The list of votes made.
     */
	public int[] getVotes()
	{
		return this.votes;
	}

	/**
	 * The resetVotes instance method is used to reset the players votes back to their default values.
	 */
	public void resetVotes()
	{
		this.votes = new int[Config.TableCards];
	}

	/**
	 * The isReady instance method is used to check if the player is set as ready.
	 * @return - If the player is set as ready.
     */
	public boolean isReady()
	{
		return this.ready;
	}

	/**
	 * The setReady instance method is used to change the players ready state.
	 * @param ready - The state to set the player to.
     */
	public void setReady(boolean ready)
	{
		this.ready = ready;
	}

	/**
	 * The sendMessages instance method is used to send messages to the players output.
	 * @param messages - The messages to send.
     */
	public abstract void sendMessages(String[] messages);
}
