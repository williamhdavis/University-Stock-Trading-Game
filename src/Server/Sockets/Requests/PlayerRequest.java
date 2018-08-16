/**
 * Created by William Davis on 22/11/2016.
 */
package Server.Sockets.Requests;

import Server.Game;
import Server.Sockets.SocketPlayer;

public interface PlayerRequest
{
    /**
     * The execute instance method is used to execute a request.
     * @param player - The player executing the request.
     * @param game - The game the request is to be run on.
     * @return - Messages produced in running the request.
     */
    public String[] execute(SocketPlayer player, Game game);

    /**
     * The parse instance method is used to convert an input string to a executable command.
     * @param line - The input string.
     * @return - A new request object.
     */
    public static PlayerRequest parse(String line)
    {
        String[] items = line.trim().split("\\s+");
        switch(items[0].toUpperCase())
        {
            case "HELP":
                if(items.length > 1)
                {
                    return new HelpRequest(items[1]);
                }
                else
                {
                    return new HelpRequest("");
                }
            case "READY":
                return new ReadyRequest();
            case "STATUS":
                if(items.length > 1)
                {
                    return new StatusRequest(items[1]);
                }
                else
                {
                    return new StatusRequest("");
                }
            case "TABLE":
                if(items.length > 1)
                {
                    switch(items[1].toUpperCase())
                    {
                        case "STOCK":
                            return new ViewTableRequest(-1);
                        case "CARDS":
                            return new ViewTableRequest(1);
                        default:
                            return new InvalidRequest();
                    }
                }
                else
                {
                    return new ViewTableRequest(0);
                }
            case "HOLDINGS":
            case "HOLDING":
            case "HOLD":
                if(items.length > 1)
                {
                    if(items[1].toUpperCase().equals("ALL"))
                    {
                        return new ViewRequest(-1);
                    }
                    else
                    {
                        try
                        {
                            return new ViewRequest(Integer.parseInt(items[1]));
                        }
                        catch(NumberFormatException ex)
                        {
                            return new InvalidRequest();
                        }
                    }
                }
                else
                {
                    return new ViewRequest(-2);
                }
            case "BUY":
                if(items.length > 2)
                {
                    try
                    {
                        return new TradeRequest(items[1], Integer.parseInt(items[2]));
                    }
                    catch(NumberFormatException ex)
                    {
                        return new InvalidRequest();
                    }
                }
                else
                {
                    return new InvalidRequest();
                }
            case "SELL":
                if(items.length > 2)
                {
                    try
                    {
                        return new TradeRequest(items[1], Integer.parseInt(items[2]), false);
                    }
                    catch(NumberFormatException ex)
                    {
                        return new InvalidRequest();
                    }
                }
                else
                {
                    return new InvalidRequest();
                }
            case "VOTE":
                if(items.length > 2)
                {
                    return new VoteRequest(items[1], items[2]);
                }
                else
                    return new InvalidRequest();
            case "RESET":
                return new ResetRequest();
            case "LOGOUT":
            case "QUIT":
            case "EXIT":
            case "":
                return new LogoutRequest();
            default:
                return new InvalidRequest();
        }
    }
}
