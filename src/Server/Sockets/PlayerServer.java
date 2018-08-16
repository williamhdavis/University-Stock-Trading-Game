/**
 * Created by William Davis on 22/11/2016.
 */
package Server.Sockets;

import Data.Config;
import Server.Player;
import Server.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerServer implements Runnable
{
    /**
     * The running instance variable is used to store if the player server is running.
     */
    private Boolean running;
    /**
     * The server instance variable is used to store the listening socket server for player connections.
     */
    private ServerSocket server;
    /**
     * The players instance variable is used to store the players objects in the game.
     */
    private Player[] players;
    /**
     * The game instance variable is used to store a reference to the game object.
     */
    private Game game;

    /**
     * The PlayerServer constructor is used to create a new instance of a player server.
     * @param game - The game object the server is to run on.
     */
    public PlayerServer(Game game)
    {
        System.out.println("Opening player server on port " + Config.ServerPort);
        try
        {
            this.server = new ServerSocket(Config.ServerPort);
            this.players = game.getPlayers();
            this.game = game;
        }
        catch(IOException ex)
        {
            System.out.println("Player server failed to open.");
        }
    }

    /**
     * The run instance method is used to run the player listening server and manage connections to the game from players.
     */
    public void run()
    {
        if(this.server != null)
        {
            this.running = true;
            while(this.running)
            {
                try
                {
                    Socket socket = this.server.accept();
                    if(!this.game.isInProgress())
                    {
                        int i = 0;
                        while(i < this.players.length)
                        {
                            if(this.players[i] == null)
                            {
                                break;
                            }
                            ++i;
                        }
                        if(i < this.players.length)
                        {
                            System.out.println("Player connected.");
                            PlayerService service = new PlayerService(socket, i, this.game);
                            this.players[i] = new SocketPlayer(i, service);
                            new Thread(service).start();
                        }
                        else
                        {
                            System.out.println("Connection refused: No more player spaces.");
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            out.println("Connection refused: No more player spaces.");
                            out.println();
                            out.close();
                            socket.close();
                        }
                    }
                    else
                    {
                        System.out.println("Connection refused: Game in progress.");
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("Connection refused: Game in progress.");
                        out.println();
                        out.close();
                        socket.close();
                    }
                }
                catch(IOException ex)
                {
                    System.out.println("Unable to access socket.");
                }
            }
        }
    }

    /**
     * The close instance method is used to close the player listening server.
     */
    public void close()
    {
        System.out.println("Closing player server.");
        this.running = false;
        if(this.server != null)
        {
            try
            {
                this.server.close();
            }
            catch(IOException ex)
            {
                System.out.println("Failed to close server.");
            }
        }
    }
}
