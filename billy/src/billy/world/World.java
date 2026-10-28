package billy.world;

import billy.data.ContentStore;
import billy.model.Exit;
import billy.model.Item;
import billy.model.Monster;
import billy.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Builds the playable map: a central hub with four randomized district arms
 * (Earth, Water, Air, Fire) and a locked throne room.
 */
public class World {

    public static class District {
        private final String region;
        private final Room gate;
        private final List<Room> path;      // ordered: gate, interior..., miniboss
        private final Room minibossRoom;
        private final String keyItemName;
        private final String hubDirection;  // N/S/E/W from the hub

        public District(String region, Room gate, List<Room> path, Room minibossRoom,
                        String keyItemName, String hubDirection) {
            this.region = region;
            this.gate = gate;
            this.path = path;
            this.minibossRoom = minibossRoom;
            this.keyItemName = keyItemName;
            this.hubDirection = hubDirection;
        }

        public String getRegion() {
            return region;
        }

        public Room getGate() {
            return gate;
        }

        public List<Room> getPath() {
            return path;
        }

        public Room getMinibossRoom() {
            return minibossRoom;
        }

        public String getKeyItemName() {
            return keyItemName;
        }

        public String getHubDirection() {
            return hubDirection;
        }
    }

    private static final String[][] REGION_CONFIG = {
            {"Earth", "2", "SOUTH"},
            {"Water", "3", "EAST"},
            {"Air", "4", "WEST"},
            {"Fire", "5", "NORTH"},
    };

    private final ContentStore content;
    private final Room hub;
    private final Room throne;
    private final List<District> districts = new ArrayList<>();
    private final Map<Integer, List<Exit>> exits = new LinkedHashMap<>();

    public World(ContentStore content, Random random) {
        this.content = content;
        this.hub = content.getRoom(1);
        this.throne = content.getRoom(34);
        buildDistricts(random);
        buildExits();
    }

    private void buildDistricts(Random random) {
        for (String[] cfg : REGION_CONFIG) {
            String region = cfg[0];
            Room gate = content.getRoom(Integer.parseInt(cfg[1]));
            String direction = cfg[2];

            List<Room> regionRooms = new ArrayList<>();
            Room miniboss = null;
            for (Room room : content.getRooms().values()) {
                if (!region.equalsIgnoreCase(room.getRegion())) {
                    continue;
                }
                regionRooms.add(room);
                Monster monster = content.findMonsterByRoom(room.getId());
                if (monster != null && monster.isMiniboss()) {
                    miniboss = room;
                }
            }

            String keyName = null;
            if (miniboss != null) {
                for (Item item : content.getItems().values()) {
                    if (item.isKey() && item.getRoomId() == miniboss.getId()) {
                        keyName = item.getName();
                    }
                }
            }

            List<Room> interior = new ArrayList<>();
            for (Room room : regionRooms) {
                if (room.getId() != gate.getId() && (miniboss == null || room.getId() != miniboss.getId())) {
                    interior.add(room);
                }
            }
            Collections.shuffle(interior, random);

            List<Room> path = new ArrayList<>();
            path.add(gate);
            path.addAll(interior);
            if (miniboss != null) {
                path.add(miniboss);
            }

            districts.add(new District(region, gate, path, miniboss, keyName, direction));
        }
    }

    private void buildExits() {
        List<Exit> hubExits = new ArrayList<>();
        for (District district : districts) {
            hubExits.add(new Exit(district.getHubDirection(), district.getGate().getId()));
        }
        hubExits.add(new Exit("UP", throne.getId()));
        exits.put(hub.getId(), hubExits);

        addExit(throne.getId(), "BACK", hub.getId());

        for (District district : districts) {
            List<Room> path = district.getPath();
            addExit(path.get(0).getId(), "BACK", hub.getId());
            for (int i = 0; i < path.size(); i++) {
                Room current = path.get(i);
                if (i + 1 < path.size()) {
                    addExit(current.getId(), "FORWARD", path.get(i + 1).getId());
                    addExit(path.get(i + 1).getId(), "BACK", current.getId());
                }
            }
            addExit(path.get(path.size() - 1).getId(), "FORWARD", hub.getId());
        }
    }

    private void addExit(int roomId, String direction, int destination) {
        exits.computeIfAbsent(roomId, k -> new ArrayList<>()).add(new Exit(direction, destination));
    }

    public Room getRoom(int roomId) {
        return content.getRoom(roomId);
    }

    public Room getHub() {
        return hub;
    }

    public Room getThrone() {
        return throne;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public List<Exit> getExits(int roomId) {
        return exits.getOrDefault(roomId, Collections.emptyList());
    }

    /** Returns the exit matching a direction, or null if the room has no such exit. */
    public Exit findExit(int roomId, String direction) {
        for (Exit exit : getExits(roomId)) {
            if (exit.getDirection().equalsIgnoreCase(direction)) {
                return exit;
            }
        }
        return null;
    }

    public String describeExits(int roomId) {
        List<Exit> roomExits = getExits(roomId);
        if (roomExits.isEmpty()) {
            return "There are no visible exits.";
        }
        StringBuilder sb = new StringBuilder("Exits: ");
        for (int i = 0; i < roomExits.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(roomExits.get(i).getDirection());
        }
        return sb.toString();
    }
}
