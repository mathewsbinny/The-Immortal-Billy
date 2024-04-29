package View;

import Controller.Account;
import Controller.GameController;
import GameExceptions.GameException;
import Model.SQLiteDB;

import java.sql.SQLException;
import java.util.Scanner;

public class Adventure {

    Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws GameException {
        GameController gc = new GameController();
        Adventure adventure = new Adventure();
        adventure.playGame(gc);
    }
    public void playGame(GameController gc) throws GameException {
        gc.startDB();
        boolean stopped = true;
        for(int i = 0; i < 4; i++){

                System.out.println("What would you like to do");
                System.out.println(gc.executeCommand(sc.nextLine()));
        }
    }
}
