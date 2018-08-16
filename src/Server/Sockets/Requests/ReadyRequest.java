/**
 * Created by William Davis on 23/11/2016.
 */
package Server.Sockets.Requests;

import Server.Game;
import Server.Sockets.SocketPlayer;

public class ReadyRequest implements PlayerRequest
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
        String[] out = new String[]{};
        if(game.playerCount() < 2)
        {
            out = new String[]{"Not enough players to start the game. You have not been marked as ready."};
        }
        else if(!game.isInProgress() && game.getRound() > 0)
        {
            out = new String[]{"The game has ended. Please use \"RESET\" to prepare a new game."};
        }
        else if(!player.isReady())
        {
            player.setReady(true);
            int count = game.playerCount() - game.readyCount();
            String s = "";
            if(count != 1)
            {
                s = "s";
            }
            player.getService().sendMessages(new String[]{"Readied up. " + count + " player" + s + " left to ready up."});
            if(count == 0)
            {
                game.trigger();
            }
        }
        else
        {
            int count = game.playerCount() - game.readyCount();
            String s = "";
            if(count != 1)
            {
                s = "s";
            }
            out = new String[]{"You are already readied up. Please wait. " + count + " player" + s + " not readied up."};
        }
        return out;
    }
}
