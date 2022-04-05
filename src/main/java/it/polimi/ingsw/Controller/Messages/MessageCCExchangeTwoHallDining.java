package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXCHANGE_TWO_HALL_DINING;

public class MessageCCExchangeTwoHallDining extends MessageCC{
    private final String nicknamePlayer;
    private final String colourHall1;
    private final String colourHall2;
    private final String colourDiningRoom1;
    private final String colourDiningRoom2;


    public MessageCCExchangeTwoHallDining(int indexCard, String nicknamePlayer, String colourHall1, String colourHall2,
                                          String colourDiningRoom1, String colourDiningRoom2){
        super(CC_EXCHANGE_TWO_HALL_DINING, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourHall1 = colourHall1;
        this.colourHall2 = colourHall2;
        this.colourDiningRoom1 = colourDiningRoom1;
        this.colourDiningRoom2 = colourDiningRoom2;
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

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExchangeTwoHallDining(this);
    }
}
