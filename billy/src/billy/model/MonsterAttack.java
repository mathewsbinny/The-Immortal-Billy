package billy.model;

/**
 * A monster attack template loaded from content.
 */
public class MonsterAttack {

    private final int id;
    private final String name;
    private final int monsterId;      // owning monster (0 = generic attack)
    private final double addHp;       // self-heal amount
    private final double addAp;       // damage amount
    private final double probability; // chance of being chosen (0..1)
    private final String description;

    public MonsterAttack(int id, String name, int monsterId, double addHp,
                         double addAp, double probability, String description) {
        this.id = id;
        this.name = name;
        this.monsterId = monsterId;
        this.addHp = addHp;
        this.addAp = addAp;
        this.probability = probability;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public double getAddHp() {
        return addHp;
    }

    public double getAddAp() {
        return addAp;
    }

    public double getProbability() {
        return probability;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
