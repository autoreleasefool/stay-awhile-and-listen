package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.utils.JsonValue;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.input.GameInput;

public abstract class Grabbable extends Entity {

    private boolean dragging = false;

    public Grabbable(Level level, float x, float y, float width, float height) {
        super(level, x, y, width, height);
    }

    public void handleInput(GameInput gameInput) {
        if (!gameInput.isDragConsumed()
                && getBounds().contains(gameInput.getLastFingerXConstrained() + level.getDrawOffset(), gameInput.getLastFingerYConstrained())) {
            gameInput.consumeDrag();
            dragging = true;
        }

        if (dragging) {
            boundingBox.setPosition(gameInput.getLastFingerXConstrained() + level.getDrawOffset(),
                    gameInput.getLastFingerYConstrained());

            if (!gameInput.isFingerDown()) {
                dragging = false;
                snapToNearestCell();
            }
        }
    }

    public boolean isDragging() {
        return dragging;
    }

    public static Grabbable create(Level level, JsonValue json) {
        String type = json.getString("type");
        if (type.equals("plant")) {
            return new Plant(level, json.getInt("x"), json.getInt("y"), json.getInt("size"));
        }

        return null;
    }
}
