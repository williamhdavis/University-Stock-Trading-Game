/**
 * Created by William Davis on 22/11/2016.
 */
package Server.Sockets.Requests;

import Data.Config;
import Server.Game;
import Server.Sockets.SocketPlayer;

import java.util.LinkedList;
import java.util.List;

public class HelpRequest implements PlayerRequest
{
    /**
     * The argument instance variable is used to store any additional parameter sent with the help command.
     */
    private String argument;

    /**
     * The HelpRequest constructor is used to create a new help request.
     * @param argument - Any additional arguments for the help command.
     */
    public HelpRequest(String argument)
    {
        this.argument = argument.toUpperCase();
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
        boolean showAll = false;
        if(this.argument.equals("ALL"))
        {
            showAll = true;
        }
        List<String> messages = new LinkedList<>();
        messages.add("HELP:");
        messages.add("Get helpful commands for playing the game.");
        messages.add("    - Add \"ALL\" to view all commands at any time.");
        if(showAll || (!player.isReady() && (game.isInProgress() || game.getRound() == 0)))
        {
            messages.add("");
            messages.add("READY:");
            messages.add("Set your status to ready to vote for the game to advance. All players must be ready for the game to advance.");
        }
        if(showAll || game.isInProgress())
        {
            messages.add("");
            messages.add("STATUS:");
            messages.add("Get the current game status.");
            messages.add("    - Add \"ROUND\" to only get round information.");
            messages.add("    - Add \"PHASE\" to only get phase information.");
            messages.add("");
            messages.add("TABLE:");
            messages.add("View the influence cards currently on the table and the current stock prices.");
            messages.add("    - Add \"STOCK\" to only view the current stock prices.");
            messages.add("    - Add \"CARDS\" to only view the influence cards currently on the table.");
            messages.add("");
            messages.add("HOLDINGS:");
            if(showAll || Config.Phases[game.getPhase()].equals(Config.ViewPhaseName))
            {
                messages.add("Get the current holdings of any player in the game.");
                messages.add("    - Add \"ALL\" to view all players holdings.");
                messages.add("    - Add \"<Player Number>\" to view a specific players holdings.");
            }
            else
            {
                messages.add("Get your current holdings.");
            }
            messages.add("    Alts: HOLDING | HOLD");
            if(showAll || Config.Phases[game.getPhase()].equals(Config.TradePhaseName))
            {
                messages.add("");
                messages.add("BUY <Stock> <Quantity>:");
                messages.add("Buy a given quantity of a stock.");
                messages.add("");
                messages.add("SELL <Stock> <Quantity>:");
                messages.add("Sell a given quantity of a stock.");
            }
            if(showAll || Config.Phases[game.getPhase()].equals(Config.VotePhaseName))
            {
                messages.add("");
                messages.add("VOTE <Card> <Status>:");
                messages.add("Vote for an influence card on the table.");
            }
        }
        if(showAll || (!game.isInProgress() && game.getRound() > 0))
        {
            messages.add("");
            messages.add("RESET:");
            messages.add("Reset the game so a new game can be started. Can only be used at the end of a game.");
        }
        messages.add("");
        messages.add("LOGOUT:");
        messages.add("Disconnect from the game. If a game is in progress, it will be ended.");
        messages.add("    Alts: QUIT | EXIT");
        return messages.toArray(new String[0]);
    }
}
