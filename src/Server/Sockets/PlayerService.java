/**
 * Created by William Davis on 22/11/2016.
 */
package Server.Sockets;

import Server.Game;
import Server.Sockets.Requests.LogoutRequest;
import Server.Sockets.Requests.PlayerRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerService implements Runnable
{
    /**
     * The socket instance variable is used to store the socket for the connection to the player.
     */
    private Socket socket;
    /**
     * The playerNumber instance variable is used to store the players position in the player list.
     */
    private int playerNumber;
    /**
     * The socketPlayer instance variable is used to store a reference to the player object.
     */
    private SocketPlayer player;
    /**
     * The game instance variable is used to store a reference to the game object.
     */
    private Game game;
    /**
     * The in instance variable is used to store a reader to receive messages from the player.
     */
    private Scanner in;
    /**
     * The out instance variable is used to store a writer to allow the server to send messages to the player.
     */
    private PrintWriter out;

    /**
     * The PlayerService constructor is used to create a new player service.
     * @param socket - The socket the client is connected on.
     * @param playerNumber - The position of the player object in the player list.
     * @param game - The game that the players are in.
     */
    public PlayerService(Socket socket, int playerNumber, Game game)
    {
        this.socket = socket;
        this.playerNumber = playerNumber;
        this.game = game;
        try
        {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException ex)
        {
            System.out.println("Failed to connect to socket.");
        }
    }

    /**
     * The run instance method is used to receive and respond to requests from the player client.
     */
    public void run()
    {
        this.player = (SocketPlayer)game.getPlayers()[playerNumber];
        if(this.player != null)
        {
            this.start();
            boolean check = true;
            while(check)
            {
                PlayerRequest request = PlayerRequest.parse(this.in.nextLine());
                String[] responses = request.execute(this.player, this.game);
                for(String response: responses)
                {
                    this.out.println(response);
                }
                this.out.println();
                if(request instanceof LogoutRequest)
                {
                    check = false;
                }
            }
            this.close();
        }
    }

    /**
     * The start instance method is used to prepare the player service when it first starts.
     */
    public void start()
    {
        this.out.println("Welcome to the game. You are Player " + (this.playerNumber + 1));
        this.out.println("Please enter your name:");
        this.player.setName(this.in.nextLine().trim());
        this.out.println();
        this.out.println("Welcome " + this.player.getName());
        this.out.println("If you need help, enter \"HELP\" at any time.");
        this.out.println();
    }

    /**
     * The close instance method is used to close the player connection.
     */
    public void close()
    {
        this.in.close();
        this.out.close();
        this.game.getPlayers()[this.playerNumber] = null;
        try
        {
            Thread.sleep(1000);
            this.socket.close();
        }
        catch(IOException ex)
        {
            System.out.println("Failed to close player connection.");
        }
        catch(InterruptedException ex)
        {}
    }

    /**
     * The sendMessages instance method is used to send messages to the players client.
     * @param messages - The messages to be sent.
     */
    public void sendMessages(String[] messages)
    {
        for(String message: messages)
        {
            this.out.println(message);
        }
        this.out.println();
    }
}
