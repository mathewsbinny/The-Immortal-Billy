package billy.model;

/**
 * The reward granted for solving a puzzle.
 */
public class PuzzleReward {

    private final int roomId;
    private final String target;   // Character | Item | Weapon | Armor
    private final double hp;
    private final double ap;
    private final double maxHp;
    private final String itemName;

    public PuzzleReward(int roomId, String target, double hp, double ap, double maxHp, String itemName) {
        this.roomId = roomId;
        this.target = target;
        this.hp = hp;
        this.ap = ap;
        this.maxHp = maxHp;
        this.itemName = itemName;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getTarget() {
        return target;
    }

    public double getHp() {
        return hp;
    }

    public double getAp() {
        return ap;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public String getItemName() {
        return itemName;
    }
}
