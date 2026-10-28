package billy;

import billy.data.ContentStore;
import billy.model.Attack;
import billy.model.Player;
import billy.model.Room;
import billy.puzzle.PuzzleSystem;
import billy.ui.Console;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PuzzleSystemTest {

    private static ContentStore content;

    @BeforeAll
    static void setUp() throws Exception {
        content = ContentStore.load();
    }

    private PuzzleSystem system() {
        return new PuzzleSystem(new Console(new ByteArrayInputStream(new byte[0]),
                new PrintStream(new ByteArrayOutputStream())), content);
    }

    private Room room(int id) {
        return content.getRoom(id);
    }

    private Attack attack(int id) {
        for (Attack attack : content.getAttacks()) {
            if (attack.getId() == id) {
                return attack;
            }
        }
        throw new IllegalStateException("No attack with id " + id);
    }

    @Test
    void actionPuzzleSolvedWithCorrectChoice() {
        Player player = new Player();
        PuzzleSystem puzzles = system();
        Room room = room(6);
        assertTrue(puzzles.trySpecialAction(player, room, "Investigate Bags"));
        assertTrue(puzzles.choose(player, room, "3"));
        assertTrue(player.isPuzzleSolved(6));
        assertEquals(101, player.getMaxHp(), 0.001); // +1 max HP reward
        assertEquals(100, player.getXp());
    }

    @Test
    void actionPuzzleWrongChoiceCanRetry() {
        Player player = new Player();
        PuzzleSystem puzzles = system();
        Room room = room(6);
        puzzles.trySpecialAction(player, room, "Investigate Bags");
        puzzles.choose(player, room, "1"); // wrong
        assertFalse(player.isPuzzleSolved(6));
        assertTrue(puzzles.choose(player, room, "3")); // correct on retry
        assertTrue(player.isPuzzleSolved(6));
    }

    @Test
    void wrongChoiceInRoom11Kills() {
        Player player = new Player();
        PuzzleSystem puzzles = system();
        Room room = room(11);
        puzzles.trySpecialAction(player, room, "Feel Walls");
        puzzles.choose(player, room, "1"); // wrong -> crushed
        assertTrue(player.getHp() <= 0);
        assertFalse(player.isPuzzleSolved(11));
    }

    @Test
    void itemPuzzleSolvedByUsingCorrectItem() {
        Player player = new Player();
        player.addItem(content.findItemByName("Luminous Pearl"));
        PuzzleSystem puzzles = system();
        Room room = room(14);
        assertTrue(puzzles.tryItemUse(player, room, content.findItemByName("Luminous Pearl")));
        assertTrue(player.isPuzzleSolved(14));
        assertFalse(player.hasItem("Luminous Pearl")); // consumed
        assertEquals(45, player.getAp(), 0.001); // +20 AP
    }

    @Test
    void spellPuzzleSolvedByCorrectSpell() {
        Player player = new Player();
        player.learnAttack(7); // Gust
        player.setMp(5);
        PuzzleSystem puzzles = system();
        assertTrue(puzzles.trySpellCast(player, room(24), attack(7)));
        assertTrue(player.isPuzzleSolved(24));
    }

    @Test
    void forgePuzzleGrantsBothRewards() {
        Player player = new Player();
        player.learnAttack(9); // Fireball
        player.setMp(5);
        PuzzleSystem puzzles = system();
        assertTrue(puzzles.trySpellCast(player, room(31), attack(9)));
        assertTrue(player.isPuzzleSolved(31));
        assertEquals(45, player.getAp(), 0.001); // 25 + 10 + 10
    }

    @Test
    void rainbowPuzzleAcceptsCorrectSequence() {
        Player player = new Player();
        PuzzleSystem puzzles = system();
        Room room = room(23);
        puzzles.trySpecialAction(player, room, "Inspect Panel");
        for (char letter : new char[]{'R', 'O', 'Y', 'G', 'B', 'I', 'V'}) {
            puzzles.choose(player, room, String.valueOf(letter));
        }
        assertTrue(player.isPuzzleSolved(23));
        assertTrue(player.hasItem("Rainbow Cloak"));
    }

    @Test
    void alreadySolvedPuzzleCannotBeRetriggered() {
        Player player = new Player();
        PuzzleSystem puzzles = system();
        Room room = room(6);
        puzzles.trySpecialAction(player, room, "Investigate Bags");
        puzzles.choose(player, room, "3");
        assertFalse(puzzles.trySpecialAction(player, room, "Investigate Bags"));
    }
}
