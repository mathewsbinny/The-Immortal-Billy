package billy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A static monster template loaded from content. Current HP is tracked per
 * encounter, so only the maximum (and base stats) live here.
 */
public class Monster {

    private final int id;
    private final int roomId;      // the room this monster inhabits
    private final String name;
    private final String category; // monster | miniboss | boss
    private final double maxHp;
    private final double dp;
    private final double ap;
    private final int bossPhase;
    private final List<MonsterAttack> attacks = new ArrayList<>();

    public Monster(int id, int roomId, String name, String category, double maxHp,
                   double dp, double ap, int bossPhase) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.category = category;
        this.maxHp = maxHp;
        this.dp = dp;
        this.ap = ap;
        this.bossPhase = bossPhase;
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getDp() {
        return dp;
    }

    public double getAp() {
        return ap;
    }

    public int getBossPhase() {
        return bossPhase;
    }

    public List<MonsterAttack> getAttacks() {
        return attacks;
    }

    public void addAttack(MonsterAttack attack) {
        attacks.add(attack);
    }

    public boolean isMiniboss() {
        return "miniboss".equalsIgnoreCase(category);
    }

    public boolean isBoss() {
        return "boss".equalsIgnoreCase(category);
    }

    @Override
    public String toString() {
        return name;
    }
}
