package ca.josephroque.stayawhile.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.LinkedList;

import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;
import ca.josephroque.stayawhile.util.Triplet;

public class GameInput
        implements InputProcessor {

    private static final int MAXIMUM_FINGER_HISTORY = 5;
    private static final int MAXIMUM_CLICK_HOLD_THRESHOLD = 300;
    private static final int MAXIMUM_CLICK_MOVE_THRESHOLD = 10;

    private static final int FINGER_VELOCITY_SCALE = 1000;

    private int mLastFingerX;
    private int mLastFingerY;
    private int mFingerDownX;
    private int mFingerDownY;
    private boolean mFingerDown;
    private long mFingerDownTime;
    private boolean mFingerJustReleased;
    private boolean mFingerDragConsumed;
    private boolean mFingerDragConsumeable;

    /** Past locations of the user's finger. */
    private final LinkedList<Triplet<Integer, Integer, Long>> mFingerHistory
            = new LinkedList<Triplet<Integer, Integer, Long>>();
    /** Used to store the moving velocity of the user's finger. */
    private final Vector2 mFingerDragVelocity = new Vector2();

    public int getLastFingerXCell() {
        return getLastFingerXConstrained() / GameScreen.BLOCK_SIZE;
    }

    public int getLastFingerYCell() {
        return getLastFingerYConstrained() / GameScreen.BLOCK_SIZE;
    }

    public int getLastFingerXConstrained() {
        return DisplayUtils.constrain(0,
                GameScreen.getScreenWidth(),
                0,
                GameScreen.WORLD_WIDTH,
                getLastFingerX());
    }

    public int getLastFingerYConstrained() {
        return DisplayUtils.constrain(0,
                GameScreen.getScreenHeight(),
                0,
                GameScreen.WORLD_HEIGHT,
                getLastFingerY());
    }

    public int getLastFingerX() {
        return mLastFingerX;
    }

    public int getLastFingerY() {
        return GameScreen.getScreenHeight() - mLastFingerY;
    }

    public boolean isFingerDown() {
        return mFingerDown;
    }

    public boolean clickOccurred() {
        return mFingerJustReleased && TimeUtils.timeSinceMillis(mFingerDownTime) < MAXIMUM_CLICK_HOLD_THRESHOLD
                && Math.abs(mLastFingerX - mFingerDownX) < MAXIMUM_CLICK_MOVE_THRESHOLD
                && Math.abs(mLastFingerY - mFingerDownY) < MAXIMUM_CLICK_MOVE_THRESHOLD;
    }

    public Vector2 calculateFingerDragVelocity() {
        if (mFingerHistory.size() == 0) {
            mFingerDragVelocity.set(0, 0);
        } else {
            int totalXDistance = mFingerHistory.getLast().getFirst() - mFingerHistory.getFirst().getFirst();
            int totalYDistance = mFingerHistory.getLast().getSecond() - mFingerHistory.getFirst().getSecond();
            float elapsedTime = mFingerHistory.getLast().getThird() - mFingerHistory.getFirst().getThird();
            mFingerDragVelocity.set(totalXDistance / elapsedTime * FINGER_VELOCITY_SCALE,
                    -totalYDistance / elapsedTime * FINGER_VELOCITY_SCALE);
        }

        return mFingerDragVelocity;
    }

    public void tick() {
        mFingerJustReleased = false;

        if (isFingerDown() && !mFingerDragConsumeable && mFingerDownTime > MAXIMUM_CLICK_HOLD_THRESHOLD) {
            mFingerDragConsumeable = true;
            mFingerDragConsumed = false;
        }
    }

    public void consumeDrag() {
        mFingerDragConsumed = true;
    }

    public boolean isDragConsumed() {
        return mFingerDragConsumed;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer > 0)
            return false;

        mFingerHistory.clear();
        mLastFingerX = screenX;
        mLastFingerY = screenY;
        mFingerDownX = screenX;
        mFingerDownY = screenY;
        mFingerDown = true;
        mFingerDragConsumeable = false;

        mFingerDownTime = TimeUtils.millis();
        mFingerHistory.add(Triplet.create(mLastFingerX, mLastFingerY, mFingerDownTime));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer > 0)
            return false;

        mLastFingerX = screenX;
        mLastFingerY = screenY;
        mFingerDown = false;
        mFingerJustReleased = true;

        mFingerHistory.add(Triplet.create(screenX, screenY, TimeUtils.millis()));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer > 0)
            return false;

        mLastFingerX = screenX;
        mLastFingerY = screenY;

        while (mFingerHistory.size() >= MAXIMUM_FINGER_HISTORY)
            mFingerHistory.removeFirst();
        mFingerHistory.add(Triplet.create(screenX, screenY, TimeUtils.millis()));

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // does nothing
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // does nothing
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        // does nothing
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // does nothing
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // does nothing
        return false;
    }
}
