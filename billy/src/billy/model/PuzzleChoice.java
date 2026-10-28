package billy.model;

/**
 * A possible player response to a puzzle and the text shown for it.
 */
public class PuzzleChoice {

    private final int roomId;
    private final String choice;   // e.g. "1", "ROYGBIV", "Gust"
    private final String content;  // narrative result text

    public PuzzleChoice(int roomId, String choice, String content) {
        this.roomId = roomId;
        this.choice = choice;
        this.content = content;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getChoice() {
        return choice;
    }

    public String getContent() {
        return content;
    }
}
