/**
 * Created by William on 18/11/2016.
 */
package Data;

public class Config
{
	/**
	 * The AILoopDelay class variable is used to set how many seconds a bot player should wait between actions.
	 */
	public static final int AILoopDelay = 2;

	/**
	 * The ServerPort class variable is used to set the port that the Socket based server should listen on.
	 */
	public static final int ServerPort = 8888;

	/**
	 * The RMIServerName class variable is used to set the server name where the RMI service will be made available.
	 */
	public static final String RMIServerName = "localhost";
	/**
	 * The RMIRegistryPort class variable is used to set the port the RMI based registry server should listen on.
	 */
	public static final int RMIRegistryPort = 1099;
	/**
	 * The RMIServiceURL class variable is used to set the full URL to the RMI registry entry for this server.
	 */
	public static final String RMIServiceURL = "//" + Config.RMIServerName + ":" + Config.RMIRegistryPort + "/Game";

	/**
	 * The PlayerLimit class variable is used to set the number of players that can connect to the game.
	 */
	public static final int PlayerLimit = 4;
	/**
	 * The PlayerPositions class variable is used to set the titles given to the winners of a game.
	 */
	public static final String[] PlayerPositions = {"1st", "2nd", "3rd", "4th"};

	/**
	 * The RoundLimit class variable is used to set the number of rounds that are played before the game ends.
	 */
	public static final int RoundLimit = 5;
	/**
	 * The ViewPhaseName class variable is used to set the title of the view phase in the game.
	 */
	public static final String ViewPhaseName = "View";
	/**
	 * The TradePhaseName class variable is used to set the title of the trade phase in the game.
	 */
	public static final String TradePhaseName = "Trade";
	/**
	 * The VotePhaseName class variable is used to set the title of the vote phase in the game.
	 */
	public static final String VotePhaseName = "Vote";
	/**
	 * The Phases class variable is used to set the names given to the phases that make up the game.
	 */
	public static final String[] Phases = {Config.ViewPhaseName, Config.TradePhaseName, Config.ViewPhaseName, Config.VotePhaseName};

	/**
	 * The Currency class variable is used to set the character used for the games currency.
	 */
	public static final char Currency = '£';

	/**
	 * The NumberOfVotes class variable is used to set the number of votes a player has in the vote phase.
	 */
	public static final int NumberOfVotes = 3;

	/**
	 * The TableCards class variable is used to set the number of cards that are placed on the table each round.
	 */
	public static final int TableCards = 5;

	/**
	 * The StartingMoney class variable is used to set the amount of money each player starts the game with.
	 */
	public static final int StartingMoney = 1000;
	/**
	 * The StartingStock class variable is used to set the amount of stocks a player is assigned randomly at the start of the game.
	 */
	public static final int StartingStock = 10;

	/**
	 * The StartingStockPrice class variable is used to set the staring price of each of the stocks.
	 */
	public static final int StartingStockPrice = 100;
	/**
	 * The StockBuyPrice class variable is used to set the amount that each stock costs extra to buy.
	 */
	public static final int StockBuyPrice = 3;

	/**
	 * The SmartAITradeBuffer class variable is used to set how far a stocks price must have moved from the starting price before the smart bot will trade it.
	 * Note: This limit is affected by influence cards
	 */
	public static final int SmartAITradeBuffer = 10;
	/**
	 * The SmartAIMoneyBuffer class variable is used to set how much money the smart bot will always keep back when buying stock.
	 */
	public static final int SmartAIMoneyBuffer = 100;
	/**
	 * The SmartAIRoundSpendLimit class variable is used to set the maximum that the smart bot can spend in a single round.
	 */
	public static final int SmartAIRoundSpendLimit = 1000;
	/**
	 * The SmartAIBuySplit class variable is used to set the ratio of stock that the smart bot will buy between the highest prefered and the second highest prefered buy options.
	 */
	public static final double SmartAIBuySplit = 0.5;
}
