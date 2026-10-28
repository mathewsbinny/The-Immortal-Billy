package billy.model;

/**
 * A player attack / spell template loaded from content. Whether the player
 * has actually learned an attack is tracked on the {@link Player}, not here.
 */
public class Attack {

    private final int id;
    private final String name;
    private final String uiType;  // Battle | Nonbattle
    private final int monsterId;  // monster that teaches this attack (0 = always known)
    private final int mpLevel;    // required MP level
    private final double addHp;   // healing component
    private final double addAp;   // damage component
    private final String description;

    public Attack(int id, String name, String uiType, int monsterId, int mpLevel,
                  double addHp, double addAp, String description) {
        this.id = id;
        this.name = name;
        this.uiType = uiType;
        this.monsterId = monsterId;
        this.mpLevel = mpLevel;
        this.addHp = addHp;
        this.addAp = addAp;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUiType() {
        return uiType;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public int getMpLevel() {
        return mpLevel;
    }

    public double getAddHp() {
        return addHp;
    }

    public double getAddAp() {
        return addAp;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
