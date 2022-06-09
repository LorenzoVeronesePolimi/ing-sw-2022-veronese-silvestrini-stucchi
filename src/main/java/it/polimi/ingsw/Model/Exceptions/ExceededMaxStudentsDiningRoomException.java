package it.polimi.ingsw.Model.Exceptions;

public class ExceededMaxStudentsDiningRoomException extends Exception{
    public ExceededMaxStudentsDiningRoomException() {
        System.out.println("[Exception]: Number of student in dining room exceeded");
        printStackTrace();
    }
}
