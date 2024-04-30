package Controller;

import GameExceptions.GameException;
import Model.CharacterDB;
import Model.ExitDB;
import Model.RoomDB;

import java.util.ArrayList;

public class Room {

    private int roomID;
    private String roomRegion;
    private String roomRandomization;
    private String roomType;
    private String roomName;
    private String roomDescription;
    private ArrayList<Exit> exitList;
    private int roomIsVisited;
    private RoomDB rdb;


    public Room(){
        rdb = new RoomDB();
        exitList = new ArrayList<Exit>();
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomRegion() {
        return roomRegion;
    }

    public void setRoomRegion(String roomRegion) {
        this.roomRegion = roomRegion;
    }

    public String getRoomRandomization() {
        return roomRandomization;
    }

    public void setRoomRandomization(String roomRandomization) {
        this.roomRandomization = roomRandomization;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public ArrayList<Exit> getExitList() {
        return exitList;
    }

    public void setExitList(ArrayList<Exit> exitList) {
        this.exitList = exitList;
    }

    public int getRoomIsVisited() {
        return roomIsVisited;
    }

    public void setRoomIsVisited(int roomIsVisited) {
        this.roomIsVisited = roomIsVisited;
    }

    public Room getRoom(int roomID) throws GameException {
        RoomDB roomDB = new RoomDB();
        return roomDB.getRoomByRoomID(roomID);
    }

    public String validateDirection(String command) throws GameException {
        command = command.toUpperCase();
        if (command.equals("F")) {
            command = "FORWARD";
        } else if (command.equals("B")) {
            command = "BACK";
        }
        else if(command.equals("N")){
            command = "NORTH";
        }
        else if(command.equals("S")){
            command = "SOUTH";
        }
        else if(command.equals("E")){
            command = "EAST";
        }
        else if(command.equals("W")){
            command = "WEST";
        }
        else if(command.equals("U")){
            command = "UP";
        }
        Character character = new Character();
        Character characterData = character.getCharacter1();
        ExitDB exitDB = new ExitDB();
        exitList = exitDB.getExits(characterData.getCharacterCurrentRoomID());
        int i = 0;
        for (Exit direction : exitList) {
            if (direction.getDirection().equals(command)) {
                System.out.println("into update currentRoomID");
                characterData.setCharacterCurrentRoomID(direction.getExitDestinationID());
                CharacterDB characterDB = new CharacterDB();
                characterDB.updateCharacterCurrentRoom(characterData);
                System.out.println(" Moved after roomID is " + direction.getExitDestinationID());
            }
        }
        return "Invalid Direction Entered";
    }

        public String display(){
    return toString();

    }

    public void updateRoom(){
        rdb = new RoomDB();

    }

    public Room retrieveByID(int roomID) throws GameException {
        rdb = new RoomDB();
       return rdb.getRoomByRoomID(roomID);


    }



    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", roomRegion='" + roomRegion + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomDesc='" + roomDescription + '\'' +
                ", exitList=" + exitList +
                ", roomIsVisited=" + roomIsVisited +
                ", rdb=" + rdb +
                '}';
    }
}
