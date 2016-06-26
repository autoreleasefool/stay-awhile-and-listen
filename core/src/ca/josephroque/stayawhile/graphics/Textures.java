package ca.josephroque.stayawhile.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

    public enum Props {
        Helmet,
        KnittingNeedles,
        PruneJuice,
    }
}
