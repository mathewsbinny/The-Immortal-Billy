package Controller;

import GameExceptions.GameException;
import Model.CharacterDB;
import Model.ItemRoomDB;
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

    private ArrayList<Item> itemList;
    private int roomIsVisited;
    private RoomDB rdb;


    public Room(){
        rdb = new RoomDB();
        exitList = new ArrayList<Exit>();
        itemList = new ArrayList<>();
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

    public int validateDirection(String command) throws GameException {
        int newRoomID = 1;
        boolean found = false;

        for (int i = 0; !found && i < exitList.size(); i++) {
            if (exitList.get(i).getDirection().charAt(0) == command.charAt(0)) {
                newRoomID = exitList.get(i).getExitDestinationID();
                found = true;

            }
        }
        if (found) {
            throw new GameException("Invalid Direction Entered");
        }

        return newRoomID;
    }

    public String display(){
    return buildDescription() + displayExits();

    }

    public String buildDescription(){
        return roomDescription;
    }


    public Room retrieveByID(int roomID) throws GameException {
        rdb = new RoomDB();
       return rdb.getRoomByRoomID(roomID);


    }

    public String displayExits(){
        String exitOptions = "You can go ";
        for(Exit e : exitList){
            exitOptions += e.getDirection() + ", ";
        }

        return exitOptions;
    }



    @Override
    public String toString() {
        return "Room" +
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
