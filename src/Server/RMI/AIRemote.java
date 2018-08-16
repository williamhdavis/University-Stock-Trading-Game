/**
 * Created by William on 26/11/2016.
 */
package Server.RMI;

import Data.Card;
import Data.Share;
import Data.Stock;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AIRemote extends Remote
{
	/**
	 * The createPlayer instance method is used to create a new ai player.
	 * @return - The player position in the games player list.
	 * @throws RemoteException - If there is no server connection.
     */
	public int createPlayer() throws RemoteException;

	/**
	 * The namePlayer instance method is used to set a players name.
	 * @param player - The position of the player in the games player list to set the name of.
	 * @param name - The name of the player to be set.
	 * @throws RemoteException - If there is no server connection.
     */
	public void namePlayer(int player, String name) throws RemoteException;

	/**
	 * The destroyPlayer instance method is used to remove a player from the game.
	 * @param player - The position of the player in the games player list.
	 * @throws RemoteException - If there is no server connection.
     */
	public void destroyPlayer(int player) throws RemoteException;

	/**
	 * The resetGame instance method is used to reset the game back to its default state.
	 * @throws RemoteException - If there is no server connection.
     */
	public void resetGame() throws RemoteException;

	/**
	 * The readyUp instance method is used to set a player as ready.
	 * @param player - The position of the player in the games player list.
	 * @throws RemoteException - If there is no server connection.
     */
	public void readyUp(int player) throws RemoteException;

	/**
	 * The isReady instance method is used to check if a player is marked as ready.
	 * @param player - The position of the player in the games player list.
	 * @return - The status of the player.
	 * @throws RemoteException - If there is no server connection.
     */
	public boolean isReady(int player) throws RemoteException;

	/**
	 * The getMoney instance method is used to get the money the player has.
	 * @param player - The position of the player in the games player list.
	 * @return - The amount of money the player has.
	 * @throws RemoteException - If there is no server connection.
     */
	public int getMoney(int player) throws RemoteException;

	/**
	 * The getShares instance method is used to get the list of shares the player owns.
	 * @param player - The position of the player in the games player list.
	 * @return - The shares the player owns.
	 * @throws RemoteException - If there is no server connection.
     */
	public List<Share> getShares(int player) throws RemoteException;

	/**
	 * The buyStock instance method is used to buy stock for the player.
	 * @param player - The position of the player in the games player list.
	 * @param stock - The stock that should be bought.
	 * @param price - The price of the stock to be bought.
	 * @param quantity - The number of shares to be bought.
	 * @return - A status code on if the buy worked.
     * @throws RemoteException - If there is no server connection.
     */
	public int buyStock(int player, Stock stock, int price, int quantity) throws RemoteException;

	/**
	 *	The sellStock instance method is used to buy stock for the player.
	 * @param player - The position of the player in the games player list.
	 * @param stock - The stock that should be sold.
	 * @param price - The price the stock is sold for.
	 * @param quantity - The number of shares to be sold.
	 * @return - A status code on if the sell worked.
     * @throws RemoteException - If there is no server connection.
     */
	public int sellStock(int player, Stock stock, int price, int quantity) throws RemoteException;

	/**
	 * The vote instance method is used to place a vote on a card that is on the table.
	 * @param player - The position of the player in the games player list.
	 * @param position - The position of the card in the card list.
	 * @param yes - If the vote is a yes or no.
	 * @return - A status code on if the vote worked.
	 * @throws RemoteException - If there is no server connection.
     */
	public int vote(int player, int position, boolean yes) throws RemoteException;

	/**
	 * The checkForMessages instance method is used to check if there are any messages waiting for the player.
	 * @param player - The position of the player in the games player list.
	 * @return - If there are messages waiting.
	 * @throws RemoteException - If there is no server connection.
     */
	public boolean checkForMessages(int player) throws RemoteException;

	/**
	 * The getMessages instance method is used to get the waiting messages for the player.
	 * @param player - The position of the player in the games player list.
	 * @return - The messages that were waiting.
	 * @throws RemoteException - If there is no server connection.
     */
	public String[] getMessages(int player) throws RemoteException;

	/**
	 * The getPrice instance method is used to get the price of a specific stock.
	 * @param stock - The stock to get the price for.
	 * @return - The price of the stock.
	 * @throws RemoteException - If there is no server connection.
     */
	public int getPrice(Stock stock) throws RemoteException;

	/**
	 * The getPrices instance method is used to get all the prices of the stocks available.
	 * @return - The list of prices.
	 * @throws RemoteException - If there is no server connection.
     */
	public List<Share> getPrices() throws RemoteException;

	/**
	 * The getRound instance method is used to get the current round number.
	 * @return - The current round number.
	 * @throws RemoteException - If there is no server connection.
     */
	public int getRound() throws RemoteException;

	/**
	 * The getPhase instance method is used to get the current phase.
	 * @return - The current phase.
	 * @throws RemoteException - If there is no server connection.
     */
	public int getPhase() throws RemoteException;

	/**
	 *	The isGameRunning instance method is used to check if a game is running.
	 * @return - The status of the game.
	 * @throws RemoteException - If there is no server connection.
     */
	public boolean isGameRunning() throws RemoteException;

	/**
	 * The cardsOnTable instance method is used to get the list of cards on the table.
	 * @return - The list of cards on the table.
	 * @throws RemoteException - If there is no server connection.
     */
	public List<Card> cardsOnTable() throws RemoteException;
}
