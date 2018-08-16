/**
 * Created by William Davis on 22/11/2016.
 */
package Server.Sockets.Requests;

import Data.Config;
import Server.Game;
import Server.Player;
import Server.Sockets.SocketPlayer;

public class LogoutRequest implements PlayerRequest
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
        System.out.println("Player " + player.getPlayerNumber() + ", " + player.getName() + " logging out.");
        if(game.isInProgress())
        {
            game.stopGame();
            Player[] players = game.getPlayers();
            int i = 0;
            while(i < Config.PlayerLimit)
            {
                if(players[i] != null)
                {
                    if(!players[i].equals(player))
                    {
                        players[i].sendMessages(new String[]{"Game ended due to player logout.", "Player " + player.getPlayerNumber() + ", " + player.getName() + " logged out."});
                    }
                }
                ++i;
            }
        }
        return new String[]{"Closing connection."};
    }
}
