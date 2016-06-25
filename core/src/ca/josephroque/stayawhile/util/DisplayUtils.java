package ca.josephroque.stayawhile.util;

/**
 * Created by josephroque on 2016-06-25.
 */
public class DisplayUtils {

    public static int constrain(int origMin, int origMax, int min, int max, int value) {
        int oldRange = origMax - origMin;
        int newRange = max - min;

        float percentage = (value - min) / (float) oldRange;
        return (int) (percentage * newRange + min);
    }
}
