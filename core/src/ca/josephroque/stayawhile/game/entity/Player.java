package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import ca.josephroque.stayawhile.game.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;

public class Player extends Entity {

    public static final float PLAYER_WIDTH = 16;
    public static final float PLAYER_HEIGHT = 32;

    private Rectangle boundingBox;

    public Player(float x, float y) {
        boundingBox = new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void resetLocation() {
        boundingBox.setPosition(0, 0);
    }

    @Override
    public float getX() {
        return boundingBox.getX();
    }

    @Override
    public float getY() {
        return boundingBox.getY();
    }

    @Override
    public float getWidth() {
        return boundingBox.getWidth();
    }

    @Override
    public float getHeight() {
        return boundingBox.getHeight();
    }

    @Override
    public Shape2D getBounds() {
        return boundingBox;
    }

    @Override
    public void updatePosition(float delta) {
        boundingBox.setPosition(getX() + getXVelocity() * delta, getY() + getYVelocity() * delta);
    }

    public void tick(GameInput gameInput, float delta) {

    }

    @Override
    public void tick(float delta) {
        // does nothing
    }

    @Override
    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getColor(Textures.Color.Red), getX(), getY(), getWidth(), getHeight());
    }
}
