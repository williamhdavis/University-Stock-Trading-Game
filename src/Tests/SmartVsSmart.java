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

public class SmartVsSmart
{
    private static Server server;
    private static SmartAI smartAIOne;
    private static SmartAI smartAITwo;

    @BeforeClass
    public static void init()
    {

        try
        {
            server = new Server();
            new Thread(server).start();
            Thread.sleep(2000);
            smartAIOne = new SmartAI(false);
            new Thread(smartAIOne).start();
            smartAITwo = new SmartAI(false);
            new Thread(smartAITwo).start();
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
    public void TestSmartVsSmart()
    {
        Player[] players = server.getGame().getPlayers();
        int smartOne = -1;
        int smartTwo = -1;
        int i = 0;
        while(i < players.length)
        {
            if(players[i] != null)
            {
                if(players[i].getName().equals("Smart AI"))
                {
                    if(smartOne == -1)
                    {
                        smartOne = i;
                    }
                    else
                    {
                        smartTwo = i;
                    }
                }
            }
            ++i;
        }
        int initial = (Config.StartingMoney + Config.StartingStock * Config.StartingStockPrice);
        System.out.println("Starting holdings value: " + Config.Currency + initial);
        System.out.println("SmartAI One end holdings value: " + Config.Currency + players[smartOne].getMoney());
        System.out.println("SmartAI Two end holdings value: " + Config.Currency + players[smartTwo].getMoney());
        System.out.println();
        System.out.println("Percentage change from starting value:");
        System.out.printf("SmartAI One percentage change: %.2f%%", ((players[smartOne].getMoney() - initial) / (double)initial) * 100);
        System.out.println();
        System.out.printf("SmartAI Two percentage change: %.2f%%", ((players[smartTwo].getMoney() - initial) / (double)initial) * 100);
        System.out.println();
    }
}
