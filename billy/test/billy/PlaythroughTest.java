package billy;

import billy.data.ContentStore;
import billy.data.SaveStore;
import billy.model.Monster;
import billy.model.Player;
import billy.model.Puzzle;
import billy.model.Room;
import billy.ui.Console;
import billy.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end playthrough: drives the real {@link Game} loop with a generated,
 * scripted input through all four districts (combat + puzzles + minibosses),
 * collects the four keys, unlocks the throne room, and defeats the final boss.
 *
 * <p>Uses a boosted player and a fixed seed so the run is deterministic.</p>
 */
class PlaythroughTest {

    private static final long SEED = 42L;

    private static ContentStore content;

    @TempDir
    Path tempDir;

    @BeforeAll
    static void setUp() throws Exception {
        content = ContentStore.load();
    }

    @Test
    void fullPlaythroughToVictory() throws Exception {
        SaveStore store = new SaveStore(tempDir.resolve("playthrough.db").toString());
        Player player = boostedPlayer(store);

        // Build a world with the same seed to pre-compute the scripted input.
        World world = new World(content, new Random(SEED));
        String script = generateScript(world);

        new Game(consoleWith(script), store, content, player, new Random(SEED)).run();

        assertEquals(4, player.countKeys(), "expected all four district keys");
        for (int minibossId : new int[]{12, 13, 14, 15}) {
            assertTrue(player.isMonsterDefeated(minibossId), "miniboss " + minibossId + " should be defeated");
        }
        assertTrue(player.flagTrue("game_completed"), "final boss should be defeated");
        assertTrue(player.getXp() > 0, "the player should have earned XP");
    }

    private Player boostedPlayer(SaveStore store) throws Exception {
        Player player = store.newPlayer(1, "Hero");
        player.setHp(10000);
        player.setMaxHp(10000);
        player.setAp(10000);
        player.setDp(10000);
        player.setMp(10);
        for (billy.model.Attack attack : content.getAttacks()) {
            player.learnAttack(attack.getId());
        }
        player.addItem(content.findItemByName("Luminous Pearl"));
        player.addItem(content.findItemByName("Stone Pot"));
        return player;
    }

    private Console consoleWith(String input) {
        return new Console(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(new ByteArrayOutputStream()));
    }

    private String generateScript(World world) {
        StringBuilder script = new StringBuilder();
        for (World.District district : world.getDistricts()) {
            script.append(district.getHubDirection().toLowerCase().charAt(0)).append('\n');
            for (Room room : district.getPath()) {
                appendRoomInputs(script, room);
                script.append("f\n"); // forward through the district (miniboss "f" returns to hub)
            }
        }
        script.append("up\n");  // throne room (four keys collected)
        script.append("1\n");   // defeat the final boss
        script.append("quit\n");
        return script.toString();
    }

    private void appendRoomInputs(StringBuilder script, Room room) {
        Monster monster = content.findMonsterByRoom(room.getId());
        if (room.isBattle() && monster != null) {
            script.append("1\n"); // boosted player one-shots every monster
            return;
        }
        Puzzle puzzle = content.getPuzzle(room.getId());
        if (puzzle == null) {
            return;
        }
        if (puzzle.isAction()) {
            script.append(puzzle.getTriggers().get(0)).append('\n');
            if (room.getId() == 23) {
                script.append("R\nO\nY\nG\nB\nI\nV\n");
            } else {
                script.append(puzzle.getSolution()).append('\n');
            }
        } else if ("item".equalsIgnoreCase(puzzle.getType())) {
            script.append("use ").append(puzzle.getTriggers().get(0)).append('\n');
        } else { // spell puzzle
            script.append(room.getId() == 31 ? "cast Fireball\n" : "cast " + puzzle.getTriggers().get(0) + "\n");
        }
    }
}
