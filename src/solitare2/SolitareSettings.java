package solitare2;

public class SolitareSettings {

    public String getDrawMode(boolean drawThree) {

        if (drawThree) {
            return "Draw 3";
        } else {
            return "Draw 1";
        }
    }

    public String getHintSetting(boolean hintsEnabled) {

        if (hintsEnabled) {
            return "Hints On";
        } else {
            return "Hints Off";
        }
    }
}