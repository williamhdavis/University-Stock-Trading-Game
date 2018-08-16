/**
 * Created by William Davis on 22/11/2016.
 */
package Server.Sockets.Requests;

import Server.Game;
import Server.Sockets.SocketPlayer;

public class InvalidRequest implements PlayerRequest
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
        return new String[]{"Invalid command. Please try again."};
    }
}
