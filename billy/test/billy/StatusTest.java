package billy;

import billy.model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusTest {

    @Test
    void poisonAndBurnDealDamageOverTime() {
        assertTrue(Status.POISON.getDamagePerTurn() > 0);
        assertTrue(Status.BURN.getDamagePerTurn() > 0);
        assertEquals(0.0, Status.FROZEN.getDamagePerTurn(), 0.001);
        assertEquals(0.0, Status.DAZED.getDamagePerTurn(), 0.001);
    }

    @Test
    void onlyFrozenSkipsTurns() {
        assertTrue(Status.FROZEN.skipsTurn());
        assertFalse(Status.POISON.skipsTurn());
        assertFalse(Status.BURN.skipsTurn());
        assertFalse(Status.DAZED.skipsTurn());
    }

    @Test
    void durationsArePositive() {
        for (Status status : Status.values()) {
            assertTrue(status.getDuration() > 0);
        }
    }
}
