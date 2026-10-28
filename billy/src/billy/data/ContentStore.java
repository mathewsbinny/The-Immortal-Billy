package billy.data;

import billy.GameException;
import billy.model.Attack;
import billy.model.Element;
import billy.model.HelpEntry;
import billy.model.Item;
import billy.model.Monster;
import billy.model.MonsterAttack;
import billy.model.Puzzle;
import billy.model.PuzzleChoice;
import billy.model.PuzzleReward;
import billy.model.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads static game content (rooms, monsters, items, attacks, puzzles, help)
 * from the bundled SQL seed files into in-memory model objects.
 */
public class ContentStore {

    private final Map<Integer, Room> rooms = new LinkedHashMap<>();
    private final Map<Integer, Monster> monsters = new LinkedHashMap<>();
    private final Map<Integer, Item> items = new LinkedHashMap<>();
    private final List<Attack> attacks = new ArrayList<>();
    private final Map<Integer, Puzzle> puzzles = new LinkedHashMap<>();
    private final List<HelpEntry> help = new ArrayList<>();

    private ContentStore() {
    }

    /** Loads content, resolving the resources directory relative to the working dir. */
    public static ContentStore load() throws GameException {
        String dir = "resources/content";
        if (!new File(dir).isDirectory() && new File("billy/resources/content").isDirectory()) {
            dir = "billy/resources/content";
        }
        return load(dir);
    }

    public static ContentStore load(String resourceDir) throws GameException {
        ContentStore store = new ContentStore();
        try (Connection conn = Database.connect(":memory:")) {
            store.seed(conn, resourceDir, "Room.txt");
            store.seed(conn, resourceDir, "Exit.txt");
            store.seed(conn, resourceDir, "Item.txt");
            store.seed(conn, resourceDir, "Monster.txt");
            store.seed(conn, resourceDir, "MonsterAttack.txt");
            store.seed(conn, resourceDir, "CharacterAttack.txt");
            store.seed(conn, resourceDir, "Puzzle.txt");
            store.seed(conn, resourceDir, "PuzzleChoice.txt");
            store.seed(conn, resourceDir, "PuzzleCollider.txt");
            store.seed(conn, resourceDir, "PuzzleReward.txt");
            store.seed(conn, resourceDir, "HelpMenu.txt");
            store.hydrate(conn);
        } catch (SQLException e) {
            throw new GameException("Failed to load game content: " + e.getMessage(), e);
        }
        return store;
    }

    private void seed(Connection conn, String resourceDir, String fileName) throws GameException, SQLException {
        File file = new File(resourceDir, fileName);
        if (!file.exists()) {
            throw new GameException("Missing content file: " + file.getPath());
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
             Statement statement = conn.createStatement()) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                statement.executeUpdate(line);
            }
        } catch (IOException e) {
            throw new GameException("Could not read content file " + file.getPath(), e);
        }
    }

    private void hydrate(Connection conn) throws SQLException {
        hydrateRooms(conn);
        hydrateItems(conn);
        hydrateMonsters(conn);
        hydrateAttacks(conn);
        hydratePuzzles(conn);
        hydrateHelp(conn);
    }

    private void hydrateRooms(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Room")) {
            while (rs.next()) {
                Room room = new Room(rs.getInt("roomID"), rs.getString("roomRegion"), rs.getString("roomType"),
                        rs.getString("roomName"), rs.getString("roomDescription"));
                rooms.put(room.getId(), room);
            }
        }
    }

    private void hydrateItems(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Item")) {
            while (rs.next()) {
                Item item = new Item(rs.getInt("itemID"), rs.getInt("itemRoomID"), rs.getString("itemName"),
                        rs.getString("itemType"), rs.getDouble("itemAddHP"), rs.getDouble("itemAddMaxHP"),
                        rs.getInt("itemAddMP"), rs.getDouble("itemAddDP"), rs.getDouble("itemAddAP"),
                        rs.getInt("itemIsConsumable") != 0);
                items.put(item.getId(), item);
            }
        }
    }

    private void hydrateMonsters(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Monster")) {
            while (rs.next()) {
                Monster monster = new Monster(rs.getInt("monsterID"), rs.getInt("monsterRoomID"),
                        rs.getString("monsterName"), rs.getString("monsterCategory"),
                        rs.getDouble("monsterMaxHP"), rs.getDouble("monsterDP"), rs.getDouble("monsterAP"),
                        rs.getInt("monsterBossPhase"),
                        monsterElementFor(rs.getInt("monsterRoomID"), rs.getString("monsterName")));
                monsters.put(monster.getId(), monster);
            }
        }
    }

    private void hydrateAttacks(Connection conn) throws SQLException {
        List<MonsterAttack> globalAttacks = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM AttackMonster")) {
            while (rs.next()) {
                MonsterAttack attack = new MonsterAttack(rs.getInt("attackID"), rs.getString("attackName"),
                        rs.getInt("attackMonsterID"), rs.getDouble("attackAddHP"), rs.getDouble("attackAddAP"),
                        rs.getDouble("attackProbability"), rs.getString("attackDescription"));
                Monster owner = monsters.get(attack.getMonsterId());
                if (owner != null) {
                    owner.addAttack(attack);
                } else {
                    globalAttacks.add(attack);
                }
            }
        }
        // The generic "Attack" (monsterID NULL) is a basic move every monster can use.
        for (Monster monster : monsters.values()) {
            for (MonsterAttack attack : globalAttacks) {
                monster.addAttack(attack);
            }
        }
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM AttackCharacter")) {
            while (rs.next()) {
                Attack attack = new Attack(rs.getInt("attackID"), rs.getString("attackName"),
                        rs.getString("attackUIType"), rs.getInt("attackMonsterID"), rs.getInt("attackMPLevel"),
                        rs.getDouble("attackAddHP"), rs.getDouble("attackAddAP"), rs.getString("attackDescription"),
                        attackElementFor(rs.getInt("attackMonsterID")));
                attacks.add(attack);
            }
        }
    }

    /** Derives a monster's element from its room region, with throne-boss overrides. */
    private Element monsterElementFor(int roomId, String name) {
        Room room = rooms.get(roomId);
        String region = room == null ? "" : room.getRegion();
        switch (region.toLowerCase()) {
            case "earth": return Element.EARTH;
            case "water": return Element.WATER;
            case "air": return Element.AIR;
            case "fire": return Element.FIRE;
            default: break;
        }
        // Throne-room bosses carry their element in their name; the Demon King is neutral.
        if (name.contains("Earth")) {
            return Element.EARTH;
        }
        if (name.contains("Water")) {
            return Element.WATER;
        }
        if (name.contains("Air")) {
            return Element.AIR;
        }
        if (name.contains("Fire")) {
            return Element.FIRE;
        }
        return Element.PHYSICAL;
    }

    /** A player attack inherits the element of the monster that teaches it. */
    private Element attackElementFor(int monsterId) {
        Monster monster = monsters.get(monsterId);
        return monster == null ? Element.PHYSICAL : monster.getElement();
    }

    private void hydratePuzzles(Connection conn) throws SQLException {
        Map<Integer, List<PuzzleChoice>> choiceMap = new LinkedHashMap<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM PuzzleChoice")) {
            while (rs.next()) {
                int roomId = rs.getInt("puzzleRoomID");
                PuzzleChoice choice = new PuzzleChoice(roomId, rs.getString("playerChoice"), rs.getString("choiceContent"));
                choiceMap.computeIfAbsent(roomId, k -> new ArrayList<>()).add(choice);
            }
        }
        Map<Integer, List<PuzzleReward>> rewardMap = new LinkedHashMap<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM PuzzleReward")) {
            while (rs.next()) {
                int roomId = rs.getInt("puzzleRoomID");
                PuzzleReward reward = new PuzzleReward(roomId, rs.getString("target"), rs.getDouble("HP"),
                        rs.getDouble("AP"), rs.getDouble("maxHP"), rs.getString("item"));
                rewardMap.computeIfAbsent(roomId, k -> new ArrayList<>()).add(reward);
            }
        }
        Map<Integer, List<String>> triggerMap = new LinkedHashMap<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM PuzzleCollider")) {
            while (rs.next()) {
                int roomId = rs.getInt("puzzleRoomID");
                triggerMap.computeIfAbsent(roomId, k -> new ArrayList<>()).add(rs.getString("usedName"));
            }
        }
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Puzzle")) {
            while (rs.next()) {
                int roomId = rs.getInt("puzzleRoomID");
                Puzzle puzzle = new Puzzle(roomId, rs.getString("puzzleType"), rs.getString("puzzleName"),
                        rs.getString("puzzleDescription"), rs.getString("puzzleSolution"));
                puzzle.getChoices().addAll(choiceMap.getOrDefault(roomId, new ArrayList<>()));
                for (PuzzleReward reward : rewardMap.getOrDefault(roomId, new ArrayList<>())) {
                    puzzle.addReward(reward);
                }
                for (String trigger : triggerMap.getOrDefault(roomId, new ArrayList<>())) {
                    puzzle.addTrigger(trigger);
                }
                puzzles.put(puzzle.getRoomId(), puzzle);
            }
        }
    }

    private void hydrateHelp(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM HelpMenu")) {
            while (rs.next()) {
                help.add(new HelpEntry(rs.getInt("hintID"), rs.getString("hintName"), rs.getString("hintDescription")));
            }
        }
    }

    public Room getRoom(int roomId) {
        return rooms.get(roomId);
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public Item getItem(int itemId) {
        return items.get(itemId);
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public Item findItemByName(String name) {
        for (Item item : items.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public Monster getMonster(int monsterId) {
        return monsters.get(monsterId);
    }

    public Map<Integer, Monster> getMonsters() {
        return monsters;
    }

    public Monster findMonsterByRoom(int roomId) {
        for (Monster m : monsters.values()) {
            if (m.getRoomId() == roomId) {
                return m;
            }
        }
        return null;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public Puzzle getPuzzle(int roomId) {
        return puzzles.get(roomId);
    }

    public Map<Integer, Puzzle> getPuzzles() {
        return puzzles;
    }

    /** Returns true if the given item name is granted as a puzzle reward (so it isn't free loot). */
    public boolean isPuzzleRewardItem(String itemName) {
        for (Puzzle puzzle : puzzles.values()) {
            for (PuzzleReward reward : puzzle.getRewards()) {
                if (itemName.equalsIgnoreCase(reward.getItemName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<HelpEntry> getHelp() {
        return help;
    }
}
