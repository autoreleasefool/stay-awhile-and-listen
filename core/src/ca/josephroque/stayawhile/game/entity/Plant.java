package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.graphics.Textures;
import ca.josephroque.stayawhile.screen.GameScreen;

public class Plant extends Grabbable {

    public Plant(float x, float y, int width, int height) {
        super(x, y, width * GameScreen.BLOCK_SIZE, height * GameScreen.BLOCK_SIZE);
    }

    public void tick(float delta) {

    }

    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getColor(Textures.Color.Green),
                getX(),
                getY(),
                getWidth(),
                getHeight());
    }
}
