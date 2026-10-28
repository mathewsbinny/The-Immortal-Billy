package billy.model;

/**
 * A static puzzle template loaded from content.
 */
public class Puzzle {

    private final int roomId;
    private final String type;   // action | item | spell
    private final String name;
    private final String description;
    private final String solution;
    private final java.util.List<PuzzleChoice> choices = new java.util.ArrayList<>();
    private final java.util.List<PuzzleReward> rewards = new java.util.ArrayList<>();
    private final java.util.List<String> triggers = new java.util.ArrayList<>();

    public Puzzle(int roomId, String type, String name, String description, String solution) {
        this.roomId = roomId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.solution = solution;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSolution() {
        return solution;
    }

    public java.util.List<PuzzleChoice> getChoices() {
        return choices;
    }

    public java.util.List<PuzzleReward> getRewards() {
        return rewards;
    }

    public void addReward(PuzzleReward reward) {
        rewards.add(reward);
    }

    public java.util.List<String> getTriggers() {
        return triggers;
    }

    public void addTrigger(String trigger) {
        triggers.add(trigger);
    }

    public boolean hasTrigger(String phrase) {
        for (String trigger : triggers) {
            if (trigger.equalsIgnoreCase(phrase)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAction() {
        return "action".equalsIgnoreCase(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
