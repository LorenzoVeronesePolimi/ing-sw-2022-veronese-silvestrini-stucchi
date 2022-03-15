package it.polimi.ingsw.Model;

public class GameThree implements Game{
    // This will have specific methods for a Game of 3 players
    private Player player1, player2, player3;

    public GameTwo(Player p1, Player p2, Player p3){
        this.player1 = p1;
        this.player2 = p2;
        this.player3 = p3;
    }

    public void addPlayer(Player player){};

    public Board buildBoard(){};
}
