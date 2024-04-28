package Controller;

public class Exit {
    private int exitID;
    private int exitRoomID;
    private String direction;
    private int exitDestinationID;

    public int getExitID() {
        return exitID;
    }

    public void setExitID(int exitID) {
        this.exitID = exitID;
    }

    public int getExitRoomID() {
        return exitRoomID;
    }

    public void setExitRoomID(int exitRoomID) {
        this.exitRoomID = exitRoomID;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getExitDestinationID() {
        return exitDestinationID;
    }

    public void setExitDestinationID(int exitDestinationID) {
        this.exitDestinationID = exitDestinationID;
    }

    @Override
    public String toString() {
        return "Exit{" +
                "exitRoomID=" + exitRoomID +
                ", direction='" + direction + '\'' +
                ", exitDestinationID=" + exitDestinationID +
                '}';
    }
}
