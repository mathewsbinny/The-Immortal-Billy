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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
