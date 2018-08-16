/**
 * Created by William Davis on 23/11/2016.
 */
package Server.Sockets.Requests;

import Data.Config;
import Server.Game;
import Server.Sockets.SocketPlayer;

public class StatusRequest implements PlayerRequest
{
    /**
     * The mode instance variable is used to store an extra argument for a status request.
     */
    private String mode;

    /**
     * The StatusRequest constructor is used to create a new status request.
     * @param mode - An extra argument to ajust the output of the request.
     */
    public StatusRequest(String mode)
    {
        this.mode = mode.toUpperCase();
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
            if(this.mode.equals("ROUND"))
            {
                return new String[]{"Current round: " + (game.getRound() + 1)};
            }
            else if(this.mode.equals("PHASE"))
            {
                return new String[]{"Current phase: " + Config.Phases[game.getPhase()]};
            }
            else
            {
                return new String[]{"Current round: " + (game.getRound() + 1), "Current phase: " + Config.Phases[game.getPhase()]};
            }
        }
        else
        {
            return new String[]{"No game in progress. Use \"READY\" to ready up for the game."};
        }
    }
}
