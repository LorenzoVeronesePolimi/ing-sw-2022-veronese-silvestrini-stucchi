package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXCHANGE_THREE_STUDENTS;

public class MessageCCExchangeThreeStudents extends MessageCC{
    private final String nicknamePlayer;
    private final String colourCard1;
    private final String colourCard2;
    private final String colourCard3;
    private final String colourHall1;
    private final String colourHall2;
    private final String colourHall3;


    public MessageCCExchangeThreeStudents(int indexCard, String nicknamePlayer, String colourCard1, String colourCard2,
                                          String colourCard3, String colourHall1, String colourHall2, String colourHall3){
        super(CC_EXCHANGE_THREE_STUDENTS, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourCard1 = colourCard1;
        this.colourCard2 = colourCard2;
        this.colourCard3 = colourCard3;
        this.colourHall1 = colourHall1;
        this.colourHall2 = colourHall2;
        this.colourHall3 = colourHall3;
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

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExchangeThreeStudents(this);
    }
}
