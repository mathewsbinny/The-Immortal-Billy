package Controller;

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
