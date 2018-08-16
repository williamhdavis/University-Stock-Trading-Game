/**
 * Created by William Davis on 23/11/2016.
 */
package Server.Sockets;

import Server.Player;

public class SocketPlayer extends Player
{
    /**
     * The service instance variable is used to store the socket service that is used for the connection to the player client.
     */
    private PlayerService service;

    /**
     * The SocketPlayer constructor is used to create a new socket player for tracking a player.
     * @param playerNumber - The number of the player in the games player list.
     * @param service - The player service that has the connection to the player.
     */
    public SocketPlayer(int playerNumber, PlayerService service)
    {
        super(playerNumber);
        this.service = service;
    }

    /**
     * The getService instance method is used to get the connection to the player.
     * @return - The connection object of the player.
     */
    public PlayerService getService()
    {
        return this.service;
    }

    /**
     * The sendMessages instance method is used to send messages to the players output.
     * @param messages - The messages to send.
     */
    @Override
    public void sendMessages(String[] messages)
    {
        this.service.sendMessages(messages);
    }
}
