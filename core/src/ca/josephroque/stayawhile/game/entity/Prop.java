package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public class Prop extends Grabbable {

    private Textures.Props type;

    public Prop(Level level, Textures.Props type, float x, float y) {
        super(level, x, y, GameScreen.BLOCK_SIZE, GameScreen.BLOCK_SIZE);
        this.type = type;
    }

    public Textures.Props getType() {
        return type;
    }

    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getOther(type),
                getX() - level.getDrawOffset(),
                getY(),
                getWidth(),
                getHeight());

        if (isDragging()) {
            Color batchColor = spriteBatch.getColor();
            batchColor.a = 0.3f;
            spriteBatch.setColor(batchColor);
            spriteBatch.draw(textures.getOther(type),
                    DisplayUtils.getSnappedValue(getX() - level.getDrawOffset()),
                    DisplayUtils.getSnappedValue(getY()),
                    getWidth(),
                    getHeight());
            batchColor.a = 1f;
            spriteBatch.setColor(batchColor);
        }
    }
}
