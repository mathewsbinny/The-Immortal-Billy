package View;

import Controller.GameController;
import GameExceptions.GameException;

public class Adventure {
    public static void main(String[] args) throws GameException {
        boolean valid = true;
        GameController gc = new GameController();
         try {
         gc.startDB();
         } catch (GameException ge) {
         valid = false;
         System.out.println(ge.getMessage());
         }
        gc.startDB();


        if (valid) {

        }
    }
}
