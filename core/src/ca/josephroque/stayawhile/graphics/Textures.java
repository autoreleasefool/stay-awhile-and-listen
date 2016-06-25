package ca.josephroque.stayawhile.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ca.josephroque.stayawhile.game.entity.Human;

public class Textures {

    private Texture spriteSheet;
    private TextureRegion[] playerSprites;

    public Textures() {
        spriteSheet = new Texture(Gdx.files.internal("spritesheet.png"));

        playerSprites = new TextureRegion[Human.Position.values().length];
        for (int i = 0; i < playerSprites.length; i++) {
            playerSprites[i] = new TextureRegion(spriteSheet, i * 16, 0, 16, 40);
        }
    }

    public TextureRegion getPlayer(Human.Position positon) {
        return playerSprites[positon.ordinal()];
    }
}
