package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public abstract class Human
        extends Entity {

    public static final int WIDTH = GameScreen.BLOCK_SIZE;
    public static final int HEIGHT = GameScreen.BLOCK_SIZE * 2;

    private float speed;

    private boolean hasTarget = false;
    private int targetX = -1;
    private int targetY = -1;

    public Human(float x, float y, float speed) {
        super(x, y, WIDTH, HEIGHT);
        this.speed = speed;
    }

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
        hasTarget = true;
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

    @Override
    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getColor(Textures.Color.Green),
                getX(),
                getY(),
                getWidth(),
                getHeight());
    }

    public static float getAverageSpeed() {
        // Average speed is 1.5 cells per second
        return DisplayUtils.constrain(0,
                GameScreen.WORLD_WIDTH,
                0,
                GameScreen.getScreenWidth(),
                GameScreen.BLOCK_SIZE * 1f);
    }
}
