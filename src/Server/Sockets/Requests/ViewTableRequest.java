/**
 * Created by William Davis on 24/11/2016.
 */
package Server.Sockets.Requests;

import Data.Card;
import Data.Config;
import Data.Share;
import Server.Game;
import Server.Sockets.SocketPlayer;

import java.util.LinkedList;
import java.util.List;

public class ViewTableRequest implements PlayerRequest
{
    /**
     * The status instance variable is used to set what will be output by the request.
     */
    private int status;

    /**
     * The ViewTableRequest constructor is used to create a new view request for the table of the game.
     * @param status - The mode that will be used for the requests output.
     */
    public ViewTableRequest(int status)
    {
        this.status = status;
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
        if(game.isInProgress())
        {
            List<String> messages = new LinkedList<>();
            if(this.status >= 0)
            {
                String message = "";
                boolean isNotFirst = false;
                for(Card card : game.cardsOnTable())
                {
                    if(isNotFirst)
                    {
                        message += ", ";
                    } else
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
                messages.add("Current influence cards: " + message);
            }
            if(this.status <= 0)
            {
                String message = "";
                boolean isNotFirst = false;
                for(Share price : game.getPrices())
                {
                    if(isNotFirst)
                    {
                        message += ", ";
                    } else
                    {
                        isNotFirst = true;
                    }
                    message += price.getStock() + ": " + Config.Currency + price.getShares();
                }
                messages.add("Current stock prices: " + message);
            }
            return messages.toArray(new String[0]);
        }
        else
        {
            return new String[]{"No game in progress."};
        }
    }
}
