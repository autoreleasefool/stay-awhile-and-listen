package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class Player extends Human {

    public Player(float x, float y) {
        super(x, y, Human.getAverageSpeed());
    }

    public void resetLocation() {
        boundingBox.setPosition(0, 0);
    }

    public void handleInput(GameInput gameInput) {
        if (gameInput.clickOccurred()) {
            setTarget(gameInput.getLastFingerXCell() * GameScreen.BLOCK_SIZE,
                    gameInput.getLastFingerYConstrained() * GameScreen.BLOCK_SIZE);
        }
    }

    @Override
    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getColor(Textures.Color.Red),
                getX(),
                getY(),
                getWidth(),
                getHeight());
    }
}
