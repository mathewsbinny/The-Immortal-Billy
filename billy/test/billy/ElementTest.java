package billy;

import billy.model.Element;
import billy.model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ElementTest {

    @Test
    void strongMatchupsDealBonusDamage() {
        assertEquals(1.5, Element.FIRE.multiplierVs(Element.AIR), 0.001);
        assertEquals(1.5, Element.AIR.multiplierVs(Element.EARTH), 0.001);
        assertEquals(1.5, Element.EARTH.multiplierVs(Element.WATER), 0.001);
        assertEquals(1.5, Element.WATER.multiplierVs(Element.FIRE), 0.001);
    }

    @Test
    void weakMatchupsDealReducedDamage() {
        assertEquals(0.5, Element.FIRE.multiplierVs(Element.WATER), 0.001);
        assertEquals(0.5, Element.WATER.multiplierVs(Element.EARTH), 0.001);
        assertEquals(0.5, Element.EARTH.multiplierVs(Element.AIR), 0.001);
        assertEquals(0.5, Element.AIR.multiplierVs(Element.FIRE), 0.001);
    }

    @Test
    void physicalIsAlwaysNeutral() {
        assertEquals(1.0, Element.PHYSICAL.multiplierVs(Element.FIRE), 0.001);
        assertEquals(1.0, Element.FIRE.multiplierVs(Element.PHYSICAL), 0.001);
    }

    @Test
    void mirrorMatchIsNeutral() {
        assertEquals(1.0, Element.FIRE.multiplierVs(Element.FIRE), 0.001);
    }

    @Test
    void eachElementMapsToItsStatus() {
        assertEquals(Status.POISON, Element.EARTH.status());
        assertEquals(Status.BURN, Element.FIRE.status());
        assertEquals(Status.FROZEN, Element.WATER.status());
        assertEquals(Status.DAZED, Element.AIR.status());
        assertNull(Element.PHYSICAL.status());
    }
}
