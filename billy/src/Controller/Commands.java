package Controller;

import GameExceptions.GameException;

import java.util.Arrays;

public class Commands {

    private Character character = new Character();
    private Room room = new Room();

    protected static final java.util.List<java.lang.Character> VALID_DIRECTIONS = Arrays.asList('F','U','B','N','S','E','W');



    public Commands(){

    }

    private int validateCommand(String cmdLine) throws GameException {
        int commandPrompt;

        if(VALID_DIRECTIONS.contains(cmdLine.toUpperCase().charAt(0))){
            commandPrompt = 1;
        }
  //      else if(ITEM_COMMANDS.contains(cmdLine.charAt(0))){
      //      commandPrompt = 2;
     //   }
     //   else if(cmdLine.equalsIgnoreCase("l")){
      //      commandPrompt = 3;
     //   }
    //    else if(cmdLine.equalsIgnoreCase("b")){
      //      commandPrompt = 4;
     //   }
     //   else if(cmdLine.equalsIgnoreCase("x")){
     //       commandPrompt = EXIT_COMMAND;
    //    }
        else{
            commandPrompt = 0;
        }
        return commandPrompt;
    }

    protected String executeCommand(String cmd) throws GameException {
        String returnedCmd = "";
        room = new Room();
        character = new Character();
        switch(validateCommand(cmd)){
            case 0 -> throw new GameException("Invalid Input");
            case 1 -> returnedCmd = move(cmd);
           // case 2 -> returnedCmd = itemCommand(cmd);
           // case 3 -> returnedCmd = room.display();
           // case 4 -> returnedCmd = player.printInventory();
           // case 5 -> returnedCmd = "Exit";   //Revisit this case

        }

        return returnedCmd;
    }


    public String move(String cmd) throws GameException {
        String displayedRoom;
       character.setCurrentRoom(room.retrieveByID(room.validateDirection(cmd)));
       character.updateCharacterCurrentRoom();


       return character.getCurrentRoom().display();

    }
}