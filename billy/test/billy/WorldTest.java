package billy;

import billy.data.ContentStore;
import billy.model.Exit;
import billy.model.Room;
import billy.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorldTest {

    private static ContentStore content;

    @BeforeAll
    static void setUp() throws Exception {
        content = ContentStore.load();
    }

    private World newWorld(long seed) {
        return new World(content, new Random(seed));
    }

    @Test
    void hubAndThroneAreCorrect() {
        World world = newWorld(1);
        assertEquals(1, world.getHub().getId());
        assertEquals(34, world.getThrone().getId());
    }

    @Test
    void fourDistrictsWithGateAndMinibossEndpoints() {
        World world = newWorld(1);
        assertEquals(4, world.getDistricts().size());
        for (World.District district : world.getDistricts()) {
            assertEquals(district.getGate().getId(), district.getPath().get(0).getId());
            assertNotNull(district.getMinibossRoom());
            assertEquals(district.getMinibossRoom().getId(),
                    district.getPath().get(district.getPath().size() - 1).getId());
            assertNotNull(district.getKeyItemName());
        }
    }

    @Test
    void hubHasFourCardinalDirectionsAndUp() {
        World world = newWorld(1);
        List<Exit> exits = world.getExits(1);
        assertEquals(5, exits.size());
        Set<String> directions = new HashSet<>();
        for (Exit exit : exits) {
            directions.add(exit.getDirection());
        }
        assertTrue(directions.containsAll(List.of("NORTH", "SOUTH", "EAST", "WEST", "UP")));
    }

    @Test
    void districtChainsAreConsistent() {
        World world = newWorld(1);
        for (World.District district : world.getDistricts()) {
            List<Room> path = district.getPath();
            for (int i = 0; i < path.size(); i++) {
                Room current = path.get(i);
                if (i + 1 < path.size()) {
                    Exit forward = world.findExit(current.getId(), "FORWARD");
                    assertNotNull(forward, "missing FORWARD from " + current.getName());
                    assertEquals(path.get(i + 1).getId(), forward.getDestinationRoomId());
                    Exit back = world.findExit(path.get(i + 1).getId(), "BACK");
                    assertNotNull(back);
                    assertEquals(current.getId(), back.getDestinationRoomId());
                }
            }
            // The last room (miniboss) and the gate both connect back to the hub.
            assertEquals(1, world.findExit(path.get(path.size() - 1).getId(), "FORWARD").getDestinationRoomId());
            assertEquals(1, world.findExit(path.get(0).getId(), "BACK").getDestinationRoomId());
        }
    }

    @Test
    void sameSeedIsDeterministic() {
        World a = newWorld(7);
        World b = newWorld(7);
        for (int i = 0; i < 4; i++) {
            assertEquals(roomIds(a.getDistricts().get(i).getPath()),
                    roomIds(b.getDistricts().get(i).getPath()));
        }
    }

    @Test
    void differentSeedShufflesInterior() {
        World a = newWorld(1);
        World b = newWorld(2);
        boolean differs = false;
        for (int i = 0; i < 4; i++) {
            if (!roomIds(a.getDistricts().get(i).getPath()).equals(roomIds(b.getDistricts().get(i).getPath()))) {
                differs = true;
                break;
            }
        }
        assertTrue(differs, "expected interior rooms to be shuffled between seeds");
    }

    private List<Integer> roomIds(List<Room> rooms) {
        List<Integer> ids = new ArrayList<>();
        for (Room room : rooms) {
            ids.add(room.getId());
        }
        return ids;
    }
}
