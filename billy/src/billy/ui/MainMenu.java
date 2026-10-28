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
                    Account account = login();
                    if (account != null) {
                        Player player = accountMenu(account);
                        if (player != null) {
                            return player;
                        }
                    }
                    break;
                }
                case "2": {
                    Account account = createAccount();
                    if (account != null) {
                        Player player = accountMenu(account);
                        if (player != null) {
                            return player;
                        }
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

    private Account login() throws GameException {
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
        return account;
    }

    private Account createAccount() throws GameException {
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
        console.println("Account created. Welcome, " + account.getUsername() + "!");
        return account;
    }

    private Player playGame(Account account) throws GameException {
        while (true) {
            console.println();
            console.println("Choose a save slot:");
            for (int slot = 1; slot <= 3; slot++) {
                Player existing = saveStore.loadPlayer(account.getId(), slot);
                if (existing == null) {
                    console.println("  " + slot + ". Empty");
                } else {
                    console.println("  " + slot + ". " + existing.getName()
                            + " (Level " + existing.getLevel() + ", Room " + existing.getCurrentRoomId() + ")");
                }
            }
            String choice = console.prompt("Slot (1-3) or BACK: ");
            if (choice == null || choice.trim().equalsIgnoreCase("back")) {
                return null;
            }
            try {
                int slot = Integer.parseInt(choice.trim());
                if (slot < 1 || slot > 3) {
                    console.println("Choose 1, 2, or 3.");
                    continue;
                }
                Player player = saveStore.loadPlayer(account.getId(), slot);
                if (player == null) {
                    player = saveStore.newPlayer(account.getId(), account.getUsername());
                    player.setSaveSlot(slot);
                    saveStore.savePlayer(player, slot);
                    console.println("New adventure started in slot " + slot + ".");
                } else {
                    player.setSaveSlot(slot);
                    console.println("Welcome back, " + player.getName() + "! (slot " + slot + ")");
                }
                return player;
            } catch (NumberFormatException ignored) {
                console.println("Choose 1, 2, or 3.");
            }
        }
    }

    /** Post-login menu. The game only starts when the player chooses Play Game. */
    private Player accountMenu(Account account) throws GameException {
        while (true) {
            console.println();
            console.println("Logged in as " + account.getUsername());
            console.println("1. Play Game");
            console.println("2. Change Password");
            console.println("3. Delete Account");
            console.println("4. Log Out");
            String choice = console.prompt("Choice: ");
            if (choice == null) {
                return null;
            }
            switch (choice.trim()) {
                case "1":
                    return playGame(account);
                case "2":
                    changePassword(account);
                    break;
                case "3":
                    if (deleteAccount(account)) {
                        return null;
                    }
                    break;
                case "4":
                    return null;
                default:
                    console.println("Please choose 1, 2, 3, or 4.");
            }
        }
    }

    private void changePassword(Account account) throws GameException {
        String current = console.prompt("Current password: ");
        if (current == null) {
            return;
        }
        if (saveStore.authenticate(account.getUsername(), current) == null) {
            console.println("Incorrect password.");
            return;
        }
        String next = console.prompt("New password (min 4 characters): ");
        if (next == null) {
            return;
        }
        if (next.length() < 4) {
            console.println("Password must be at least 4 characters.");
            return;
        }
        saveStore.updatePassword(account.getId(), next);
        console.println("Password changed.");
    }

    private boolean deleteAccount(Account account) throws GameException {
        String confirm = console.prompt("Type DELETE to permanently delete this account and its save: ");
        if (confirm != null && confirm.trim().equalsIgnoreCase("DELETE")) {
            saveStore.deleteAccount(account.getId());
            console.println("Account deleted.");
            return true;
        }
        console.println("Cancelled.");
        return false;
    }
}
