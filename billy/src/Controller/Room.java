package Controller;

import Model.RoomDB;

import java.util.ArrayList;

public class Room {

    private int roomID;

    private String roomRegion;

    private String roomType;

    private String roomName;

    private String roomDesc;

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

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
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
}
