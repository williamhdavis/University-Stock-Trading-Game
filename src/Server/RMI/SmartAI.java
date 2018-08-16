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

public class SmartAI extends AIClient
{
	/**
	 * The SmartAI constructor is used to create a new smart bot that resets the game at the end.
	 */
	public SmartAI()
	{
		super("Smart AI", true);
	}

	/**
	 * The SmartAI constructor is used to create a new smart bot while specifying if it can reset the game at the end.
	 * @param allowReset - If the bot can reset the game.
	 */
	public SmartAI(boolean allowReset)
	{
		super("Smart AI", allowReset);
	}

	/**
	 * The run instance method is used to run the smart AI main loop.
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
						List<Share> shares = this.server.getShares(this.playerNumber);
						List<Card> cards = this.server.cardsOnTable();
						// Give each stock a weighting based on the price change and any card effects on the table.
						int[] weights = new int[shares.size()];
						int i = 0;
						while(i < shares.size())
						{
							Stock stock = shares.get(i).getStock();
							int price = this.server.getPrice(stock);
							weights[i] = price - Config.StartingStockPrice;
							for(Card card: cards)
							{
								if(card.stock.equals(stock))
								{
									weights[i] += card.effect;
								}
							}
							++i;
						}

						// Sell stocks that have a negative weight that is larger than the buffer.
						i = 0;
						boolean first = true;
						while(i < shares.size())
						{
							if(weights[i] < Config.SmartAITradeBuffer * -1 && shares.get(i).getShares() > 0)
							{
								if(first)
								{
									first = false;
									System.out.println("Selling stock.");
								}
								this.trade(shares.get(i).getStock(), shares.get(i).getShares(), false);
							}
							++i;
						}
						// Buy shares in either one or two stocks that have weights larger than the buffer.
						i = 0;
						int max = Config.SmartAITradeBuffer;
						int maxPos = -1;
						int maxTwoPos = -1;
						while(i < shares.size())
						{
							if(weights[i] > max)
							{
								maxTwoPos = maxPos;
								max = weights[i];
								maxPos = i;
							}
							++i;
						}
						if(maxPos > -1)
						{
							System.out.println("");
							System.out.println("Buying stock.");
							int money = this.server.getMoney(this.playerNumber) - Config.SmartAIMoneyBuffer;
							if(money > Config.SmartAIRoundSpendLimit)
							{
								money = Config.SmartAIRoundSpendLimit;
							}
							if(maxTwoPos > -1)
							{
								Stock stockOne = shares.get(maxPos).getStock();
								Stock stockTwo = shares.get(maxTwoPos).getStock();
								int moneyOne = (int)(money * Config.SmartAIBuySplit);
								int moneyTwo = (int)(money * (1 - Config.SmartAIBuySplit));
								int quantityOne = (int) Math.floor(moneyOne / (double) (this.server.getPrice(stockOne) + Config.StockBuyPrice));
								int quantityTwo = (int) Math.floor(moneyTwo / (double) (this.server.getPrice(stockTwo) + Config.StockBuyPrice));
								if(quantityOne > 0)
								{
									this.trade(stockOne, quantityOne, true);
								}
								else
								{
									System.out.println("Not enough money to make desired purchase.");
								}
								if(quantityTwo > 0)
								{
									this.trade(stockTwo, quantityTwo, true);
								}
							}
							else
							{
								Stock stock = shares.get(maxPos).getStock();
								int quantity = (int) Math.floor(money / (double) (this.server.getPrice(stock) + Config.StockBuyPrice));
								if(quantity > 0)
								{
									this.trade(stock, quantity, true);
								}
								else
								{
									System.out.println("Not enough money to make desired purchase.");
								}
							}
						}
						System.out.println("Trading finished.");
						System.out.println("");
					}
					else if(Config.Phases[phase].equals(Config.VotePhaseName))
					{
						List<Share> shares = this.server.getShares(this.playerNumber);
						List<Card> cards = this.server.cardsOnTable();
						// Weight each card based on the shares owned in the stock and the impact of the card on the share value.
						int[] weights = new int[cards.size()];
						int i = 0;
						while(i < weights.length)
						{
							Card card = cards.get(i);
							int sharePos = -1;
							int j = 0;
							while(j < shares.size())
							{
								if(shares.get(j).getStock().equals(card.stock))
								{
									sharePos = j;
									break;
								}
								++j;
							}
							if(sharePos > -1)
							{
								weights[i] = shares.get(sharePos).getShares() * card.effect;
							}
							++i;
						}
						// Pick the three cards with the biggest impact to owned stock.
						i = 0;
						//int max = 0;
						int[] max = new int[Config.NumberOfVotes];
						int[] maxPos = new int[Config.NumberOfVotes];
						int j = 0;
						while(j < maxPos.length)
						{
							maxPos[j] = -1;
							++j;
						}
						while(i < cards.size())
						{
							int weight = Math.abs(weights[i]);
							int k = 0;
							while(k < max.length)
							{
								if(weight > max[k])
								{
									int p = max.length - 2;
									while(p > k)
									{
										max[p + 1] = max[p];
										maxPos[p + 1] = maxPos[p];
										--p;
									}
									max[k] = weight;
									maxPos[k] = i;
									k = max.length;
								}
								++k;
							}
							++i;
						}
						boolean first = true;
						for(int pos: maxPos)
						{
							if(pos > -1)
							{
								if(first)
								{
									first = false;
									System.out.println("Voting:");
								}
								boolean vote = weights[pos] > 0;
								this.vote(pos, vote);
							}
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
	 * The main class method is used to start a new smart ai.
	 * @param args - Program arguments.
	 */
	public static void main(String[] args)
	{
		new SmartAI().run();
	}
}
