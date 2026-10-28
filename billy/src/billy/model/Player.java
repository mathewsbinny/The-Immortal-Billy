package billy.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mutable per-save player state. Persisted by {@code billy.data.SaveStore}.
 */
public class Player {

    public static final String KEY_EARTH = "Earth Key";
    public static final String KEY_WATER = "Water Key";
    public static final String KEY_AIR = "Air Key";
    public static final String KEY_FIRE = "Fire Key";

    /** An item carried by the player, optionally equipped. */
    public static class ItemInstance {
        public int itemId;
        public String name;
        public String type;
        public boolean equipped;

        public ItemInstance() {
        }

        public ItemInstance(int itemId, String name, String type, boolean equipped) {
            this.itemId = itemId;
            this.name = name;
            this.type = type;
            this.equipped = equipped;
        }
    }

    private int accountId;
    private int saveSlot = 1;
    private String name = "Billy";
    private int currentRoomId = 1;
    private double hp = 100;
    private double maxHp = 100;
    private double ap = 25;
    private double dp = 10;
    private int mp = 10;
    private int maxMp = 10;
    private int xp = 0;
    private int level = 1;
    private int deaths = 0;
    private int monstersKilled = 0;
    private int roomsVisited = 0;
    private double playTime = 0;
    private final List<ItemInstance> inventory = new ArrayList<>();
    private final Map<String, String> flags = new LinkedHashMap<>();

    // ---- account / identity ----
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getSaveSlot() {
        return saveSlot;
    }

    public void setSaveSlot(int saveSlot) {
        this.saveSlot = saveSlot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ---- location ----
    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    // ---- stats ----
    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    }

    public double getAp() {
        return ap;
    }

    public void setAp(double ap) {
        this.ap = ap;
    }

    public double getDp() {
        return dp;
    }

    public void setDp(double dp) {
        this.dp = dp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public boolean hasMp(int cost) {
        return mp >= cost;
    }

    public void spendMp(int cost) {
        mp = Math.max(0, mp - cost);
    }

    public void restoreMpToFull() {
        mp = maxMp;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getMonstersKilled() {
        return monstersKilled;
    }

    public void setMonstersKilled(int monstersKilled) {
        this.monstersKilled = monstersKilled;
    }

    public int getRoomsVisited() {
        return roomsVisited;
    }

    public void setRoomsVisited(int roomsVisited) {
        this.roomsVisited = roomsVisited;
    }

    public double getPlayTime() {
        return playTime;
    }

    public void setPlayTime(double playTime) {
        this.playTime = playTime;
    }

    // ---- inventory ----
    public List<ItemInstance> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        addItem(item, false);
    }

    public void addItem(Item item, boolean equipped) {
        inventory.add(new ItemInstance(item.getId(), item.getName(), item.getType(), equipped));
    }

    public boolean hasItem(String itemName) {
        for (ItemInstance i : inventory) {
            if (i.name.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(int itemId) {
        inventory.removeIf(i -> i.itemId == itemId);
    }

    public int countKeys() {
        int count = 0;
        for (ItemInstance i : inventory) {
            if ("Key".equalsIgnoreCase(i.type)) {
                count++;
            }
        }
        return count;
    }

    // ---- progress flags ----
    public Map<String, String> getFlags() {
        return flags;
    }

    public void setFlag(String key, String value) {
        flags.put(key, value);
    }

    public String getFlag(String key) {
        return flags.get(key);
    }

    public boolean flagTrue(String key) {
        return "true".equalsIgnoreCase(flags.get(key));
    }

    public boolean isMonsterDefeated(int monsterId) {
        return flagTrue("monster_" + monsterId + "_defeated");
    }

    public void markMonsterDefeated(int monsterId) {
        setFlag("monster_" + monsterId + "_defeated", "true");
    }

    public boolean knowsAttack(int attackId) {
        return flagTrue("atk_" + attackId);
    }

    public void learnAttack(int attackId) {
        setFlag("atk_" + attackId, "true");
    }

    public boolean isPuzzleSolved(int roomId) {
        return flagTrue("puzzle_" + roomId + "_solved");
    }

    public void markPuzzleSolved(int roomId) {
        setFlag("puzzle_" + roomId + "_solved", "true");
    }

    public enum StatChoice { MAX_HEALTH, ATTACK, DEFENSE, MAGIC }

    /** Adds experience points. Leveling is manual; see {@link #levelUp(StatChoice)}. */
    public void addXp(int amount) {
        xp += amount;
    }

    public int xpToNextLevel() {
        return level * 100;
    }

    public boolean canLevelUp() {
        return xp >= xpToNextLevel();
    }

    /** Consumes the XP for one level and applies the chosen stat increase. */
    public void levelUp(StatChoice choice) {
        if (!canLevelUp()) {
            return;
        }
        xp -= xpToNextLevel();
        level++;
        switch (choice) {
            case MAX_HEALTH:
                maxHp += 25;
                hp = Math.min(maxHp, hp + 25);
                break;
            case ATTACK:
                ap += 5;
                break;
            case DEFENSE:
                dp += 5;
                break;
            case MAGIC:
                maxMp += 5;
                mp = maxMp;
                break;
        }
    }
}
