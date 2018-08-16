# University - Stock Trading Game
A project from my third year at university as part of an advanced programming module. This assignment required the use of RMI and Sockets within Java to play a simple multiplayer trading game. This game is made up of a server, a "Random AI" which randomly decides how to act in the game, a "Smart AI" which bases its decisions on what it holds within the game and stock prices and finally a player client which is connected via an application such as PuTTY. In addition, JUnit tests had to be performed on a number of player and bot combinations to demonstrate the interfaces where working correctly. To run these tests, [JUnit](https://junit.org/junit4/) is required.

To run this project, the server should first be launched and then any human players should connect first. Finally, any desired bots can be connected, however as they ready up the moment they connect, usually, there can only be two bots at a time unless a human connects first.

#### Player Commands
- HELP (ALL) - *This command is used to get help information in the game. The ALL flag is used to get a list of all commands at any time in the game.*
- READY - *This command is used to ready up. It requires at least two players to be connected and cannot be used after a game ends until the game is reset.*
- STATUS (ROUND/PHASE) - *This command is used to check the current game state. By default, it shows both the round and phase, but the flags can be used to only show the desired one.*
- TABLE (STOCK/CARDS) - *This command is used to see details of the table. It includes the current stock prices and the influence cards currently on the table. The flags can be used to only view one of these similar to status.*
- HOLDINGS (ALL/<Player Number>) - *This command is used to see what assets a player is holding. Outside of view phases, only the players own holdings can be viewed. Inside view phases, all holdings can be viewed. The ALL flag can be used to show all players holdings or an integer player number can be provided to view a specific player’s holdings.*
- BUY <Stock> <Quantity> - *This command is used to buy shares in the trading phase. Stock should either be a single letter or the name of a stock that is available. Quantity must be an integer.*
- SELL <Stock> <Quantity> - *This command is used to sell shares in the trading phase. Stock should either be a single letter or the name of a stock that is available. Quantity must be an integer.*
- VOTE <Card> <Status> - *This command is used to vote for a card in the voting phase. The card must be either a single letter and the effect of the card without spaces or the full name of the stock and the effect of the card without spaces. The status must be either a YES or a NO.*
- RESET - *This command is used to reset the game after a winner has been announced.*
- LOGOUT - *This command is used to disconnect from the server. If used during a game, the game will automatically end and need resetting.*
