package billy;

import billy.combat.CombatEngine;
import billy.data.ContentStore;
import billy.data.SaveStore;
import billy.model.Exit;
import billy.model.Item;
import billy.model.Monster;
import billy.model.Player;
import billy.model.Attack;
import billy.model.Room;
import billy.puzzle.PuzzleSystem;
import billy.ui.Console;
import billy.world.World;

import java.util.Random;

/**
 * The main game loop: renders the current room, reads commands, and routes
 * them to movement or the various sub-systems.
 */
public class Game {

    private final Console console;
    private final SaveStore saveStore;
    private final ContentStore content;
    private final World world;
    private final Player player;
    private final CombatEngine combat;
    private final PuzzleSystem puzzles;
    private final long startTimeNanos = System.nanoTime();

    public Game(Console console, SaveStore saveStore, ContentStore content, Player player) {
        this(console, saveStore, content, player, new Random());
    }

    public Game(Console console, SaveStore saveStore, ContentStore content, Player player, Random random) {
        this.console = console;
        this.saveStore = saveStore;
        this.content = content;
        this.player = player;
        this.world = new World(content, random);
        this.combat = new CombatEngine(console, content, random);
        this.puzzles = new PuzzleSystem(console, content);
    }

    public void run() throws GameException {
        console.println();
        console.println("Welcome, " + player.getName() + "! Type HELP for a list of commands.");
        boolean running = true;
        while (running) {
            Room room = world.getRoom(player.getCurrentRoomId());
            markVisited(room);
            describeRoom(room);
            resolveRoom(room);
            String input = console.prompt("> ");
            if (input == null) {
                saveGame();
                return;
            }
            running = handle(input.trim(), room);
            if (player.getHp() <= 0) {
                onDefeat();
            }
        }
    }

    private void describeRoom(Room room) {
        console.println();
        console.println("[" + room.getName() + "]");
        console.println(room.getDescription());
        console.println(world.describeExits(room.getId()));

        Monster monster = content.findMonsterByRoom(room.getId());
        if (monster != null && !player.isMonsterDefeated(monster.getId())) {
            console.println("A " + monster.getCategory() + " (" + monster.getName() + ") is here!");
        }
        for (Item item : content.getItems().values()) {
            if (item.getRoomId() == room.getId() && !item.isKey()
                    && player.getFlag("picked_" + item.getId()) == null) {
                console.println("There is an item here: " + item.getName() + " (" + item.getType() + ").");
            }
        }
        billy.model.Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle != null && !player.isPuzzleSolved(room.getId())) {
            console.println("(A puzzle awaits: type SPECIAL to see available actions.)");
        }
    }

    private boolean handle(String input, Room room) throws GameException {
        if (input.isEmpty()) {
            return true;
        }
        String lower = input.toLowerCase();
        if (lower.equals("search") || lower.equals("take")) {
            doSearch(room);
            return true;
        }
        if (lower.startsWith("use ")) {
            doUse(room, input.substring(4).trim());
            return true;
        }
        if (lower.equals("special") || lower.equals("action") || lower.equals("inspect")) {
            puzzles.listActions(player, room);
            return true;
        }
        if (lower.equals("cast") || lower.equals("spells")) {
            listSpells();
            return true;
        }
        if (lower.startsWith("cast ")) {
            doCast(room, input.substring(5).trim());
            return true;
        }
        if (lower.startsWith("equip ")) {
            doEquip(input.substring(6).trim());
            return true;
        }
        if (lower.startsWith("unequip ")) {
            doUnequip(input.substring(8).trim());
            return true;
        }
        if (lower.equals("level up")) {
            doLevelUp();
            return true;
        }
        // Route active-puzzle inputs (numbered choices / rainbow letters) before
        // single-letter command aliases, so "i" can mean "indigo" mid-puzzle.
        if (puzzles.choose(player, room, input)) {
            return true;
        }
        switch (lower) {
            case "help":
                showHelp();
                return true;
            case "stats":
            case "character":
                showStats();
                return true;
            case "inventory":
            case "inv":
            case "i":
                showInventory();
                return true;
            case "map":
                showMap();
                return true;
            case "levelup":
            case "level":
                doLevelUp();
                return true;
            case "look":
            case "l":
                return true;
            case "save":
                saveGame();
                console.println("Game saved.");
                return true;
            case "quit":
            case "exit":
            case "q":
                saveGame();
                console.println("Game saved. Goodbye!");
                return false;
            default:
                break;
        }

        String direction = normalizeDirection(lower);
        if (direction != null) {
            return move(room, direction);
        }
        if (puzzles.trySpecialAction(player, room, input)) {
            return true;
        }
        console.println("I don't understand that. Type HELP for commands.");
        return true;
    }

    private boolean move(Room room, String direction) {
        if ("UP".equalsIgnoreCase(direction) && room.getId() == world.getHub().getId()) {
            if (player.countKeys() < 4) {
                console.println("The throne room door is sealed. It requires four keys ("
                        + player.countKeys() + "/4 collected).");
                return true;
            }
        }
        Exit exit = world.findExit(room.getId(), direction);
        if (exit == null) {
            console.println("You can't go that way.");
            return true;
        }
        player.setCurrentRoomId(exit.getDestinationRoomId());
        return true;
    }

    private String normalizeDirection(String raw) {
        switch (raw) {
            case "n": case "north": return "NORTH";
            case "s": case "south": return "SOUTH";
            case "e": case "east": return "EAST";
            case "w": case "west": return "WEST";
            case "u": case "up": return "UP";
            case "f": case "forward": return "FORWARD";
            case "b": case "back": return "BACK";
            default: return null;
        }
    }

    private void markVisited(Room room) {
        player.setFlag("visited_" + room.getId(), "true");
    }

    private void showHelp() {
        console.println();
        console.println("Commands:");
        for (billy.model.HelpEntry entry : content.getHelp()) {
            console.println("  " + entry.getName() + " - " + entry.getDescription());
        }
        console.println("  NORTH/SOUTH/EAST/WEST/UP/FORWARD/BACK - move");
        console.println("  LOOK - re-describe the room");
        console.println("  SPECIAL - list special actions in the room");
        console.println("  LEVELUP - spend XP to raise a stat");
        console.println("  CAST <spell> - cast a known spell");
        console.println("  USE <item> - use a consumable or puzzle item");
        console.println("  STATS - show your character");
        console.println("  INVENTORY - show carried items");
        console.println("  MAP - show the known layout");
        console.println("  SAVE - save and continue");
        console.println("  QUIT - save and exit");
    }

    private void showStats() {
        console.println();
        console.println("Name:      " + player.getName());
        console.println("Level:     " + player.getLevel());
        console.println("HP:        " + format(player.getHp()) + " / " + format(player.getMaxHp()));
        console.println("Attack:    " + format(player.getAp()));
        console.println("Defense:   " + format(player.getDp()));
        console.println("MP level:  " + player.getMp());
        console.println("XP:        " + player.getXp() + " / " + player.xpToNextLevel() + " (next level)");
        console.println("Keys:      " + player.countKeys() + " / 4");
        console.println("Monsters:  " + player.getMonstersKilled());
        console.println("Deaths:    " + player.getDeaths());
        console.println("Rooms:     " + countVisitedRooms());
    }

    private void doLevelUp() {
        if (!player.canLevelUp()) {
            console.println("You need " + (player.xpToNextLevel() - player.getXp()) + " more XP to level up.");
            return;
        }
        console.println();
        console.println("Level up! (costs " + player.xpToNextLevel() + " XP)");
        console.println("  1. Max Health (+25)");
        console.println("  2. Attack Power (+5)");
        console.println("  3. Defense (+5)");
        console.println("  4. Magic Proficiency (+1 MP)");
        String choice = console.prompt("Choose: ");
        switch (choice == null ? "" : choice.trim()) {
            case "1":
                player.levelUp(Player.StatChoice.MAX_HEALTH);
                break;
            case "2":
                player.levelUp(Player.StatChoice.ATTACK);
                break;
            case "3":
                player.levelUp(Player.StatChoice.DEFENSE);
                break;
            case "4":
                player.levelUp(Player.StatChoice.MAGIC);
                break;
            default:
                console.println("Cancelled.");
                return;
        }
        console.println("You are now level " + player.getLevel() + "!");
    }

    private void showInventory() {
        console.println();
        if (player.getInventory().isEmpty()) {
            console.println("Your inventory is empty.");
            return;
        }
        console.println("Inventory:");
        for (Player.ItemInstance item : player.getInventory()) {
            console.println("  - " + item.name + " (" + item.type + ")" + (item.equipped ? " [equipped]" : ""));
        }
        console.println("Keys: " + player.countKeys() + " / 4");
    }

    private void showMap() {
        console.println();
        console.println("Central Hub -> " + world.getHub().getName());
        for (World.District district : world.getDistricts()) {
            StringBuilder sb = new StringBuilder();
            sb.append(district.getRegion()).append(" (").append(district.getHubDirection()).append("): ");
            for (Room room : district.getPath()) {
                sb.append(room.getName());
                if (room.getId() != district.getPath().get(district.getPath().size() - 1).getId()) {
                    sb.append(" -> ");
                }
            }
            console.println(sb.toString());
        }
        console.println("Throne Room (UP) -> " + world.getThrone().getName());
    }

    private void saveGame() throws GameException {
        double elapsed = (System.nanoTime() - startTimeNanos) / 1_000_000_000.0;
        player.setPlayTime(player.getPlayTime() + elapsed);
        player.setRoomsVisited(countVisitedRooms());
        saveStore.savePlayer(player);
    }

    private int countVisitedRooms() {
        int count = 0;
        for (String key : player.getFlags().keySet()) {
            if (key.startsWith("visited_")) {
                count++;
            }
        }
        return count;
    }

    private String format(double value) {
        return value == Math.floor(value) ? String.valueOf((long) value) : String.valueOf(value);
    }

    private void resolveRoom(Room room) {
        Monster monster = content.findMonsterByRoom(room.getId());
        if (!room.isBattle() || monster == null || player.isMonsterDefeated(monster.getId())) {
            return;
        }
        CombatEngine.Outcome outcome = combat.fight(player, monster, content.getAttacks());
        if (outcome == CombatEngine.Outcome.VICTORY) {
            onVictory(monster);
        } else if (outcome == CombatEngine.Outcome.DEFEAT) {
            onDefeat();
        }
    }

    private void onVictory(Monster monster) {
        player.markMonsterDefeated(monster.getId());
        player.setMonstersKilled(player.getMonstersKilled() + 1);
        awardXp(monster);
        learnAttacksFromMonster(monster.getId());
        if (monster.isMiniboss()) {
            String keyName = findDistrictKeyName(monster.getRoomId());
            Item key = content.findItemByName(keyName);
            if (key != null && !player.hasItem(keyName)) {
                player.addItem(key);
                console.println("The " + monster.getName() + " drops a " + keyName + "!");
            }
        }
        if (monster.isBoss()) {
            player.setFlag("game_completed", "true");
            console.println("The Demon King falls! You have completed The Immortal Billy!");
        }
    }

    private void onDefeat() {
        player.setDeaths(player.getDeaths() + 1);
        player.setHp(player.getMaxHp());
        player.setCurrentRoomId(world.getHub().getId());
        console.println("You wake in the Central Hub, keeping your belongings and progress.");
    }

    private void awardXp(Monster monster) {
        int xp = monster.isBoss() ? 500 : monster.isMiniboss() ? 200 : 50;
        player.addXp(xp);
        console.println("You gain " + xp + " XP.");
        if (player.canLevelUp()) {
            console.println("You can level up! Type LEVELUP.");
        }
    }

    private void learnAttacksFromMonster(int monsterId) {
        for (billy.model.Attack attack : content.getAttacks()) {
            if (attack.getMonsterId() == monsterId && !player.knowsAttack(attack.getId())) {
                player.learnAttack(attack.getId());
                console.println("You learned the spell: " + attack.getName() + "!");
            }
        }
    }

    private String findDistrictKeyName(int minibossRoomId) {
        for (World.District district : world.getDistricts()) {
            if (district.getMinibossRoom() != null && district.getMinibossRoom().getId() == minibossRoomId) {
                return district.getKeyItemName();
            }
        }
        return null;
    }

    private void doSearch(Room room) {
        boolean found = false;
        for (Item item : content.getItems().values()) {
            if (item.getRoomId() != room.getId() || item.isKey() || player.hasItem(item.getName())
                    || player.getFlag("picked_" + item.getId()) != null
                    || content.isPuzzleRewardItem(item.getName())) {
                continue;
            }
            player.addItem(item);
            player.setFlag("picked_" + item.getId(), "true");
            console.println("You found: " + item.getName() + " (" + item.getType() + ").");
            found = true;
        }
        if (!found) {
            console.println("You search but find nothing of use.");
        }
    }

    private void doUse(Room room, String name) {
        if (name.isEmpty()) {
            console.println("Use what?");
            return;
        }
        Player.ItemInstance instance = findInInventory(name);
        if (instance == null) {
            console.println("You don't have that.");
            return;
        }
        Item item = content.getItem(instance.itemId);
        if (item == null) {
            console.println("Unknown item.");
            return;
        }
        if (puzzles.tryItemUse(player, room, item)) {
            return;
        }
        if (!item.isConsumable()) {
            console.println("You can't consume that.");
            return;
        }
        player.setMaxHp(player.getMaxHp() + item.getAddMaxHp());
        player.setHp(Math.min(player.getMaxHp(), player.getHp() + item.getAddHp()));
        player.setAp(player.getAp() + item.getAddAp());
        player.setDp(player.getDp() + item.getAddDp());
        player.setMp(player.getMp() + item.getAddMp());
        player.removeItem(instance.itemId);
        console.println("You use " + item.getName() + ".");
    }

    private void doEquip(String name) {
        if (name.isEmpty()) {
            console.println("Equip what?");
            return;
        }
        Player.ItemInstance instance = findInInventory(name);
        if (instance == null) {
            console.println("You don't have that.");
            return;
        }
        Item item = content.getItem(instance.itemId);
        if (item == null || item.isConsumable() || item.isKey()) {
            console.println("You can't equip that.");
            return;
        }
        if (instance.equipped) {
            console.println("Already equipped.");
            return;
        }
        for (Player.ItemInstance other : player.getInventory()) {
            if (other.equipped && other.type.equalsIgnoreCase(instance.type)) {
                Item otherItem = content.getItem(other.itemId);
                if (otherItem != null) {
                    player.setAp(player.getAp() - otherItem.getAddAp());
                    player.setDp(player.getDp() - otherItem.getAddDp());
                    player.setMaxHp(player.getMaxHp() - otherItem.getAddMaxHp());
                }
                other.equipped = false;
            }
        }
        instance.equipped = true;
        player.setAp(player.getAp() + item.getAddAp());
        player.setDp(player.getDp() + item.getAddDp());
        player.setMaxHp(player.getMaxHp() + item.getAddMaxHp());
        console.println("You equip " + item.getName() + ".");
    }

    private void doUnequip(String name) {
        if (name.isEmpty()) {
            console.println("Unequip what?");
            return;
        }
        Player.ItemInstance instance = findInInventory(name);
        if (instance == null || !instance.equipped) {
            console.println("That isn't equipped.");
            return;
        }
        Item item = content.getItem(instance.itemId);
        if (item != null) {
            player.setAp(player.getAp() - item.getAddAp());
            player.setDp(player.getDp() - item.getAddDp());
            player.setMaxHp(player.getMaxHp() - item.getAddMaxHp());
        }
        instance.equipped = false;
        console.println("You unequip " + instance.name + ".");
    }

    private void doCast(Room room, String name) {
        if (name.isEmpty()) {
            console.println("Cast what?");
            return;
        }
        Attack attack = findAttackByName(name);
        if (attack == null) {
            console.println("You don't know that spell.");
            return;
        }
        if (!player.knowsAttack(attack.getId())) {
            console.println("You haven't learned that spell.");
            return;
        }
        if (player.getMp() < attack.getMpLevel()) {
            console.println("Your MP level is too low to cast that.");
            return;
        }
        if (puzzles.trySpellCast(player, room, attack)) {
            return;
        }
        if (attack.getAddHp() > 0) {
            player.setHp(Math.min(player.getMaxHp(), player.getHp() + attack.getAddHp()));
            console.println(attack.getDescription());
        } else {
            console.println(attack.getDescription() + " (no effect here.)");
        }
    }

    private void listSpells() {
        console.println();
        console.println("Spells you know:");
        boolean any = false;
        for (Attack attack : content.getAttacks()) {
            if (!player.knowsAttack(attack.getId())) {
                continue;
            }
            String note = player.getMp() < attack.getMpLevel() ? " [needs MP " + attack.getMpLevel() + "]" : "";
            console.println("  - " + attack.getName() + note);
            any = true;
        }
        if (!any) {
            console.println("  (none)");
        }
    }

    private Attack findAttackByName(String name) {
        for (Attack attack : content.getAttacks()) {
            if (attack.getName().equalsIgnoreCase(name)) {
                return attack;
            }
        }
        return null;
    }

    private Player.ItemInstance findInInventory(String name) {
        for (Player.ItemInstance item : player.getInventory()) {
            if (item.name.equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}
