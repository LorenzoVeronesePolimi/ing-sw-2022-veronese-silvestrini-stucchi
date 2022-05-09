package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXCHANGE_THREE_STUDENTS;

public class MessageCCExchangeThreeStudents extends Message{
    private final String colourCard1;
    private final String colourCard2;
    private final String colourCard3;
    private final String colourHall1;
    private final String colourHall2;
    private final String colourHall3;


    public MessageCCExchangeThreeStudents(String nicknamePlayer, String colourCard1, String colourCard2,
                                          String colourCard3, String colourHall1, String colourHall2, String colourHall3){
        super(CC_EXCHANGE_THREE_STUDENTS, nicknamePlayer);
        this.colourCard1 = colourCard1;
        this.colourCard2 = colourCard2;
        this.colourCard3 = colourCard3;
        this.colourHall1 = colourHall1;
        this.colourHall2 = colourHall2;
        this.colourHall3 = colourHall3;
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
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkMultipleStudentColour(this.getColoursCard()) &&
                controller.checkMultipleStudentColour(this.getColoursHall()));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExchangeThreeStudents(this);
    }
}
