/**
 * Created by William on 18/11/2016.
 */
package Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Share implements Serializable
{
	/**
	 * The stock instance variable is used to store the stock type this share is for.
	 */
	private Stock stock;
	/**
	 * The number instance variable is used to store how many shares of the stock are owned.
	 */
	private int number;

	/**
	 * The Share constructor is used to create a new share for a given stock.
	 * @param stock - The stock the share is for.
     */
	public Share(Stock stock)
	{
		this.stock = stock;
		this.number = 0;
	}

	/**
	 * The getStock instance method is used to get the stock type of the share.
	 * @return - The stock type of the share.
     */
	public Stock getStock()
	{
		return this.stock;
	}

	/**
	 * The getShares instance method is used to get the number of shares owned.
	 * @return - The number of shares.
     */
	public int getShares()
	{
		return this.number;
	}

	/**
	 * The addShares instance method is used to adjust the number of shares.
	 * @param number - The number of shares to change the stored value by.
     */
	public void addShares(int number)
	{
		this.number += number;
	}

	/**
	 * The toString instance method is used to return the share as a string.
	 * @return - A string containing the stock name and the number owned.
     */
	@Override
	public String toString()
	{
		return this.stock + ": " + this.number;
	}

	/**
	 * The generate class method is used to generate a list of shares with one for each stock type.
	 * @return - The list of shares.
     */
	public static List<Share> generate()
	{
		List<Share> shares = new LinkedList<>();
		for(Stock s: Stock.values())
		{
			shares.add(new Share(s));
		}
		return shares;
	}
}
