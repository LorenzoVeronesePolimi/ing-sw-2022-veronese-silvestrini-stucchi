package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.MessageStudentToArchipelago;

import java.util.Locale;


public class ControllerInput {
    /*
    private Board model;
    private boolean boardAdvanced;
    private int numPlayers;
    private View view;

    private ControllerState controllerState;

    public ControllerInput(Board model, View view){
        this.model = model;
        this.view = view;
        this.controllerState = new ControllerState();
    }*/
    private static final int MAX_NUM_ARCHIPELAGOS = 12;


    public ControllerInput(){}

    public boolean checkFormat(Object arg){
        if(!(arg instanceof Message)){
            return false;
        }

        Message message = (Message)arg;
        switch(message.getType()){
            case STUDENT_TO_ARCHIPELAGO:
                if(!(message instanceof MessageStudentToArchipelago)){
                    return false;
                }

                String studentColour = ((MessageStudentToArchipelago) message).getColour();
                int destArchipelagoIndex = ((MessageStudentToArchipelago) message).getDestArchipelagoIndex();
                return (this.checkStudentColours(studentColour) &&
                        this.checkDestArchipelagoIndex(destArchipelagoIndex));
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