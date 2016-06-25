package ca.josephroque.stayawhile.util;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.screen.GameScreen;

public class DisplayUtils {

    public static int constrain(int origMin, int origMax, int min, int max, int value) {
        int oldRange = origMax - origMin;
        int newRange = max - min;

        float percentage = (value - min) / (float) oldRange;
        return (int) (percentage * newRange + min);
    }

    public static float constrain(float origMin, float origMax, float min, float max, float value) {
        float oldRange = origMax - origMin;
        float newRange = max - min;

        float percentage = (value - min) / oldRange;
        return percentage * newRange + min;
    }

    public static float getSnappedValue(float value) {
        int remainder = ((int) value) % GameScreen.BLOCK_SIZE;
        int offset = 0;

        if (remainder < GameScreen.BLOCK_SIZE / 2) {
            offset -= remainder;
        } else {
            offset += GameScreen.BLOCK_SIZE - remainder;
        }

        return value + offset;
    }

    public static boolean isOnScreen(Level level, float x, float width) {
        return x + width > level.getDrawOffset() && x < level.getDrawOffset() + level.getWidth();
    }
}
