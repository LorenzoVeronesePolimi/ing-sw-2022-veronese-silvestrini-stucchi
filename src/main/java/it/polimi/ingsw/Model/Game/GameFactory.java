package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Player;

public class GameFactory {

    public GameFactory(Player .. players){}
    public Game createGame(Integer numPlayers){
        Game result;
        if (numPlayers == 2){
            result = new GameTwo(players[0], players[1]);
        }
        else if (numPlayers == 3){
            result = new GameThree(players[0], players[1], players[3]);
        }
        else if (numPlayers == 4){
            result = new GameFour(players[0], players[1], players[2], players[3]);
        }
        else{
            // exception
        }
    }
}
