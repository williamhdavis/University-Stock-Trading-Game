/**
 * Created by William Davis on 23/11/2016.
 */
package Server.Sockets.Requests;

import Data.Config;
import Server.Game;
import Server.Player;
import Server.Sockets.SocketPlayer;

public class ResetRequest implements PlayerRequest
{
    /**
     * The execute instance method is used to execute a request.
     * @param player - The player executing the request.
     * @param game - The game the request is to be run on.
     * @return - Messages produced in running the request.
     */
    @Override
    public String[] execute(SocketPlayer player, Game game)
    {
        if(!game.isInProgress())
        {
            if(game.getRound() != 0)
            {
                System.out.println("Game reset by Player " + player.getPlayerNumber() + ", " + player.getName() + ".");
                game.resetGame();
                Player[] players = game.getPlayers();
                int i = 0;
                while(i < Config.PlayerLimit)
                {
                    if(players[i] != null)
                    {
                        if(!players[i].equals(player))
                        {
                            players[i].sendMessages(new String[]{"Game reset by Player " + player.getPlayerNumber() + ", " + player.getName() + "."});
                        }
                    }
                    ++i;
                }
                return new String[]{"Game reset."};
            }
            else
            {
                return new String[]{"The game is already reset."};
            }
        }
        else
        {
            return new String[]{"A game is in progress. Please wait until the game is over."};
        }
    }
}
