package billy;

import billy.combat.CombatEngine;
import billy.data.ContentStore;
import billy.model.Element;
import billy.model.Monster;
import billy.model.Player;
import billy.ui.Console;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

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

    private String repeat(String token, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(token);
        }
        return sb.toString();
    }

    @Test
    void victoryAgainstRegularMonster() {
        Player player = defaultPlayer();
        // A harmless monster (no attacks) keeps the outcome independent of enemy rolls.
        Monster monster = new Monster(1, 1, "Training Dummy", "monster", 100, 0, 0, 0, Element.PHYSICAL);
        CombatEngine engine = new CombatEngine(consoleWith("1\n1\n1\n1\n1\n"), content, new Random(1));
        assertEquals(CombatEngine.Outcome.VICTORY, engine.fight(player, monster, content.getAttacks()));
        assertTrue(player.getHp() > 0);
    }

    @Test
    void runAwayDealsNoDamage() {
        Player player = defaultPlayer();
        Monster monster = content.getMonster(1);
        CombatEngine engine = new CombatEngine(consoleWith("4\n"), content, new Random(1));
        assertEquals(CombatEngine.Outcome.RAN_AWAY, engine.fight(player, monster, content.getAttacks()));
        assertEquals(100, player.getHp(), 0.001);
    }

    @Test
    void defeatWhenPlayerHpReachesZero() {
        Player player = defaultPlayer();
        player.setHp(5);
        // Overwhelming monster: the player cannot kill it, and one hit is lethal.
        Monster monster = new Monster(99, 99, "Overwhelming Beast", "monster", 100000, 0, 1000, 0, Element.PHYSICAL);
        CombatEngine engine = new CombatEngine(consoleWith(repeat("1\n", 30)), content, new Random(1));
        assertEquals(CombatEngine.Outcome.DEFEAT, engine.fight(player, monster, content.getAttacks()));
        assertTrue(player.getHp() <= 0);
    }

    @Test
    void learnedSpellCanBeCast() {
        Player player = defaultPlayer();
        player.learnAttack(9); // Fireball
        player.setMp(5);
        Monster monster = content.getMonster(9); // Fire Elemental
        CombatEngine engine = new CombatEngine(consoleWith("2\n1\n"), content, new Random(1));
        assertEquals(CombatEngine.Outcome.VICTORY, engine.fight(player, monster, content.getAttacks()));
        assertEquals(1, player.getMp()); // Fireball costs 4 MP
    }
}
