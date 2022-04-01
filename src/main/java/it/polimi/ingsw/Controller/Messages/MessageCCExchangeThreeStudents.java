package it.polimi.ingsw.Controller.Messages;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXCHANGE_THREE_STUDENTS;

public class MessageCCExchangeThreeStudents extends Message{
    private String nicknamePlayer;
    private String colourCard1;
    private String colourCard2;
    private String colourCard3;
    private String colourHall1;
    private String colourHall2;
    private String colourHall3;


    public MessageCCExchangeThreeStudents(String nicknamePlayer, String cc1, String cc2, String cc3, String ch1, String ch2, String ch3){
        super(CC_EXCHANGE_THREE_STUDENTS);
        this.nicknamePlayer = nicknamePlayer;
        this.colourCard1 = cc1;
        this.colourCard2 = cc2;
        this.colourCard3 = cc3;
        this.colourHall1 = ch1;
        this.colourHall2 = ch2;
        this.colourHall3 = ch3;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public List<String> getColoursCard(){
        List<String> colours = new ArrayList<>();

        colours.add(colourCard1);
        colours.add(colourCard2);
        colours.add(colourCard3);

        return colours;
    }

    public List<String> getColoursHall(){
        List<String> colours = new ArrayList<>();

        colours.add(colourHall1);
        colours.add(colourHall2);
        colours.add(colourHall3);

        return colours;
    }
}
