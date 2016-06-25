package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public class Plant extends Grabbable {

    public Plant(Level level, float x, float y, int blockHeight) {
        super(level, x, y, GameScreen.BLOCK_SIZE, blockHeight * GameScreen.BLOCK_SIZE);
    }

    public void tick(float delta) {

    }

    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getColor(Textures.Color.Green),
                getX() - level.getDrawOffset(),
                getY(),
                getWidth(),
                getHeight());

        if (isDragging()) {
            Color batchColor = spriteBatch.getColor();
            batchColor.a = 0.3f;
            spriteBatch.setColor(batchColor);
            spriteBatch.draw(textures.getColor(Textures.Color.Green),
                    DisplayUtils.getSnappedValue(getX() - level.getDrawOffset()),
                    DisplayUtils.getSnappedValue(getY()),
                    getWidth(),
                    getHeight());
            batchColor.a = 1f;
            spriteBatch.setColor(batchColor);
        }
    }
}
