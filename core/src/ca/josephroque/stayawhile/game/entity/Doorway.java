package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;

import java.util.List;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class Doorway extends Entity {


    private Type type;

    private OldPerson oldPerson;

    private boolean lost;
    private boolean won;
    private boolean continued;

    public void reset() {
        super.reset();
        if (oldPerson != null)
            oldPerson.reset();
        lost = false;
        won = false;
    }

    public Doorway(Level level, Type type, Textures textures, float x, float y) {
        super(level, x, y, GameScreen.BLOCK_SIZE * 2, GameScreen.BLOCK_SIZE * 3);
        this.type = type;

        if (type != Type.Empty) {
            oldPerson = new OldPerson(level, textures, x + GameScreen.BLOCK_SIZE / 2, y);
        }
    }

    public void tick(List<Grabbable> distractions, float delta) {
        if (type == Type.Empty) {
            won = (level.getPlayerX() + GameScreen.BLOCK_SIZE >= getX() + 3 && level.getPlayerX() <= getX() + getWidth() - 3
                    && level.getPlayerY() >= getY() && level.getPlayerY() <= getY() + getHeight());
        } else {
            oldPerson.tick(distractions, type, delta);
            if (oldPerson.hasCaught())
                lose();
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

        if (oldPerson != null) {
            oldPerson.draw(textures, spriteBatch);
        }

        if (type == Type.Empty && won) {
            spriteBatch.draw(textures.getContinueBubble(),
                    getX() - level.getDrawOffset() - GameScreen.BLOCK_SIZE / 2,
                    getY() - GameScreen.BLOCK_SIZE * 1.5f);
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
