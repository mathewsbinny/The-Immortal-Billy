package billy.model;

/**
 * A static item template loaded from content.
 */
public class Item {

    private final int id;
    private final int roomId;    // the room this item is found in
    private final String name;
    private final String type;   // Weapon | Armor | Consumable | Key
    private final double addHp;
    private final double addMaxHp;
    private final int addMp;
    private final double addDp;
    private final double addAp;
    private final boolean consumable;

    public Item(int id, int roomId, String name, String type, double addHp, double addMaxHp,
                int addMp, double addDp, double addAp, boolean consumable) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.addHp = addHp;
        this.addMaxHp = addMaxHp;
        this.addMp = addMp;
        this.addDp = addDp;
        this.addAp = addAp;
        this.consumable = consumable;
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

    public String getType() {
        return type;
    }

    public double getAddHp() {
        return addHp;
    }

    public double getAddMaxHp() {
        return addMaxHp;
    }

    public int getAddMp() {
        return addMp;
    }

    public double getAddDp() {
        return addDp;
    }

    public double getAddAp() {
        return addAp;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public boolean isKey() {
        return "Key".equalsIgnoreCase(type);
    }

    public boolean isWeapon() {
        return "Weapon".equalsIgnoreCase(type);
    }

    public boolean isArmor() {
        return "Armor".equalsIgnoreCase(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
