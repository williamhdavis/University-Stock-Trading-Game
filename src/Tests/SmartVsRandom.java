/**
 * Created by William Davis on 02/12/2016.
 */
package Tests;

import Data.Config;
import Server.RMI.RandomAI;
import Server.RMI.SmartAI;
import Server.Server;
import Server.Player;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SmartVsRandom
{
    private static Server server;
    private static RandomAI randomAI;
    private static SmartAI smartAI;

    @BeforeClass
    public static void init()
    {

        try
        {
            server = new Server();
            new Thread(server).start();
            Thread.sleep(2000);
            randomAI = new RandomAI(false);
            new Thread(randomAI).start();
            smartAI = new SmartAI(false);
            new Thread(smartAI).start();
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
    public void TestSmartVsRandom()
    {
        Player[] players = server.getGame().getPlayers();
        int smart = -1;
        int random = -1;
        int i = 0;
        while(i < players.length)
        {
            if(players[i] != null)
            {
                if(players[i].getName().equals("Smart AI"))
                {
                    smart = i;
                }
                else if(players[i].getName().equals("Random AI"))
                {
                    random = i;
                }
            }
            ++i;
        }
        int initial = (Config.StartingMoney + Config.StartingStock * Config.StartingStockPrice);
        System.out.println("Starting holdings value: " + Config.Currency + initial);
        System.out.println("RandomAI end holdings value: " + Config.Currency + players[random].getMoney());
        System.out.println("SmartAI end holdings value: " + Config.Currency + players[smart].getMoney());
        System.out.println();
        System.out.println("Percentage change from starting value:");
        System.out.printf("RandomAI percentage change: %.2f%%", ((players[random].getMoney() - initial) / (double)initial) * 100);
        System.out.println();
        System.out.printf("SmartAI percentage change: %.2f%%", ((players[smart].getMoney() - initial) / (double)initial) * 100);
        System.out.println();
    }
}
