package billy;

import billy.combat.CombatEngine;
import billy.data.ContentStore;
import billy.model.Monster;
import billy.model.Player;
import billy.ui.Console;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatEngineTest {

    private static ContentStore content;

    @BeforeAll
    static void setUp() throws Exception {
        content = ContentStore.load();
    }

    private Console consoleWith(String input) {
        return new Console(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(new ByteArrayOutputStream()));
    }

    private Player defaultPlayer() {
        Player player = new Player();
        player.learnAttack(1);
        return player;
    }

    @Test
    void victoryAgainstRegularMonster() {
        Player player = defaultPlayer();
        Monster monster = content.getMonster(1); // Earth Elemental, 100 HP
        CombatEngine engine = new CombatEngine(consoleWith("1\n1\n1\n1\n1\n"), content);
        assertEquals(CombatEngine.Outcome.VICTORY, engine.fight(player, monster, content.getAttacks()));
        assertTrue(player.getHp() > 0);
    }

    @Test
    void runAwayDealsNoDamage() {
        Player player = defaultPlayer();
        Monster monster = content.getMonster(1);
        CombatEngine engine = new CombatEngine(consoleWith("4\n"), content);
        assertEquals(CombatEngine.Outcome.RAN_AWAY, engine.fight(player, monster, content.getAttacks()));
        assertEquals(100, player.getHp(), 0.001);
    }

    @Test
    void defeatWhenPlayerHpReachesZero() {
        Player player = defaultPlayer();
        player.setHp(5);
        Monster monster = content.getMonster(1); // hits for 40
        CombatEngine engine = new CombatEngine(consoleWith("1\n"), content);
        assertEquals(CombatEngine.Outcome.DEFEAT, engine.fight(player, monster, content.getAttacks()));
        assertTrue(player.getHp() <= 0);
    }

    @Test
    void learnedSpellCanBeCast() {
        Player player = defaultPlayer();
        player.learnAttack(9); // Fireball
        player.setMp(5);
        Monster monster = content.getMonster(9); // Fire Elemental
        CombatEngine engine = new CombatEngine(consoleWith("2\n1\n"), content);
        assertEquals(CombatEngine.Outcome.VICTORY, engine.fight(player, monster, content.getAttacks()));
    }
}
