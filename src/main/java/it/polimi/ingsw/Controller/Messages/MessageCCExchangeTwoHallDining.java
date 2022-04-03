package it.polimi.ingsw.Controller.Messages;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXCHANGE_TWO_HALL_DINING;

public class MessageCCExchangeTwoHallDining extends MessageCC{
    private String nicknamePlayer;
    private String colourHall1;
    private String colourHall2;
    private String colourDiningRoom1;
    private String colourDiningRoom2;


    public MessageCCExchangeTwoHallDining(int indexCard, String nicknamePlayer, String ch1, String ch2, String cdr1, String cdr2){
        super(CC_EXCHANGE_TWO_HALL_DINING, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourHall1 = ch1;
        this.colourHall2 = ch2;
        this.colourDiningRoom1 = cdr1;
        this.colourDiningRoom2 = cdr2;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public List<String> getColoursHall(){
        List<String> colours = new ArrayList<>();

        colours.add(colourHall1);
        colours.add(colourHall2);

        return colours;
    }

    public List<String> getColoursDiningRoom(){
        List<String> colours = new ArrayList<>();

        colours.add(colourDiningRoom1);
        colours.add(colourDiningRoom2);

        return colours;
    }
}
