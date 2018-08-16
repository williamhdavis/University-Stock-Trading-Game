/**
 * Created by William Davis on 24/11/2016.
 */
package Server.Sockets.Requests;

import Data.Config;
import Data.Stock;
import Server.Game;
import Server.Sockets.SocketPlayer;

public class TradeRequest implements PlayerRequest
{
    /**
     * The stock instance variable is used to store the stock to bre traded.
     */
    private String stock;
    /**
     * The quantity instance variable is used to store the number of shares to be traded.
     */
    private int quantity;
    /**
     * The buy instance variable is used to store if it is a buy or sell request.
     */
    private boolean buy;

    /**
     * The TradeRequest constructor is used to create a new trade request for a player as a buy request.
     * @param stock - The stock to trade.
     * @param quantity - The amount of stock to trade.
     */
    public TradeRequest(String stock, int quantity)
    {
        this(stock, quantity, true);
    }

    /**
     * The TradeRequest constructor is used to create a new trade request for a player.
     * @param stock - The stock to trade.
     * @param quantity - The amount of stock to trade.
     * @param buy - If the request is to buy or sell.
     */
    public TradeRequest(String stock, int quantity, boolean buy)
    {
        this.stock = stock;
        this.quantity = quantity;
        this.buy = buy;
    }

    /**
     * The execute instance method is used to execute a request.
     * @param player - The player executing the request.
     * @param game - The game the request is to be run on.
     * @return - Messages produced in running the request.
     */
    @Override
    public String[] execute(SocketPlayer player, Game game)
    {
        if(Config.Phases[game.getPhase()].equals(Config.TradePhaseName))
        {
            try
            {
                Stock stock = Stock.parse(this.stock);
                boolean check = this.stock.toUpperCase().equals(stock.toString().toUpperCase());
                if(this.stock.length() == 1 || check)
                {
                    int price = game.getPrice(stock);
                    if(price > -1)
                    {
                        if(this.buy)
                        {
                            int buyStatus = player.buyStock(stock, price, this.quantity);
                            if(buyStatus == -1)
                            {
                                return new String[]{"You do not have enough money to make this stock purchase."};
                            }
                            else if(buyStatus == -2)
                            {
                                return new String[]{"No price found for stock."};
                            }
                            else
                            {
                                return new String[]{"Successfully bought " + this.quantity + " " + stock + " stock."};
                            }
                        }
                        else
                        {
                            int sellStatus = player.sellStock(stock, price, this.quantity);
                            if(sellStatus == -1)
                            {
                                return new String[]{"You do not have enough stock to make this sale."};
                            }
                            else if(sellStatus == -2)
                            {
                                return new String[]{"No price found for stock."};
                            }
                            else
                            {
                                return new String[]{"Successfully sold " + this.quantity + " " + stock + " stock."};
                            }
                        }
                    }
                    else
                    {
                        return new String[]{"No price found for stock."};
                    }
                }
                else
                {
                    return new String[]{"No such stock."};
                }
        
            }
            catch(RuntimeException ex)
            {
                return new String[]{"No such stock."};
            }
        }
        else
        {
            return new String[]{"Trading only allowed in the " + Config.TradePhaseName + " phase."};
        }
    }
}
