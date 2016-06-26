package ca.josephroque.stayawhile.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ca.josephroque.stayawhile.game.entity.Human;

public class Textures {

    private Texture spriteSheet;
    private TextureRegion[] playerSprites;
    private TextureRegion[] oldLady;
    private TextureRegion[] oldMan;
    private TextureRegion[] floor;
    private TextureRegion[] wall;
    private TextureRegion[] plants;
    private TextureRegion[] other;
    private TextureRegion doorway;
    private TextureRegion[] speechBubble;

    public Textures() {
        spriteSheet = new Texture(Gdx.files.internal("spritesheet.png"));

        playerSprites = new TextureRegion[Human.Position.values().length];
        for (int i = 0; i < playerSprites.length; i++) {
            playerSprites[i] = new TextureRegion(spriteSheet, i * 16, 0, 16, 40);
        }

        oldLady = new TextureRegion[2];
        oldMan = new TextureRegion[2];
        floor = new TextureRegion[2];
        wall = new TextureRegion[2];
        plants = new TextureRegion[2];
        for (int i = 0; i < 2; i++) {
            oldLady[i] = new TextureRegion(spriteSheet, i * 16, 48, 16, 32);
            oldMan[i] = new TextureRegion(spriteSheet, i * 16, 80, 16, 32);
            floor[i] = new TextureRegion(spriteSheet, 0, 112 + i * 16, 16, 16);
            wall[i] = new TextureRegion(spriteSheet, 32, 192 + i * 16, 16, 16);
            plants[i] = new TextureRegion(spriteSheet, i * 16, 160 - i * 16, 16, (i + 1) * 16);
        }

        doorway = new TextureRegion(spriteSheet, 0, 176, 32, 48);

        other = new TextureRegion[3];
        for (int i = 0; i < other.length; i++) {
            other[i] = new TextureRegion(spriteSheet, 32 + (i / 2) * 16, 144 + (i % 2) * 16, 16, 16);
        }

        speechBubble = new TextureRegion[6];
        speechBubble[0] = new TextureRegion(spriteSheet, 0, 240, 3, 3);
        speechBubble[1] = new TextureRegion(spriteSheet, 13, 240, 3, 3);
        speechBubble[2] = new TextureRegion(spriteSheet, 0, 253, 3, 3);
        speechBubble[3] = new TextureRegion(spriteSheet, 13, 253, 3, 3);
        speechBubble[4] = new TextureRegion(spriteSheet, 0, 242, 1, 1);
        speechBubble[5] = new TextureRegion(spriteSheet, 1, 242, 1, 1);

    }

    public TextureRegion getPlant(int height) {
        return plants[height - 1];
    }

    public TextureRegion getPlayer(Human.Position positon) {
        return playerSprites[positon.ordinal()];
    }

    public TextureRegion getOldMan(boolean forwards) {
        return (forwards) ? oldMan[0] : oldMan[1];
    }

    public TextureRegion getOldLady(boolean forwards) {
        return (forwards) ? oldLady[0] : oldLady[1];
    }

    public TextureRegion getFloor(boolean carpet) {
        return (carpet) ? floor[0] : floor[1];
    }

    public TextureRegion getWall(boolean baseboard) {
        return (baseboard) ? wall[1] : wall[0];
    }

    public TextureRegion getDoorway() {
        return doorway;
    }

    public TextureRegion getOther(Props prop) {
        return other[prop.ordinal()];
    }

    public void drawSpeechBubble(float textWidth, float textHeight, float x, float y, SpriteBatch spriteBatch) {
        x += 3;
        y -= textHeight + 5;
        int width = (int) (textWidth + 10);
        int height = (int) (textHeight + 10);

        spriteBatch.draw(speechBubble[0], x, y + height - 3);
        spriteBatch.draw(speechBubble[1], x + width - 3, y + height - 3);
        spriteBatch.draw(speechBubble[2], x, y);
        spriteBatch.draw(speechBubble[3], x + width - 3, y);
        spriteBatch.draw(speechBubble[4], x + 3, y, width - 6, 1);
        spriteBatch.draw(speechBubble[4], x, y + 3, 1, height - 6);
        spriteBatch.draw(speechBubble[4], x + 3, y + height - 1, width - 6, 1);
        spriteBatch.draw(speechBubble[4], x + width - 1, y + 3, 1, height - 6);
        spriteBatch.draw(speechBubble[5], x + 1, y + 2, 1, height - 4);
        spriteBatch.draw(speechBubble[5], x + 2, y + 1, 1, height - 2);
        spriteBatch.draw(speechBubble[5], x + 3, y + 1, width - 6, height - 2);
        spriteBatch.draw(speechBubble[5], x + width - 3, y + 1, 1, height - 2);
        spriteBatch.draw(speechBubble[5], x + width - 2, y + 2, 1, height - 4);
    }

    public enum Props {
        Helmet,
        KnittingNeedles,
        PruneJuice,
    }
}
