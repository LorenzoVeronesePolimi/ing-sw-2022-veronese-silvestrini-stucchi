package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXCHANGE_THREE_STUDENTS;

/**
 * input message that asks to manage the use of the ExchangeThreeStudents character card
 */
public class MessageCCExchangeThreeStudents extends Message{
    private final String colourCard1;
    private final String colourCard2;
    private final String colourCard3;
    private final String colourHall1;
    private final String colourHall2;
    private final String colourHall3;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param colourCard1 colour of first student to pick from the card
     * @param colourCard2 colour of second student to pick from the card
     * @param colourCard3 colour of third student to pick from the card
     * @param colourHall1 colour of first student to pick from the hall
     * @param colourHall2 colour of second student to pick from the hall
     * @param colourHall3 colour of third student to pick from the hall
     */
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

    /**
     * getter of the colours of the students that are placed on the card
     * @return list of the colours of the students that are placed on the card
     */
    public List<String> getColoursCard(){
        List<String> colours = new ArrayList<>();

        colours.add(colourCard1);
        colours.add(colourCard2);
        colours.add(colourCard3);

        return colours;
    }

    /**
     * getter of the colours of the students that are placed in the hall
     * @return list of the colours of the students that are placed in the hall
     */
    public List<String> getColoursHall(){
        List<String> colours = new ArrayList<>();

        colours.add(colourHall1);
        colours.add(colourHall2);
        colours.add(colourHall3);

        return colours;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkMultipleStudentColour(this.getColoursCard()) &&
                controller.checkMultipleStudentColour(this.getColoursHall()));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExchangeThreeStudents(this);
    }
}
