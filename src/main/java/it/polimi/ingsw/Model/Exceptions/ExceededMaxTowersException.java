package it.polimi.ingsw.Model.Exceptions;

public class ExceededMaxTowersException extends Exception{
     public ExceededMaxTowersException(){
         System.out.println("[Exception]: Number of towers exceeded");
         printStackTrace();
     }
}
