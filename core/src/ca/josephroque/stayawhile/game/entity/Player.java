package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public class Player extends Human {

    public Player(Level level, float x, float y) {
        super(level, x, y, Human.getAverageSpeed());
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void resetLocation() {
        boundingBox.setPosition(0, 0);
    }

    public void handleInput(GameInput gameInput) {
        if (gameInput.clickOccurred()) {
            setTarget(gameInput.getLastFingerXCell() * GameScreen.BLOCK_SIZE + (int) level.getDrawOffset(),
                    gameInput.getLastFingerYCell() * GameScreen.BLOCK_SIZE);
        }
    }

    @Override
    public void draw(Textures textures, SpriteBatch spriteBatch) {
        float drawX;
        if (getCenterX() < GameScreen.WORLD_WIDTH / 2) {
            drawX = getCenterX();
        } else if (getCenterX() >= level.getWidth() - GameScreen.WORLD_WIDTH / 2) {
            drawX = getCenterX() - level.getDrawOffset();
        } else {
            drawX = GameScreen.WORLD_WIDTH / 2;
        }

        drawX -= getWidth() / 2;

        spriteBatch.draw(textures.getColor(Textures.Color.Red),
                drawX,
                getY(),
                getWidth(),
                getHeight());
    }
}
