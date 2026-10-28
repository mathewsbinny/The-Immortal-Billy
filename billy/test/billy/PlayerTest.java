package billy;

import billy.model.Item;
import billy.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    @Test
    void newPlayerHasDefaults() {
        Player player = new Player();
        assertEquals(100, player.getHp(), 0.001);
        assertEquals(100, player.getMaxHp(), 0.001);
        assertEquals(25, player.getAp(), 0.001);
        assertEquals(10, player.getDp(), 0.001);
        assertEquals(1, player.getLevel());
        assertEquals(1, player.getCurrentRoomId());
        assertEquals(0, player.countKeys());
        assertTrue(player.getInventory().isEmpty());
        assertTrue(player.getFlags().isEmpty());
    }

    @Test
    void addItemAndCountKeys() {
        Player player = new Player();
        player.addItem(new Item(26, 12, "Earth Key", "Key", 0, 0, 0, 0, 0, false));
        player.addItem(new Item(27, 19, "Water Key", "Key", 0, 0, 0, 0, 0, false));
        player.addItem(new Item(3, 6, "Health Potion", "Consumable", 100, 100, 0, 0, 0, true));
        assertEquals(2, player.countKeys());
        assertTrue(player.hasItem("Earth Key"));
        assertTrue(player.hasItem("health potion"));
        assertFalse(player.hasItem("Fire Key"));
    }

    @Test
    void learnAndKnowAttacks() {
        Player player = new Player();
        assertFalse(player.knowsAttack(1));
        player.learnAttack(1);
        assertTrue(player.knowsAttack(1));
        assertFalse(player.knowsAttack(9));
    }

    @Test
    void monsterDefeatedFlags() {
        Player player = new Player();
        assertFalse(player.isMonsterDefeated(1));
        player.markMonsterDefeated(1);
        assertTrue(player.isMonsterDefeated(1));
        assertFalse(player.isMonsterDefeated(2));
    }

    @Test
    void removeItemById() {
        Player player = new Player();
        player.addItem(new Item(3, 6, "Health Potion", "Consumable", 100, 100, 0, 0, 0, true));
        player.removeItem(3);
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void manualLevelUpConsumesXpAndAppliesStat() {
        Player player = new Player();
        assertFalse(player.canLevelUp());
        player.addXp(100);
        assertTrue(player.canLevelUp());
        player.levelUp(Player.StatChoice.ATTACK);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getXp());
        assertEquals(30, player.getAp(), 0.001);
        assertFalse(player.canLevelUp());
    }

    @Test
    void levelUpCostGrowsWithLevel() {
        Player player = new Player();
        player.addXp(100);
        player.levelUp(Player.StatChoice.MAX_HEALTH);
        assertEquals(2, player.getLevel());
        assertEquals(125, player.getMaxHp(), 0.001);
        player.addXp(100);
        assertFalse(player.canLevelUp()); // level 2 needs 200 XP
        player.addXp(100);
        assertTrue(player.canLevelUp());
    }

    @Test
    void eachStatChoiceApplies() {
        Player player = new Player();
        player.addXp(100);
        player.levelUp(Player.StatChoice.DEFENSE);
        assertEquals(15, player.getDp(), 0.001);

        player.addXp(200);
        player.levelUp(Player.StatChoice.MAGIC);
        assertEquals(2, player.getMp());
    }
}
