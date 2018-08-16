/**
 * Created by William Davis on 24/11/2016.
 */
package Server.Sockets.Requests;

import Data.Card;
import Data.Config;
import Data.Stock;
import Server.Game;
import Server.Sockets.SocketPlayer;

import java.util.List;

public class VoteRequest implements PlayerRequest
{
    /**
     * The selected instance variable is used to store the card to vote on as a string.
     */
    private String selected;
    /**
     * The mode instance variable is used to store the vote as a yes or no as a string.
     */
    private String mode;

    /**
     * The VoteRequest constructor is used to create a new vote request for a card.
     * @param selected - The card as a string to be voted on.
     * @param mode - Yes or no mode for the vote as a string.
     */
    public VoteRequest(String selected, String mode)
    {
        this.selected = selected;
        this.mode = mode.toUpperCase();
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
        if(Config.Phases[game.getPhase()].equals(Config.VotePhaseName))
        {
            if(player.countVotes() < Config.NumberOfVotes)
            {
                int pos = this.selected.indexOf('+');
                if(pos == -1)
                {
                    pos = this.selected.indexOf('-');
                    if(pos == -1)
                    {
                        return new String[]{"Card format incorrect. Please try again."};
                    }
                }
                try
                {
                    String cardStock = this.selected.substring(0, pos);
                    int cardValue = Integer.parseInt(this.selected.substring(pos));
                    Stock stock = Stock.parse(cardStock);
                    boolean check = cardStock.toUpperCase().equals(stock.toString().toUpperCase());
                    if(cardStock.length() == 1 || check)
                    {
                        Card selected = new Card(stock, cardValue);
                        List<Card> cards = game.cardsOnTable();
                        int i = 0;
                        while(i < cards.size())
                        {
                            if(cards.get(i).equals(selected))
                            {
                                int result = 0;
                                switch(this.mode)
                                {
                                    case "Y":
                                    case "YES":
                                        result = player.vote(i, true);
                                        break;
                                    case "N":
                                    case "NO":
                                        result = player.vote(i, false);
                                        break;
                                    default:
                                        return new String[]{"Invalid vote type."};
                                }
                                if(result == 0)
                                {
                                    return new String[]{"Vote accepted!"};
                                }
                                else if(result == -1)
                                {
                                    return new String[]{"You have already placed a vote on this card."};
                                }
                                else if(result == -2)
                                {
                                    return new String[]{"Invalid card. Please try again."};
                                }
                                else
                                {
                                    return new String[]{"You have run out of votes."};
                                }
                            }
                            ++i;
                        }
                        return new String[]{"Selected card not found on table."};
                    }
                    else
                    {
                        return new String[]{"Card stock type not found."};
                    }
                }
                catch(NumberFormatException ex)
                {
                    return new String[]{"Card format incorrect. Please try again."};
                }
                catch(RuntimeException ex)
                {
                    return new String[]{"No such stock."};
                }
            }
            else
            {
                return new String[]{"You have already used all your votes."};
            }
        }
        else
        {
            return new String[]{"Voting only allowed in the " + Config.TradePhaseName + " phase."};
        }
    }
}
