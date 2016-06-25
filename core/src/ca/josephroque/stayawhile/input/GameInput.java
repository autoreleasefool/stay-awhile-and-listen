package ca.josephroque.stayawhile.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;

import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public class GameInput
        implements InputProcessor {

    private static final int MAXIMUM_CLICK_HOLD_THRESHOLD = 300;
    private static final int MAXIMUM_CLICK_MOVE_THRESHOLD = 10;

    private int mLastFingerX;
    private int mLastFingerY;
    private int mFingerDownX;
    private int mFingerDownY;
    private boolean mFingerDown;
    private long mFingerDownTime;
    private boolean mFingerJustReleased;

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

    public void tick() {
        mFingerJustReleased = false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer > 0)
            return false;

        mLastFingerX = screenX;
        mLastFingerY = screenY;
        mFingerDownX = screenX;
        mFingerDownY = screenY;
        mFingerDown = true;

        mFingerDownTime = TimeUtils.millis();
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

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer > 0)
            return false;

        mLastFingerX = screenX;
        mLastFingerY = screenY;

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
