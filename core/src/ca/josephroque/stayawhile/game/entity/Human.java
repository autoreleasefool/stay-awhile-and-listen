package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public abstract class Human
        extends Entity {

    public static final int WIDTH = GameScreen.BLOCK_SIZE;
    public static final int HEIGHT = GameScreen.BLOCK_SIZE * 2;
    public static final int JUMP_HEIGHT = 7;

    private float speed;
    protected int direction = 1;
    protected int animation = 1;

    private boolean hasTarget = false;
    private int targetX = -1;
    private int targetY = -1;

    public Human(Level level, float x, float y, float speed) {
        super(level, x, y, WIDTH, HEIGHT);
        this.speed = speed;
    }

    public abstract void snapToFace(List<Grabbable> distractions);
    public abstract boolean lost();

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
        hasTarget = true;
    }

    public void removeTarget() {
        hasTarget = false;
    }

    public void reset() {
        super.reset();
        direction = 1;
        animation = 1;
        hasTarget = false;
    }

    @Override
    public void updatePosition(float delta) {
        super.updatePosition(delta);
        if (getXVelocity() < 0) {
            direction = 0;
            if (++animation == 10)
                animation = 0;
        } else if (getXVelocity() > 0) {
            direction = 1;
            if (++animation == 10)
                animation = 0;
        } else {
            animation = 0;
        }
    }

    @Override
    public void tick(float delta) {
        if (hasTarget) {
            if (targetX + GameScreen.BLOCK_SIZE / 2 < getCenterX() - GameScreen.BLOCK_SIZE / 4) {
                setXVelocity(-speed);
            } else if (targetX + GameScreen.BLOCK_SIZE / 2 > getCenterX() + GameScreen.BLOCK_SIZE / 4) {
                setXVelocity(speed);
            } else {
                setXVelocity(0);
                hasTarget = false;
            }
        }

        updatePosition(delta);
    }

    protected void jump() {
        setYVelocity(-Human.JUMP_HEIGHT);
    }

    public static float getAverageSpeed() {
        // Average speed is 1.5 cells per second
        return DisplayUtils.constrain(0,
                GameScreen.WORLD_WIDTH,
                0,
                GameScreen.getScreenWidth(),
                GameScreen.BLOCK_SIZE * 1f);
    }

    public enum Position {
        Left,
        Right,
        LeftWalk1,
        LeftWalk2,
        RightWalk1,
        RightWalk2,
        LeftJump,
        RightJump;

        public static Position parseOrdinal(int ordinal) {
            switch (ordinal) {
                case 0: return Left;
                case 1: return Right;
                case 2: return LeftWalk1;
                case 3: return LeftWalk2;
                case 4: return RightWalk1;
                case 5: return RightWalk2;
                case 6: return LeftJump;
                case 7: return RightJump;
                default: return null;
            }
        }
    }

    public static int[] getSkinColors() {
        int rand = (int) (Math.random() * 3);
        switch (rand) {
            default:
                return new int[]{Color.rgb888(255,199,161), Color.rgb888(255,181,131), Color.rgb888(255,191,142), Color.rgb888(231,172,117)};
            case 1:
                return new int[]{Color.rgb888(167,112,82), Color.rgb888(153,101,74), Color.rgb888(132,78,61), Color.rgb888(107,64,44)};
            case 2:
                return new int[]{Color.rgb888(215,169,139), Color.rgb888(209,156,126), Color.rgb888(190,134,107), Color.rgb888(165,114,85)};
        }
    }
}
