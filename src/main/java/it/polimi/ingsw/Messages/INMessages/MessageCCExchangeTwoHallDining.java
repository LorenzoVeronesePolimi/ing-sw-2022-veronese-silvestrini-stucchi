package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXCHANGE_TWO_HALL_DINING;

/**
 * input message that asks to manage the use of the ExchangeTwoHallDining character card
 */
public class MessageCCExchangeTwoHallDining extends Message{
    private final String colourHall1;
    private final String colourHall2;
    private final String colourDiningRoom1;
    private final String colourDiningRoom2;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param colourDiningRoom1 colour of first student to pick from the dining room
     * @param colourDiningRoom2 colour of second student to pick from the dining room
     * @param colourHall1 colour of first student to pick from the hall
     * @param colourHall2 colour of second student to pick from the hall
     */
    public MessageCCExchangeTwoHallDining(String nicknamePlayer, String colourHall1, String colourHall2,
                                          String colourDiningRoom1, String colourDiningRoom2){
        super(CC_EXCHANGE_TWO_HALL_DINING, nicknamePlayer);
        this.colourHall1 = colourHall1;
        this.colourHall2 = colourHall2;
        this.colourDiningRoom1 = colourDiningRoom1;
        this.colourDiningRoom2 = colourDiningRoom2;
    }

    /**
     * getter of the colours of the students that are placed in the hall
     * @return list of the colours of the students that are placed in the hall
     */
    public List<String> getColoursHall(){
        List<String> colours = new ArrayList<>();

        colours.add(colourHall1);
        colours.add(colourHall2);

        return colours;
    }

    /**
     * getter of the colours of the students that are placed in the dining room
     * @return list of the colours of the students that are placed in the dining room
     */
    public List<String> getColoursDiningRoom(){
        List<String> colours = new ArrayList<>();

        colours.add(colourDiningRoom1);
        colours.add(colourDiningRoom2);

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
                controller.checkMultipleStudentColour(this.getColoursHall()) &&
                controller.checkMultipleStudentColour(this.getColoursDiningRoom()));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExchangeTwoHallDining(this);
    }


}
