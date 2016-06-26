package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class Player extends Human {

    public Player(Level level, float x, float y) {
        super(level, x, y, Human.getAverageSpeed());
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        level.setPlayerLocation(getX(), getY());
    }

    public void handleInput(GameInput gameInput) {
        if (gameInput.clickOccurred()) {
            setTarget(gameInput.getLastFingerXCell() * GameScreen.BLOCK_SIZE + (int) level.getDrawOffset(),
                    gameInput.getLastFingerYCell() * GameScreen.BLOCK_SIZE);
            gameInput.consumeClick();
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

        int position = direction;
        if (getYVelocity() != 0) {
            position = 6 + direction;
        } else {
            if (getXVelocity() != 0) {
                position = 2 + animation / 5 + direction * 2;
            }
        }

        spriteBatch.draw(textures.getPlayer(Human.Position.parseOrdinal(position)),
                drawX,
                getY(),
                getWidth(),
                getHeight());
    }
}
