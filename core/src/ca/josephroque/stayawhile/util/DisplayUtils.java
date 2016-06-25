package ca.josephroque.stayawhile.util;

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
}
