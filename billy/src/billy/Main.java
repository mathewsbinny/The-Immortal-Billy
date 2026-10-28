package billy;

import billy.data.ContentStore;
import billy.data.SaveStore;
import billy.model.Player;
import billy.ui.Console;
import billy.ui.MainMenu;

/**
 * Application entry point.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Console console = new Console();
            SaveStore saveStore = new SaveStore("save.db");
            ContentStore content = ContentStore.load();
            MainMenu menu = new MainMenu(console, saveStore);

            Player player = menu.run();
            if (player == null) {
                console.println("Goodbye!");
                return;
            }

            new Game(console, saveStore, content, player).run();
            console.println("Thanks for playing!");
        } catch (GameException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
