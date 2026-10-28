package billy.puzzle;

import billy.data.ContentStore;
import billy.model.Attack;
import billy.model.Item;
import billy.model.Player;
import billy.model.Puzzle;
import billy.model.PuzzleChoice;
import billy.model.PuzzleReward;
import billy.model.Room;
import billy.ui.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resolves puzzle interactions: special actions, item use, and spell casting.
 * Puzzle state (solved / active / rainbow input) is kept in {@link Player} flags.
 */
public class PuzzleSystem {

    /** Stale trigger names in the content mapped to the actual attack names. */
    private static final Map<String, String> SPELL_ALIASES = Map.of(
            "Breathe Fire", "Firebreath",
            "Cover In Wax", "Wax Shot",
            "Start Bickering", "Goblin Aura");

    private final Console console;
    private final ContentStore content;

    public PuzzleSystem(Console console, ContentStore content) {
        this.console = console;
        this.content = content;
    }

    /** Lists discoverable special actions for the current room (action puzzles only). */
    public void listActions(Player player, Room room) {
        Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle == null) {
            console.println("There's nothing special to interact with here.");
            return;
        }
        if (player.isPuzzleSolved(room.getId())) {
            console.println("This room's mystery is already solved.");
            return;
        }
        if (puzzle.isAction()) {
            console.println("Available actions:");
            for (String trigger : puzzle.getTriggers()) {
                console.println("  - " + trigger);
            }
        } else {
            console.println("Nothing obvious to interact with, but this room feels unusual.");
        }
    }

    /** Interprets a typed phrase as a special action. Returns true if handled. */
    public boolean trySpecialAction(Player player, Room room, String phrase) {
        Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle == null || !puzzle.isAction() || player.isPuzzleSolved(room.getId())) {
            return false;
        }
        if (!puzzle.hasTrigger(phrase)) {
            return false;
        }
        startAction(player, puzzle);
        return true;
    }

    private void startAction(Player player, Puzzle puzzle) {
        player.setFlag("puzzle_" + puzzle.getRoomId() + "_active", "true");
        console.println(puzzle.getDescription());
        if (puzzle.getRoomId() == 23) {
            console.println("(Enter one color letter at a time: R, O, Y, G, B, I, V)");
        } else {
            for (PuzzleChoice choice : puzzle.getChoices()) {
                console.println("  " + choice.getChoice());
            }
        }
    }

    /** Handles a player's choice once an action puzzle is active. Returns true if handled. */
    public boolean choose(Player player, Room room, String input) {
        Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle == null || !puzzle.isAction() || player.isPuzzleSolved(room.getId())) {
            return false;
        }
        if (!player.flagTrue("puzzle_" + room.getId() + "_active")) {
            return false;
        }
        if (puzzle.getRoomId() == 23) {
            return handleRainbow(player, puzzle, input);
        }
        return handleChoice(player, puzzle, input);
    }

    private boolean handleChoice(Player player, Puzzle puzzle, String input) {
        for (PuzzleChoice choice : puzzle.getChoices()) {
            if (!choice.getChoice().equalsIgnoreCase(input)) {
                continue;
            }
            if (choice.getChoice().equalsIgnoreCase(puzzle.getSolution())) {
                console.println(choice.getContent());
                solve(player, puzzle);
            } else {
                console.println(choice.getContent());
                applyPenalty(player, puzzle.getRoomId());
            }
            return true;
        }
        return false;
    }

    private boolean handleRainbow(Player player, Puzzle puzzle, String input) {
        String trimmed = input.trim();
        if (trimmed.length() != 1) {
            return false; // let movement words (e.g. "back") pass through
        }
        String letter = trimmed.toUpperCase();
        if ("ROYGBIV".indexOf(letter) < 0) {
            console.println(choiceContent(puzzle, "Wrong Letter"));
            return true;
        }
        String current = player.getFlag("puzzle_" + puzzle.getRoomId() + "_input");
        if (current == null) {
            current = "";
        }
        if (current.indexOf(letter) >= 0) {
            console.println(choiceContent(puzzle, "Repeat"));
            return true;
        }
        String next = current + letter;
        player.setFlag("puzzle_" + puzzle.getRoomId() + "_input", next);
        if (next.length() == 7) {
            if (next.equalsIgnoreCase(puzzle.getSolution())) {
                console.println(choiceContent(puzzle, puzzle.getSolution()));
                solve(player, puzzle);
            } else {
                console.println(choiceContent(puzzle, "Incorrect"));
            }
            player.setFlag("puzzle_" + puzzle.getRoomId() + "_input", "");
        } else {
            console.println(choiceContent(puzzle, "Next") + " (entered: " + next + ")");
        }
        return true;
    }

    /** Returns true if using the item solved a puzzle (and consumed the item). */
    public boolean tryItemUse(Player player, Room room, Item item) {
        Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle == null || !"item".equalsIgnoreCase(puzzle.getType())
                || player.isPuzzleSolved(room.getId())) {
            return false;
        }
        if (!puzzle.hasTrigger(item.getName())) {
            return false;
        }
        console.println(choiceContent(puzzle, item.getName()));
        solve(player, puzzle);
        player.removeItem(item.getId());
        return true;
    }

    /** Returns true if casting the spell resolved a spell puzzle. */
    public boolean trySpellCast(Player player, Room room, Attack attack) {
        Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle == null || !"spell".equalsIgnoreCase(puzzle.getType())
                || player.isPuzzleSolved(room.getId())) {
            return false;
        }
        String trigger = matchedTrigger(puzzle, attack);
        if (trigger == null) {
            String content = choiceContent(puzzle, "Wrong Spell");
            console.println(content.isEmpty() ? "Nothing appears to happen." : content);
            return true;
        }
        String content = choiceContent(puzzle, trigger);
        if (content.isEmpty()) {
            content = choiceContent(puzzle, "Learned");
        }
        if (content.isEmpty()) {
            content = "Your spell resonates with the room.";
        }
        console.println(content);
        solve(player, puzzle);
        return true;
    }

    private String matchedTrigger(Puzzle puzzle, Attack attack) {
        for (String trigger : puzzle.getTriggers()) {
            String canonical = SPELL_ALIASES.getOrDefault(trigger, trigger);
            if (canonical.equalsIgnoreCase(attack.getName())) {
                return trigger;
            }
        }
        return null;
    }

    private void solve(Player player, Puzzle puzzle) {
        for (PuzzleReward reward : puzzle.getRewards()) {
            applyReward(player, reward);
        }
        player.markPuzzleSolved(puzzle.getRoomId());
        player.setFlag("puzzle_" + puzzle.getRoomId() + "_active", "false");
        player.addXp(100);
        console.println("(Puzzle solved! +100 XP)");
    }

    private void applyReward(Player player, PuzzleReward reward) {
        String itemName = reward.getItemName();
        if (itemName != null && !itemName.isEmpty()) {
            Item item = content.findItemByName(itemName);
            if (item != null && !player.hasItem(itemName)) {
                player.addItem(item);
                console.println("You received: " + itemName + ".");
            }
            return;
        }
        List<String> parts = new ArrayList<>();
        if (reward.getMaxHp() > 0) {
            player.setMaxHp(player.getMaxHp() + reward.getMaxHp());
            parts.add("+" + fmt(reward.getMaxHp()) + " max HP");
        }
        if (reward.getHp() > 0) {
            player.setHp(Math.min(player.getMaxHp(), player.getHp() + reward.getHp()));
            parts.add("+" + fmt(reward.getHp()) + " HP");
        }
        if (reward.getAp() > 0) {
            player.setAp(player.getAp() + reward.getAp());
            parts.add("+" + fmt(reward.getAp()) + " AP");
        }
        if (!parts.isEmpty()) {
            console.println("Reward: " + String.join(", ", parts) + ".");
        }
    }

    private void applyPenalty(Player player, int roomId) {
        if (roomId == 11) {
            console.println("The walls close in...");
            player.setHp(0);
        } else if (roomId == 13) {
            player.setHp(Math.max(1, player.getHp() - 25));
            console.println("You lose 25 HP from the poison!");
        }
    }

    private String choiceContent(Puzzle puzzle, String key) {
        for (PuzzleChoice choice : puzzle.getChoices()) {
            if (choice.getChoice().equalsIgnoreCase(key)) {
                return choice.getContent();
            }
        }
        return "";
    }

    private String fmt(double value) {
        return value == Math.floor(value) ? String.valueOf((long) value) : String.valueOf(value);
    }
}
