package billy;

import billy.model.Status;
import billy.model.StatusTracker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusTrackerTest {

    @Test
    void dazedWeakensTwoAttacksThenExpires() {
        StatusTracker tracker = new StatusTracker();
        tracker.apply(Status.DAZED); // duration 2

        // Turn 1: weakened, then expires to one turn left.
        assertEquals(0.7, tracker.damageMultiplier(), 0.001);
        tracker.expire();
        assertEquals(1, tracker.remaining(Status.DAZED));

        // Turn 2: still weakened, then expires for good.
        assertEquals(0.7, tracker.damageMultiplier(), 0.001);
        tracker.expire();
        assertEquals(0, tracker.remaining(Status.DAZED));
        assertEquals(1.0, tracker.damageMultiplier(), 0.001);
    }

    @Test
    void burnDealsThreeTicksAndWeakensThreeAttacks() {
        StatusTracker tracker = new StatusTracker();
        tracker.apply(Status.BURN); // duration 3

        for (int turn = 0; turn < 3; turn++) {
            assertTrue(tracker.dotFraction() > 0, "burn should tick on turn " + turn);
            assertEquals(0.8, tracker.damageMultiplier(), 0.001);
            tracker.expire();
        }
        assertEquals(0.0, tracker.dotFraction(), 0.001);
        assertEquals(1.0, tracker.damageMultiplier(), 0.001);
        assertEquals(0, tracker.remaining(Status.BURN));
    }

    @Test
    void poisonTicksTwiceThenExpires() {
        StatusTracker tracker = new StatusTracker();
        tracker.apply(Status.POISON); // duration 2

        assertTrue(tracker.dotFraction() > 0);
        tracker.expire();
        assertTrue(tracker.dotFraction() > 0);
        tracker.expire();
        assertEquals(0.0, tracker.dotFraction(), 0.001);
    }

    @Test
    void frozenSkipsOneTurn() {
        StatusTracker tracker = new StatusTracker();
        tracker.apply(Status.FROZEN); // duration 1

        assertTrue(tracker.isFrozen());
        tracker.expire();
        assertFalse(tracker.isFrozen());
        assertEquals(0, tracker.remaining(Status.FROZEN));
    }

    @Test
    void dazedAndBurnStackMultiplicatively() {
        StatusTracker tracker = new StatusTracker();
        tracker.apply(Status.DAZED);
        tracker.apply(Status.BURN);
        assertEquals(0.7 * 0.8, tracker.damageMultiplier(), 0.001);
    }

    @Test
    void applyingRefreshesDuration() {
        StatusTracker tracker = new StatusTracker();
        tracker.apply(Status.POISON);
        tracker.expire();
        assertEquals(1, tracker.remaining(Status.POISON));
        tracker.apply(Status.POISON);
        assertEquals(2, tracker.remaining(Status.POISON));
    }
}
