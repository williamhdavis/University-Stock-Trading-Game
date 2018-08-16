/**
 * Created by William Davis on 24/11/2016.
 */
package Server.Sockets.Requests;

import Data.Config;
import Data.Share;
import Server.Game;
import Server.Player;
import Server.Sockets.SocketPlayer;

import java.util.LinkedList;
import java.util.List;

public class ViewRequest implements PlayerRequest
{
    /**
     * The player instance variable stores the number of the player to be viewed.
     */
    private int player;

    /**
     * The ViewRequest constructor is used to create a new view request.
     * @param player - The player to be viewed.
     */
    public ViewRequest(int player)
    {
        this.player = player;
    }

    /**
     * The sharesToString instance method is used to produce a single string containing all the share data.
     * @param player - The player to get the shares from.
     * @return - The string containing all the shares the player has.
     */
    private String sharesToString(Player player)
    {
        String s = "";
        boolean notFirst = false;
        for(Share share: player.getShares())
        {
            if(notFirst)
            {
                s += ", ";
            }
            else
            {
                notFirst = true;
            }
            s += share;
        }
        return s;
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
        List<String> messages = new LinkedList<>();
        if(!game.isInProgress())
        {
            messages.add("No game in progress.");
        }
        else if(this.player == -2)
        {
            messages.add("You are holding: " + Config.Currency + player.getMoney() + " and the following shares:");
            messages.add(this.sharesToString(player));
        }
        else if(this.player > 0 && this.player < Config.PlayerLimit)
        {
            --this.player;
            if(player.getPlayerNumber() == game.getPlayers()[this.player].getPlayerNumber())
            {
                messages.add("You are holding: " + Config.Currency + player.getMoney() + " and the following shares:");
                messages.add(this.sharesToString(player));
            }
            else if(Config.Phases[game.getPhase()].equals(Config.ViewPhaseName))
            {
                messages.add("Player " + game.getPlayers()[this.player].getPlayerNumber() + ", " + game.getPlayers()[this.player].getName() + " is holding: " + Config.Currency + player.getMoney() + " and the following shares:");
                messages.add(this.sharesToString(game.getPlayers()[this.player]));
            }
            else
            {
                messages.add("You cannot view other players holdings outside of the " + Config.ViewPhaseName + " phase.");
            }
        }
        else if(this.player == -1)
        {
            if(Config.Phases[game.getPhase()].equals(Config.ViewPhaseName))
            {
                boolean notFirst = false;
                for(Player play : game.getPlayers())
                {
                    if(play != null)
                    {
                        if(notFirst)
                        {
                            messages.add("");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        String label;
                        if(play.equals(player))
                        {
                            label = "You are holding: ";
                        } else
                        {
                            label = "Player " + play.getPlayerNumber() + ", " + play.getName() + " is holding: ";
                        }
                        messages.add(label + Config.Currency + play.getMoney() + " and the following shares:");
                        messages.add(this.sharesToString(play));
                    }
                }
            }
            else
            {
                messages.add("You cannot view other players holdings outside of the " + Config.ViewPhaseName + " phase.");
            }
        }
        else
        {
            messages.add("Invalid argument. Please try again.");
        }
        return messages.toArray(new String[0]);
    }
}
