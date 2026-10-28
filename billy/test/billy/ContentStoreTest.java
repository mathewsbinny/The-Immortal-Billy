package billy;

import billy.data.ContentStore;
import billy.model.Item;
import billy.model.Monster;
import billy.model.Puzzle;
import billy.model.Room;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentStoreTest {

    private static ContentStore content;

    @BeforeAll
    static void setUp() throws Exception {
        content = ContentStore.load();
    }

    @Test
    void loadsAllRooms() {
        assertEquals(34, content.getRooms().size());
        Room hub = content.getRoom(1);
        assertNotNull(hub);
        assertEquals("Central Hub", hub.getName());
        assertEquals("Center", hub.getRegion());
        assertEquals("normal", hub.getType());
        assertEquals("Throne Room of the Elementals", content.getRoom(34).getName());
    }

    @Test
    void loadsMonstersAndFindsByRoom() {
        assertEquals(20, content.getMonsters().size());
        Monster stoneAngel = content.findMonsterByRoom(12);
        assertNotNull(stoneAngel);
        assertEquals("The Stone Angel", stoneAngel.getName());
        assertTrue(stoneAngel.isMiniboss());
        assertFalse(stoneAngel.getAttacks().isEmpty());
        assertNull(content.findMonsterByRoom(1));
    }

    @Test
    void loadsItemsAndFindsByName() {
        assertEquals(29, content.getItems().size());
        Item key = content.findItemByName("Earth Key");
        assertNotNull(key);
        assertTrue(key.isKey());
        assertEquals(12, key.getRoomId());
        assertNull(content.findItemByName("No Such Item"));
    }

    @Test
    void loadsAttacksPuzzlesAndHelp() {
        assertEquals(12, content.getAttacks().size());
        assertEquals(8, content.getPuzzles().size());
        assertEquals(10, content.getHelp().size());
    }

    @Test
    void puzzleHasChoicesRewardsAndTriggers() {
        Puzzle puzzle = content.getPuzzle(6);
        assertNotNull(puzzle);
        assertEquals("action", puzzle.getType());
        assertEquals("3", puzzle.getSolution());
        assertEquals(3, puzzle.getChoices().size());
        assertEquals(1, puzzle.getRewards().size());
        assertEquals(1, puzzle.getTriggers().size());
        assertEquals("Investigate Bags", puzzle.getTriggers().get(0));
    }

    @Test
    void forgePuzzleHasTwoRewardsAndFourTriggers() {
        Puzzle puzzle = content.getPuzzle(31);
        assertEquals(2, puzzle.getRewards().size());
        assertEquals(4, puzzle.getTriggers().size());
    }
}
