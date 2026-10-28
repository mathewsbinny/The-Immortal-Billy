package billy.model;

/**
 * The elemental affinities used in combat. An elemental attack deals bonus
 * damage against the element it beats and reduced damage against the element
 * that counters it. {@link #PHYSICAL} is neutral against everything.
 *
 * <p>Cycle: FIRE &gt; AIR &gt; EARTH &gt; WATER &gt; FIRE.</p>
 */
public enum Element {

    PHYSICAL,
    EARTH,
    WATER,
    AIR,
    FIRE;

    /**
     * Damage multiplier when an attack of this element strikes a defender.
     * Physical attacks and mirror matches are neutral (1.0); a strong
     * matchup is 1.5 and a weak matchup is 0.5.
     */
    public double multiplierVs(Element defender) {
        if (this == PHYSICAL || defender == PHYSICAL || this == defender) {
            return 1.0;
        }
        return isStrongAgainst(defender) ? 1.5 : 0.5;
    }

    /** True if this element is strong against the given element. */
    public boolean isStrongAgainst(Element other) {
        switch (this) {
            case FIRE: return other == AIR;
            case AIR: return other == EARTH;
            case EARTH: return other == WATER;
            case WATER: return other == FIRE;
            default: return false;
        }
    }

    /** The status ailment this element can inflict, or null for physical. */
    public Status status() {
        switch (this) {
            case EARTH: return Status.POISON;
            case FIRE: return Status.BURN;
            case WATER: return Status.FROZEN;
            case AIR: return Status.DAZED;
            default: return null;
        }
    }
}
