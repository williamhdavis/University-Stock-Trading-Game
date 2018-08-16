/**
 * Created by William on 18/11/2016.
 */
package Server;

import Server.RMI.AIServer;
import Server.Sockets.PlayerServer;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Server implements Runnable
{
	/**
	 * The playerServer instance variable is used to store the instance of the player socket server.
	 */
	private PlayerServer playerServer;
	/**
	 * The aiServer instance variable is used to store the instance of the AI RMI server.
	 */
	private AIServer aiServer;
	/**
	 * The running instance variable is used to store if the server should be running.
	 */
	private boolean running;
	/**
	 * The game instance variable is used to store the game item.
	 */
	private Game game;

	/**
	 * The Server constructor is used to create the game server for both AI and players.
	 */
	public Server()
	{
		this.game = new Game();
		System.out.println("Starting game server.");
		// Launch the player server in a thread so it can run a loop to work without blocking.
		this.playerServer = new PlayerServer(this.game);
		new Thread(this.playerServer).start();
		// Launch the AI server in a thread so it doesn't block.
		this.aiServer = null;
		try
		{
			this.aiServer = new AIServer(this.game);
			new Thread(this.aiServer).start();
		}
		catch(RemoteException ex)
		{
			System.out.println("RMI server for ai failed to open.");
		}
	}

	/**
	 * The run method is used to start the main server loop.
	 */
	@Override
	public void run()
	{
		// Get console input to allow the server to be closed.
		Scanner input = new Scanner(System.in);
		this.running = true;
		while(this.running)
		{
			switch(input.nextLine().toUpperCase())
			{
				case "STOP":
				case "QUIT":
				case "EXIT":
					this.playerServer.close();
					if(aiServer != null)
					{
						this.aiServer.close();
					}
					this.running = false;
					System.exit(0);
			}
		}
	}

	/**
	 * The getGame instance method is used to get the game object. It is used for testing only.
	 * @return - The game object.
     */
	public Game getGame()
	{
		return this.game;
	}

	/**
	 * The close instance method is used to close the server.
	 */
	public void close()
	{
		this.playerServer.close();
		if(aiServer != null)
		{
			this.aiServer.close();
		}
		this.running = false;
	}

	/**
	 * The main class method is used to start a new server.
	 * @param args - Program arguments.
     */
	public static void main(String[] args)
	{
		new Server().run();
	}
}
