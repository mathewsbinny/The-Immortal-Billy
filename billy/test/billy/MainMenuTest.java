package billy;

import billy.data.SaveStore;
import billy.model.Player;
import billy.ui.Console;
import billy.ui.MainMenu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MainMenuTest {

    @TempDir
    Path tempDir;

    private Console consoleWith(String input) {
        return new Console(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(new ByteArrayOutputStream()));
    }

    @Test
    void createAccountReturnsPlayer() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("menu.db").toString());
        MainMenu menu = new MainMenu(consoleWith("2\ntestuser\npass123\n"), store);
        Player player = menu.run();
        assertNotNull(player);
        assertEquals("testuser", player.getName());
    }

    @Test
    void loginReturnsExistingPlayer() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("menu2.db").toString());
        store.createAccount("testuser", "pass123");
        MainMenu menu = new MainMenu(consoleWith("1\ntestuser\npass123\n"), store);
        Player player = menu.run();
        assertNotNull(player);
        assertEquals("testuser", player.getName());
    }

    @Test
    void wrongPasswordThenQuitReturnsNull() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("menu3.db").toString());
        store.createAccount("testuser", "pass123");
        MainMenu menu = new MainMenu(consoleWith("1\ntestuser\nwrong\n3\n"), store);
        assertNull(menu.run());
    }

    @Test
    void shortPasswordIsRejected() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("menu4.db").toString());
        MainMenu menu = new MainMenu(consoleWith("2\ntestuser\nabc\n3\n"), store);
        assertNull(menu.run());
    }
}
