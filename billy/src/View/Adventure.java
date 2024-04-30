package View;

import Controller.Account;
import Controller.GameController;
import Controller.Room;
import GameExceptions.GameException;
import Model.RoomDB;
import Model.SQLiteDB;

import java.sql.SQLException;
import java.util.Scanner;

public class Adventure {

    Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws GameException {
        GameController gc = new GameController();
        Adventure adventure = new Adventure();
        adventure.playGame(gc);
        RoomDB rdb = new RoomDB();

        System.out.println(rdb.getRoomByRoomID(1));
    }
    public void playGame(GameController gc) throws GameException {
        gc.startDB();
        boolean stopped = true;
        while(stopped){

                System.out.println("What would you like to do");
                System.out.println(gc.executeCommand(sc.nextLine()));
        }
    }
}
