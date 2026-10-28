package billy;

import billy.data.ContentStore;
import billy.data.SaveStore;
import billy.model.Player;
import billy.ui.Console;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private static ContentStore content;

    @TempDir
    Path tempDir;

    @BeforeAll
    static void setUp() throws Exception {
        content = ContentStore.load();
    }

    private Console consoleWith(String input) {
        return new Console(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(new ByteArrayOutputStream()));
    }

    @Test
    void quitSavesAndExits() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("game.db").toString());
        Player player = store.newPlayer(1, "tester");
        new Game(consoleWith("quit\n"), store, content, player).run();
        assertNotNull(store.loadPlayer(1, 1));
    }

    @Test
    void invalidAndBlankInputDoesNotCrash() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("game2.db").toString());
        Player player = store.newPlayer(1, "tester");
        new Game(consoleWith("gibberish\n\nquit\n"), store, content, player).run();
        assertNotNull(store.loadPlayer(1, 1));
    }

    @Test
    void levelUpCommandRaisesChosenStat() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("game4.db").toString());
        Player player = store.newPlayer(1, "tester");
        player.addXp(100);
        new Game(consoleWith("levelup\n1\nquit\n"), store, content, player).run();
        assertEquals(2, player.getLevel());
        assertEquals(125, player.getMaxHp(), 0.001);
    }

    @Test
    void quitReturnsToMenu() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("game5.db").toString());
        Player player = store.newPlayer(1, "tester");
        Game game = new Game(consoleWith("quit\n"), store, content, player);
        assertTrue(game.run());
    }

    @Test
    void exitExitsProgram() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("game6.db").toString());
        Player player = store.newPlayer(1, "tester");
        Game game = new Game(consoleWith("exit\n"), store, content, player);
        assertFalse(game.run());
    }

    @Test
    void unequippingHealthGearClampsHpToMax() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("game7.db").toString());
        Player player = store.newPlayer(1, "tester");
        // Stone Sword grants +50 max HP; simulate the equipped-and-overhealed state.
        player.addItem(content.findItemByName("Stone Sword"), true);
        player.setMaxHp(150);
        player.setHp(150);

        new Game(consoleWith("unequip stone sword\nquit\n"), store, content, player).run();

        assertEquals(100, player.getMaxHp(), 0.001);
        assertEquals(100, player.getHp(), 0.001);
    }
}
