package billy;

import billy.data.SaveStore;
import billy.model.Account;
import billy.model.Item;
import billy.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveStoreTest {

    @TempDir
    Path tempDir;

    private SaveStore store() throws Exception {
        return new SaveStore(tempDir.resolve("save-test.db").toString());
    }

    @Test
    void createAndAuthenticate() throws Exception {
        SaveStore store = store();
        Account account = store.createAccount("alice", "secret");
        assertTrue(account.getId() > 0);

        Account loggedIn = store.authenticate("alice", "secret");
        assertNotNull(loggedIn);
        assertEquals(account.getId(), loggedIn.getId());

        assertNull(store.authenticate("alice", "wrong"));
        assertNull(store.authenticate("nobody", "secret"));
    }

    @Test
    void duplicateUsernameIsRejected() throws Exception {
        SaveStore store = store();
        store.createAccount("bob", "secret");
        assertThrows(GameException.class, () -> store.createAccount("bob", "other"));
    }

    @Test
    void saveAndLoadRoundTrip() throws Exception {
        SaveStore store = store();
        Account account = store.createAccount("carol", "secret");
        Player player = store.newPlayer(account.getId(), "carol");
        player.setHp(42);
        player.setMaxHp(150);
        player.setAp(30);
        player.setDp(12);
        player.setMp(3);
        player.setXp(250);
        player.setLevel(3);
        player.setDeaths(2);
        player.setMonstersKilled(5);
        player.setCurrentRoomId(12);
        player.addItem(new Item(3, 6, "Health Potion", "Consumable", 100, 100, 0, 0, 0, true));
        player.addItem(new Item(26, 12, "Earth Key", "Key", 0, 0, 0, 0, 0, false));
        player.learnAttack(7);
        player.markMonsterDefeated(1);
        store.savePlayer(player, 1);

        Player loaded = store.loadPlayer(account.getId(), 1);
        assertNotNull(loaded);
        assertEquals("carol", loaded.getName());
        assertEquals(42, loaded.getHp(), 0.001);
        assertEquals(150, loaded.getMaxHp(), 0.001);
        assertEquals(30, loaded.getAp(), 0.001);
        assertEquals(12, loaded.getDp(), 0.001);
        assertEquals(3, loaded.getMp());
        assertEquals(10, loaded.getMaxMp());
        assertEquals(250, loaded.getXp());
        assertEquals(3, loaded.getLevel());
        assertEquals(2, loaded.getDeaths());
        assertEquals(5, loaded.getMonstersKilled());
        assertEquals(12, loaded.getCurrentRoomId());
        assertEquals(1, loaded.countKeys());
        assertTrue(loaded.hasItem("Health Potion"));
        assertTrue(loaded.knowsAttack(7));
        assertTrue(loaded.isMonsterDefeated(1));
    }

    @Test
    void loadMissingSaveReturnsNull() throws Exception {
        SaveStore store = store();
        assertNull(store.loadPlayer(12345, 1));
    }

    @Test
    void newPlayerKnowsPunch() throws Exception {
        SaveStore store = store();
        Player player = store.newPlayer(7, "dave");
        assertEquals(7, player.getAccountId());
        assertEquals("dave", player.getName());
        assertTrue(player.knowsAttack(1));
    }

    @Test
    void updatePasswordChangesLogin() throws Exception {
        SaveStore store = store();
        Account account = store.createAccount("erin", "oldpass");
        store.updatePassword(account.getId(), "newpass");
        assertNull(store.authenticate("erin", "oldpass"));
        assertNotNull(store.authenticate("erin", "newpass"));
    }

    @Test
    void deleteAccountRemovesAccountAndSave() throws Exception {
        SaveStore store = store();
        Account account = store.createAccount("frank", "pass");
        Player player = store.newPlayer(account.getId(), "frank");
        store.savePlayer(player, 1);
        store.deleteAccount(account.getId());
        assertNull(store.authenticate("frank", "pass"));
        assertNull(store.loadPlayer(account.getId(), 1));
    }

    @Test
    void threeSaveSlotsAreIndependent() throws Exception {
        SaveStore store = store();
        Account account = store.createAccount("gina", "pass");

        Player p1 = store.newPlayer(account.getId(), "gina");
        p1.setCurrentRoomId(10);
        store.savePlayer(p1, 1);

        Player p2 = store.newPlayer(account.getId(), "gina");
        p2.setCurrentRoomId(20);
        p2.setLevel(5);
        store.savePlayer(p2, 2);

        Player p3 = store.newPlayer(account.getId(), "gina");
        p3.setCurrentRoomId(30);
        store.savePlayer(p3, 3);

        assertEquals(10, store.loadPlayer(account.getId(), 1).getCurrentRoomId());
        assertEquals(20, store.loadPlayer(account.getId(), 2).getCurrentRoomId());
        assertEquals(5, store.loadPlayer(account.getId(), 2).getLevel());
        assertEquals(30, store.loadPlayer(account.getId(), 3).getCurrentRoomId());
    }
}
