/**
 * Created by William Davis on 02/12/2016.
 */
package Tests;

import Data.Config;
import Server.RMI.RandomAI;
import Server.Server;
import Server.Player;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RandomVsRandom
{
    private static Server server;
    private static RandomAI randomAIOne;
    private static RandomAI randomAITwo;

    @BeforeClass
    public static void init()
    {

        try
        {
            server = new Server();
            new Thread(server).start();
            Thread.sleep(2000);
            randomAIOne = new RandomAI(false);
            new Thread(randomAIOne).start();
            randomAITwo = new RandomAI(false);
            new Thread(randomAITwo).start();
            Thread.sleep(5000);
            while(server.getGame().isInProgress() || server.getGame().getRound() == 0)
            {
                Thread.sleep(2000);
            }
        }
        catch(InterruptedException ex)
        {
            System.out.println("Failed to sleep.");
        }
    }

    @Test
    public void TestRandomVsRandom()
    {
        Player[] players = server.getGame().getPlayers();
        int randomOne = -1;
        int randomTwo = -1;
        int i = 0;
        while(i < players.length)
        {
            if(players[i] != null)
            {
                if(players[i].getName().equals("Random AI"))
                {
                    if(randomOne == -1)
                    {
                        randomOne = i;
                    }
                    else
                    {
                        randomTwo = i;
                    }
                }
            }
            ++i;
        }
        int initial = (Config.StartingMoney + Config.StartingStock * Config.StartingStockPrice);
        System.out.println("Starting holdings value: " + Config.Currency + initial);
        System.out.println("RandomAI One end holdings value: " + Config.Currency + players[randomOne].getMoney());
        System.out.println("RandomAI Two end holdings value: " + Config.Currency + players[randomTwo].getMoney());
        System.out.println();
        System.out.println("Percentage change from starting value:");
        System.out.printf("RandomAI One percentage change: %.2f%%", ((players[randomOne].getMoney() - initial) / (double)initial) * 100);
        System.out.println();
        System.out.printf("RandomAI Two percentage change: %.2f%%", ((players[randomTwo].getMoney() - initial) / (double)initial) * 100);
        System.out.println();
    }
}
