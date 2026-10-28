package billy;

import billy.data.ContentStore;
import billy.data.SaveStore;
import billy.model.Account;
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

            while (true) {
                Account account = menu.loginOrCreate();
                if (account == null) {
                    console.println("Goodbye!");
                    return;
                }
                while (true) {
                    Player player = menu.accountMenu(account);
                    if (player == null) {
                        break;
                    }
                    Game game = new Game(console, saveStore, content, player);
                    boolean returnToMenu = game.run();
                    if (!returnToMenu) {
                        console.println("Thanks for playing!");
                        return;
                    }
                }
            }
        } catch (GameException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
