package billy.model;

/**
 * A directed connection between two rooms.
 */
public class Exit {

    private final String direction;
    private final int destinationRoomId;

    public Exit(String direction, int destinationRoomId) {
        this.direction = direction;
        this.destinationRoomId = destinationRoomId;
    }

    public String getDirection() {
        return direction;
    }

    public int getDestinationRoomId() {
        return destinationRoomId;
    }

    @Override
    public String toString() {
        return direction + " -> room " + destinationRoomId;
    }
}
