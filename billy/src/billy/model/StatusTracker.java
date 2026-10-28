package billy.model;

import java.util.EnumMap;
import java.util.Map;

/**
 * Tracks combat-scoped status ailments for a single combatant. Durations are
 * decremented with {@link #expire()} after the combatant acts (or is skipped),
 * so a status applied with duration N is in effect for N of the combatant's
 * turns. Statuses are never persisted to saves.
 */
public class StatusTracker {

    private final Map<Status, Integer> statuses = new EnumMap<>(Status.class);

    /** Applies (or refreshes) a status to its full duration. */
    public void apply(Status status) {
        statuses.put(status, status.getDuration());
    }

    public boolean has(Status status) {
        return statuses.containsKey(status);
    }

    /** True if the combatant is frozen and must skip its next turn. */
    public boolean isFrozen() {
        return has(Status.FROZEN);
    }

    /** Remaining turns of a status, or 0 when it is not present. */
    public int remaining(Status status) {
        Integer turns = statuses.get(status);
        return turns == null ? 0 : turns;
    }

    /**
     * Multiplier applied to the combatant's outgoing damage from its own
     * debuffs (Dazed and Burn both reduce damage).
     */
    public double damageMultiplier() {
        double multiplier = 1.0;
        if (has(Status.DAZED)) {
            multiplier *= Status.DAZED.getAttackModifier();
        }
        if (has(Status.BURN)) {
            multiplier *= Status.BURN.getAttackModifier();
        }
        return multiplier;
    }

    /** Fraction of max HP lost this turn from damage-over-time ailments. */
    public double dotFraction() {
        double fraction = 0.0;
        if (has(Status.POISON)) {
            fraction += Status.POISON.getDamagePerTurn();
        }
        if (has(Status.BURN)) {
            fraction += Status.BURN.getDamagePerTurn();
        }
        return fraction;
    }

    /** Decrements all durations, expiring statuses that reach zero. */
    public void expire() {
        for (Status status : Status.values()) {
            Integer turns = statuses.get(status);
            if (turns == null) {
                continue;
            }
            if (turns <= 1) {
                statuses.remove(status);
            } else {
                statuses.put(status, turns - 1);
            }
        }
    }
}
