package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import ca.josephroque.stayawhile.game.graphics.Textures;

public abstract class Entity {

    private Vector2 mVelocity;

    public Entity() {
        mVelocity = new Vector2();
    }

    public abstract void tick(float delta);

    public abstract void draw(Textures textures, SpriteBatch spriteBatch);

    public abstract float getX();

    public abstract float getY();

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract Shape2D getBounds();

    public abstract void updatePosition(float delta);

    public float getXVelocity() {
        return mVelocity.x;
    }

    public float getYVelocity() {
        return mVelocity.y;
    }

    void setVelocity(Vector2 velocity) {
        mVelocity.set(velocity);
    }
}
