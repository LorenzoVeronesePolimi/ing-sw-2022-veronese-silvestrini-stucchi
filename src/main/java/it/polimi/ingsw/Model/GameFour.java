package it.polimi.ingsw.Model;

public class GameFour implements Game {
    // This will have specific methods for a Game of 4 players
    private Player player1, player2, player3, player4;

    public GameTwo(Player p1, Player p2, Player p3, Player p4){
        this.player1 = p1;
        this.player2 = p2;
        this.player3 = p3;
        this.player3 = p4;
    }

    public void addPlayer(Player player){}

    public Board buildBoard(){}
}
