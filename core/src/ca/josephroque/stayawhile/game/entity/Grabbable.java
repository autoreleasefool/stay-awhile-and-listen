package ca.josephroque.stayawhile.game.entity;

import com.badlogic.gdx.utils.JsonValue;

import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

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

    public void reset() {
        super.reset();
        dragging = false;
    }

    public static Grabbable create(Level level, JsonValue json) {
        String type = json.getString("type");
        if (type.equals("plant")) {
            return new Plant(level, json.getInt("x") * GameScreen.BLOCK_SIZE, json.getInt("y") * GameScreen.BLOCK_SIZE, json.getInt("size"));
        } else if (type.equals("helmet")) {
            String location = json.getString("location");
            return new Prop(level, Textures.Props.Helmet, getPropX(location), getPropY(location));
        } else if (type.equals("knittingneedles")) {
            String location = json.getString("location");
            return new Prop(level, Textures.Props.KnittingNeedles, getPropX(location), getPropY(location));
        } else if (type.equals("prunejuice")) {
            String location = json.getString("location");
            return new Prop(level, Textures.Props.PruneJuice, getPropX(location), getPropY(location));
        }

        return null;
    }

    private static int getPropX(String location) {
        if (location.equals("dresser")) {
            return 117;
        } else if (location.equals("bed")) {
            return 78;
        } else if (location.equals("floor")) {
            return 190;
        }

        return 0;
    }

    private static int getPropY(String location) {
        if (location.equals("dresser")) {
            return 49;
        } else if (location.equals("bed")) {
            return 32;
        } else if (location.equals("floor")) {
            return 24;
        }

        return 0;
    }
}
