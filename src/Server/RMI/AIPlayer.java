/**
 * Created by William Davis on 28/11/2016.
 */
package Server.RMI;

import Server.Player;

import java.util.LinkedList;

public class AIPlayer extends Player
{
    /**
     * The messageQueue instance variable is used to store the messages that are to be sent to the client.
     */
    private LinkedList<String> messageQueue;

    /**
     * The AIPlayer constructor is used to create a new AIPlayer.
     * @param playerNumber - The position of the player in the games player list.
     */
    public AIPlayer(int playerNumber)
    {
        super(playerNumber);
        this.messageQueue = new LinkedList<>();
    }

    /**
     * The sendMessages instance method is used to send messages to the players output.
     * @param messages - The messages to send.
     */
    @Override
    public void sendMessages(String[] messages)
    {
        for(String message: messages)
        {
            this.messageQueue.add(message);
        }
    }

    /**
     * The checkForMessages instance method is used to check if there are messages waiting for the client.
     * @return - If there are messages waiting.
     */
    public boolean checkForMessages()
    {
        return this.messageQueue.size() > 0;
    }

    /**
     * The getMessages instance method is used to get all the messages that are waiting.
     * @return - The array of messages that are waiting for the client.
     */
    public String[] getMessages()
    {
        LinkedList<String> temp = new LinkedList<>();
        temp.addAll(this.messageQueue);
        this.messageQueue.clear();
        return temp.toArray(new String[0]);
    }
}
