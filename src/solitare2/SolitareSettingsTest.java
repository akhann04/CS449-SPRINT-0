package solitare2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SolitareSettingsTest {

    @Test
    public void testDrawThreeMode() {

        SolitareSettings settings = new SolitareSettings();

        assertEquals(
            "Draw 3",
            settings.getDrawMode(true)
        );
    }

    @Test
    public void testHintsEnabled() {

        SolitareSettings settings = new SolitareSettings();

        assertEquals(
            "Hints On",
            settings.getHintSetting(true)
        );
    }
}