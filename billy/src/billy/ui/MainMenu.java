package billy.ui;

import billy.GameException;
import billy.data.SaveStore;
import billy.model.Account;
import billy.model.Player;

/**
 * Login / account-creation flow shown when the game starts.
 */
public class MainMenu {

    private final Console console;
    private final SaveStore saveStore;

    public MainMenu(Console console, SaveStore saveStore) {
        this.console = console;
        this.saveStore = saveStore;
    }

    /** Runs the menu; returns the player to play as, or null to quit. */
    public Player run() throws GameException {
        console.println("=== THE IMMORTAL BILLY ===");
        while (true) {
            console.println();
            console.println("1. Log in");
            console.println("2. Create a new account");
            console.println("3. Quit");
            String choice = console.prompt("Choice: ");
            if (choice == null) {
                return null;
            }
            switch (choice.trim()) {
                case "1": {
                    Player player = login();
                    if (player != null) {
                        return player;
                    }
                    break;
                }
                case "2": {
                    Player player = createAccount();
                    if (player != null) {
                        return player;
                    }
                    break;
                }
                case "3":
                    return null;
                default:
                    console.println("Please choose 1, 2, or 3.");
            }
        }
    }

    private Player login() throws GameException {
        String username = console.prompt("Username: ");
        if (username == null) {
            return null;
        }
        String password = console.prompt("Password: ");
        if (password == null) {
            return null;
        }
        Account account = saveStore.authenticate(username.trim(), password);
        if (account == null) {
            console.println("Invalid username or password.");
            return null;
        }
        return loadOrCreate(account);
    }

    private Player createAccount() throws GameException {
        String username = console.prompt("Choose a username: ");
        if (username == null) {
            return null;
        }
        String password = console.prompt("Choose a password (min 4 characters): ");
        if (password == null) {
            return null;
        }
        if (password.length() < 4) {
            console.println("Password must be at least 4 characters.");
            return null;
        }
        Account account = saveStore.createAccount(username.trim(), password);
        Player player = saveStore.newPlayer(account.getId(), username.trim());
        saveStore.savePlayer(player);
        console.println("Account created. Welcome, " + player.getName() + "!");
        return player;
    }

    private Player loadOrCreate(Account account) throws GameException {
        Player player = saveStore.loadPlayer(account.getId());
        if (player == null) {
            player = saveStore.newPlayer(account.getId(), account.getUsername());
            saveStore.savePlayer(player);
            console.println("No save found; a new adventure begins.");
        } else {
            console.println("Welcome back, " + player.getName() + "!");
        }
        return player;
    }
}
