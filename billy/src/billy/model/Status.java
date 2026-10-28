package billy.model;

/**
 * Combat-scoped status ailments. They only last for the duration of a single
 * fight and are never persisted to saves.
 */
public enum Status {

    POISON("Poisoned", 0.10, 1.0, 2, false),
    BURN("Burning", 0.06, 0.8, 3, false),
    FROZEN("Frozen", 0.0, 1.0, 1, true),
    DAZED("Dazed", 0.0, 0.7, 2, false);

    private final String label;
    private final double damagePerTurn; // fraction of max HP lost each turn
    private final double attackModifier; // multiplier applied to outgoing damage
    private final int duration; // turns the ailment lasts
    private final boolean skipsTurn;

    Status(String label, double damagePerTurn, double attackModifier, int duration, boolean skipsTurn) {
        this.label = label;
        this.damagePerTurn = damagePerTurn;
        this.attackModifier = attackModifier;
        this.duration = duration;
        this.skipsTurn = skipsTurn;
    }

    public String getLabel() {
        return label;
    }

    public double getDamagePerTurn() {
        return damagePerTurn;
    }

    public double getAttackModifier() {
        return attackModifier;
    }

    public int getDuration() {
        return duration;
    }

    public boolean skipsTurn() {
        return skipsTurn;
    }
}
