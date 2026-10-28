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

    /** Convenience: logs in (or creates an account) and runs one account-menu session. */
    public Player run() throws GameException {
        Account account = loginOrCreate();
        if (account == null) {
            return null;
        }
        return accountMenu(account);
    }

    /** Shows the login / create / quit menu. Returns the account, or null to quit. */
    public Account loginOrCreate() throws GameException {
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
                        return account;
                    }
                    break;
                }
                case "2": {
                    Account account = createAccount();
                    if (account != null) {
                        return account;
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
        Account account;
        try {
            account = saveStore.createAccount(username.trim(), password);
        } catch (GameException e) {
            console.println(e.getMessage());
            return null;
        }
        console.println("Account created. Welcome, " + account.getUsername() + "!");
        return account;
    }

    private void listSaveSlots(Account account) throws GameException {
        for (int slot = 1; slot <= 3; slot++) {
            Player existing = saveStore.loadPlayer(account.getId(), slot);
            if (existing == null) {
                console.println("  " + slot + ". Empty");
            } else {
                console.println("  " + slot + ". " + existing.getName()
                        + " (Level " + existing.getLevel() + ", Room " + existing.getCurrentRoomId() + ")");
            }
        }
    }

    /** Lets the player erase one save slot without deleting the account. */
    private void deleteSaveSlot(Account account) throws GameException {
        while (true) {
            console.println();
            console.println("Choose a save slot to delete:");
            listSaveSlots(account);
            String choice = console.prompt("Slot (1-3) or BACK: ");
            if (choice == null || choice.trim().equalsIgnoreCase("back")) {
                return;
            }
            try {
                int slot = Integer.parseInt(choice.trim());
                if (slot < 1 || slot > 3) {
                    console.println("Choose 1, 2, or 3.");
                    continue;
                }
                if (saveStore.loadPlayer(account.getId(), slot) == null) {
                    console.println("That slot is already empty.");
                    continue;
                }
                String confirm = console.prompt("Type DELETE to erase this save slot: ");
                if (confirm != null && confirm.trim().equalsIgnoreCase("DELETE")) {
                    saveStore.deleteSave(account.getId(), slot);
                    console.println("Save slot " + slot + " deleted.");
                } else {
                    console.println("Cancelled.");
                }
                return;
            } catch (NumberFormatException ignored) {
                console.println("Choose 1, 2, or 3.");
            }
        }
    }

    private Player playGame(Account account) throws GameException {
        while (true) {
            console.println();
            console.println("Choose a save slot:");
            listSaveSlots(account);
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
    public Player accountMenu(Account account) throws GameException {
        while (true) {
            console.println();
            console.println("Logged in as " + account.getUsername());
            console.println("1. Play Game");
            console.println("2. Change Password");
            console.println("3. Delete Account");
            console.println("4. Delete Save Slot");
            console.println("5. Log Out");
            String choice = console.prompt("Choice: ");
            if (choice == null) {
                return null;
            }
            switch (choice.trim()) {
                case "1": {
                    Player selected = playGame(account);
                    if (selected != null) {
                        return selected;
                    }
                    break;
                }
                case "2":
                    changePassword(account);
                    break;
                case "3":
                    if (deleteAccount(account)) {
                        return null;
                    }
                    break;
                case "4":
                    deleteSaveSlot(account);
                    break;
                case "5":
                    return null;
                default:
                    console.println("Please choose 1, 2, 3, 4, or 5.");
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
