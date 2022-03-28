package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.MessageAddPlayer;
import it.polimi.ingsw.Controller.Messages.MessageCreateMatch;
import it.polimi.ingsw.Controller.Messages.MessageStudentToArchipelago;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

import java.util.Locale;


public class ControllerInput {
    private static final int MAX_NUM_ARCHIPELAGOS = 12;


    public ControllerInput(){}

    public boolean checkFormat(Object arg){
        if(!(arg instanceof Message)){
            return false;
        }

        Message message = (Message)arg;
        switch(message.getType()){
            case CREATE_MATCH:
                String nicknameFirstPlayer = ((MessageCreateMatch)message).getNicknameFirstPlayer();
                String colourFirstPlayer = ((MessageCreateMatch)message).getColourFirstPlayer();
                return (this.checkNickname(nicknameFirstPlayer) &&
                        this.checkPlayerColour(colourFirstPlayer));
            case ADD_PLAYER:
                String nickname = ((MessageAddPlayer)message).getNickname();
                String colour = ((MessageAddPlayer)message).getColour();
                return (this.checkNickname(nickname) &&
                        this.checkPlayerColour(colour));
            case STUDENT_TO_ARCHIPELAGO:
                String studentColour = ((MessageStudentToArchipelago)message).getColour();
                int destArchipelagoIndex = ((MessageStudentToArchipelago)message).getDestArchipelagoIndex();
                return (this.checkStudentColours(studentColour) &&
                        this.checkDestArchipelagoIndex(destArchipelagoIndex));
        }

        return false;

    }

    private boolean checkNickname(String nickname){
        return !(nickname == "");
    }

    //This checks ONLY the format of the word
    private boolean checkPlayerColour(String c){
        String[] possibleColours = {"white", "black", "gray"};
        for(String s : possibleColours){
            if(c.toLowerCase() == s){return true;}
        }
        return false;
    }

    // Check if the colour in the message (a String) is possible
    private boolean checkStudentColours(String c){
        String[] possibleColours = {"red", "pink", "blue", "yellow", "green"};
        for(String s : possibleColours){
            if(c.toLowerCase() == s){return true;}
        }
        return false;
    }

    // Check if the destination Archipelago is possible
    private boolean checkDestArchipelagoIndex(int i){
        return i <= MAX_NUM_ARCHIPELAGOS;
    }

}