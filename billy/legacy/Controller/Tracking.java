package Controller;

public class Tracking {
    private int trackCharacterID;
    private int trackCharacterLevel;
    private int trackRoomsVisited;
    private double trackPlayTime;
    private int trackMonstersKilled;
    private int trackCharacterDeaths;

    public int getTrackCharacterID() {
        return trackCharacterID;
    }

    public void setTrackCharacterID(int trackCharacterID) {
        this.trackCharacterID = trackCharacterID;
    }

    public int getTrackCharacterLevel() {
        return trackCharacterLevel;
    }

    public void setTrackCharacterLevel(int trackCharacterLevel) {
        this.trackCharacterLevel = trackCharacterLevel;
    }

    public int getTrackRoomsVisited() {
        return trackRoomsVisited;
    }

    public void setTrackRoomsVisited(int trackRoomsVisited) {
        this.trackRoomsVisited = trackRoomsVisited;
    }

    public double getTrackPlayTime() {
        return trackPlayTime;
    }

    public void setTrackPlayTime(double trackPlayTime) {
        this.trackPlayTime = trackPlayTime;
    }

    public int getTrackMonstersKilled() {
        return trackMonstersKilled;
    }

    public void setTrackMonstersKilled(int trackMonstersKilled) {
        this.trackMonstersKilled = trackMonstersKilled;
    }

    public int getTrackCharacterDeaths() {
        return trackCharacterDeaths;
    }

    public void setTrackCharacterDeaths(int trackCharacterDeaths) {
        this.trackCharacterDeaths = trackCharacterDeaths;
    }

    @Override
    public String toString() {
        return "Tracking{" +
                "trackCharacterID=" + trackCharacterID +
                ", trackCharacterLevel=" + trackCharacterLevel +
                ", trackRoomsVisited=" + trackRoomsVisited +
                ", trackPlayTime=" + trackPlayTime +
                ", trackMonstersKilled=" + trackMonstersKilled +
                ", trackCharacterDeaths=" + trackCharacterDeaths +
                '}';
    }
}
