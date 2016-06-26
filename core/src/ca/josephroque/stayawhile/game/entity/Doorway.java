package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;

import java.util.List;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.Dialog;

public class Doorway extends Entity {

    private BitmapFont font = new BitmapFont();
    private GlyphLayout fontLayout = new GlyphLayout();
    private Type type;
    private Texture oldPerson;
    private TextureRegion[] oldPersonRegions;
    private List<String> oldPersonDialog;

    private float dialogTimer;
    private int currentDialog;

    private float rotationTimer;
    private boolean rotated;
    private boolean lost;
    private boolean won;
    private boolean continued;
    private boolean blocked;

    public void reset() {
        super.reset();
        dialogTimer = 0;
        currentDialog = 0;
        rotationTimer = 0;
        rotated = false;
        lost = false;
        won = false;
        blocked = false;
    }

    public Doorway(Level level, Type type, Textures textures, float x, float y) {
        super(level, x, y, GameScreen.BLOCK_SIZE * 2, GameScreen.BLOCK_SIZE * 3);
        this.type = type;

        font.setColor(Color.BLACK);
        font.getData().setScale(0.4f, 0.4f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if (type != Type.Empty) {

            oldPersonDialog = Dialog.getNextDialog();
            font.setColor(Color.BLACK);
            font.getData().setScale(0.4f, 0.4f);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            TextureRegion textureRegion;
            boolean man = Math.random() < 0.5;
            if (man) {
                textureRegion = textures.getOldMan(true);
            } else {
                textureRegion = textures.getOldLady(true);
            }

            // TODO: change old person clothes
            // TODO: skin color doesn't change
            int[] skinColors = Human.getSkinColors();
            int[] replaceSkin = {Color.rgb888(255, 199, 161), Color.rgb888(255, 181, 131), Color.rgb888(255, 191, 142), Color.rgb888(231, 172, 117)};

            Texture texture = textureRegion.getTexture();
            if (!texture.getTextureData().isPrepared()) {
                texture.getTextureData().prepare();
            }
            Pixmap temp = texture.getTextureData().consumePixmap();

            Pixmap pixmap = new Pixmap(GameScreen.BLOCK_SIZE * 2, GameScreen.BLOCK_SIZE * 2, Pixmap.Format.RGBA8888);
            for (int xDraw = 0; xDraw < textureRegion.getRegionWidth(); xDraw++) {
                for (int yDraw = 0; yDraw < textureRegion.getRegionHeight(); yDraw++) {
                    int colorInt = temp.getPixel(textureRegion.getRegionX() + xDraw, textureRegion.getRegionY() + yDraw);
                    for (int i = 0; i < replaceSkin.length; i++)
                        if (colorInt == replaceSkin[i])
                            colorInt = skinColors[i];
                    pixmap.drawPixel(xDraw, yDraw, colorInt);
                }
            }

            if (man) {
                textureRegion = textures.getOldMan(false);
            } else {
                textureRegion = textures.getOldLady(false);
            }

            for (int xDraw = 0; xDraw < textureRegion.getRegionWidth(); xDraw++) {
                for (int yDraw = 0; yDraw < textureRegion.getRegionHeight(); yDraw++) {
                    int colorInt = temp.getPixel(textureRegion.getRegionX() + xDraw, textureRegion.getRegionY() + yDraw);
                    for (int i = 0; i < replaceSkin.length; i++)
                        if (colorInt == replaceSkin[i])
                            colorInt = skinColors[i];
                    pixmap.drawPixel(xDraw + GameScreen.BLOCK_SIZE, yDraw, colorInt);
                }
            }

            oldPerson = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
            oldPersonRegions = new TextureRegion[2];
            oldPersonRegions[0] = new TextureRegion(oldPerson, 0, 0, GameScreen.BLOCK_SIZE, GameScreen.BLOCK_SIZE * 2);
            oldPersonRegions[1] = new TextureRegion(oldPerson, GameScreen.BLOCK_SIZE, 0, GameScreen.BLOCK_SIZE, GameScreen.BLOCK_SIZE * 2);
        }
    }

    public void tick(List<Grabbable> distractions, float delta) {
        if (type == Type.Empty) {
            won = (level.getPlayerX() + GameScreen.BLOCK_SIZE >= getX() + 3 && level.getPlayerX() <= getX() + getWidth() - 3
                    && level.getPlayerY() >= getY() && level.getPlayerY() <= getY() + getHeight());
        } else {
            if (type == Type.Rotates) {
                rotationTimer += delta;
                if (rotationTimer > 5) {
                    rotationTimer = 0;
                    rotated = !rotated;
                }
            }

            dialogTimer += delta;
            if (dialogTimer > 4) {
                dialogTimer = 0;
                if (++currentDialog >= oldPersonDialog.size())
                    currentDialog = 0;
            }

            blocked = false;
            for (Grabbable grabbable : distractions) {
                if (grabbable.getX() + grabbable.getWidth() > getX() + GameScreen.BLOCK_SIZE / 2
                        && grabbable.getX() < getX() + GameScreen.BLOCK_SIZE + GameScreen.BLOCK_SIZE / 2
                        && grabbable.getY() + grabbable.getHeight() > getY() + GameScreen.BLOCK_SIZE
                        && grabbable.getY() < getY() + GameScreen.BLOCK_SIZE * 3)
                    blocked = true;
            }

            if (!blocked && !rotated && level.getPlayerX() + GameScreen.BLOCK_SIZE >= getX() + 3 && level.getPlayerX() <= getX() + getWidth() - 3
                    && level.getPlayerY() >= getY() && level.getPlayerY() <= getY() + getHeight()) {
                lose();
            }
        }
    }

    @Override
    public void tick(float delta) {

    }

    private void lose() {
        lost = true;
    }

    public boolean hasLost() {
        return lost;
    }

    public boolean hasWon() {
        return continued;
    }

    @Override
    public void draw(Textures textures, SpriteBatch spriteBatch) {
        spriteBatch.draw(textures.getDoorway(),
                getX() - level.getDrawOffset(),
                getY(),
                getWidth(),
                getHeight());

        if (type != Type.Empty) {
            spriteBatch.draw(oldPersonRegions[(rotated) ? 1 : 0],
                    getX() + GameScreen.BLOCK_SIZE / 2 - level.getDrawOffset(),
                    getY(),
                    oldPersonRegions[0].getRegionWidth(),
                    oldPersonRegions[0].getRegionHeight());

            String dialog = blocked ? "Hey! Where'd you go?" : oldPersonDialog.get(currentDialog);
            fontLayout.setText(font, dialog, Color.BLACK, GameScreen.BLOCK_SIZE * 3, Align.left, true);

            textures.drawSpeechBubble(fontLayout.width,
                    fontLayout.height,
                    getX() + getWidth() - level.getDrawOffset(),
                    getY() + GameScreen.BLOCK_SIZE * 3,
                    spriteBatch);
            font.draw(spriteBatch,
                    fontLayout,
                    getX() + getWidth() - level.getDrawOffset() + 6,
                    getY() + GameScreen.BLOCK_SIZE * 3);
        } else if (won) {
            fontLayout.setText(font, "Continue", Color.BLACK, GameScreen.BLOCK_SIZE * 2, Align.left, true);
            textures.drawSpeechBubble(GameScreen.BLOCK_SIZE * 2,
                    fontLayout.height,
                    getX() - level.getDrawOffset() - GameScreen.BLOCK_SIZE / 2,
                    getY() - GameScreen.BLOCK_SIZE,
                    spriteBatch);
            font.draw(spriteBatch,
                    fontLayout,
                    getX() - level.getDrawOffset(),
                    getY() - GameScreen.BLOCK_SIZE);
        }
    }

    @Override
    public void handleInput(GameInput gameInput) {
        if (won && gameInput.clickOccurred()
                && gameInput.getLastFingerXConstrained() >= getX() - level.getDrawOffset() - GameScreen.BLOCK_SIZE / 2
                && gameInput.getLastFingerXConstrained() <= getX() - level.getDrawOffset() - GameScreen.BLOCK_SIZE / 2 + GameScreen.BLOCK_SIZE * 2
                && gameInput.getLastFingerYConstrained() >= getY() - GameScreen.BLOCK_SIZE * 2
                && gameInput.getLastFingerYConstrained() <= getY() + getHeight()) {
            continued = true;
            gameInput.consumeClick();
        }
    }

    public static Doorway create(Level level, Textures textures, JsonValue json) {
        String strType = json.getString("type");
        Type type;
        if (strType.equals("still")) {
            type = Type.Still;
        } else if (strType.equals("rotates")) {
            type = Type.Rotates;
        } else if (strType.equals("empty")) {
            type = Type.Empty;
        } else {
            return null;
        }

        return new Doorway(level, type, textures, json.getInt("x") * GameScreen.BLOCK_SIZE, GameScreen.BLOCK_SIZE * 2);
    }

    public enum Type {
        Still,
        Rotates,
        Empty,
    }
}
