package billy.model;

/**
 * A static room template loaded from content. Room instances are immutable;
 * the mutable "visited" state lives on the {@link Player}.
 */
public class Room {

    private final int id;
    private final String region;
    private final String type;   // normal | special | puzzle | battle
    private final String name;
    private final String description;

    public Room(int id, String region, String type, String name, String description) {
        this.id = id;
        this.region = region;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBattle() {
        return "battle".equalsIgnoreCase(type);
    }

    public boolean isPuzzle() {
        return "puzzle".equalsIgnoreCase(type);
    }

    public boolean isSpecial() {
        return "special".equalsIgnoreCase(type);
    }

    @Override
    public String toString() {
        return name;
    }
}
