package ca.josephroque.stayawhile.game.entity;

import ca.josephroque.stayawhile.input.GameInput;

public abstract class Grabbable extends Entity {

    private boolean dragging = false;

    public Grabbable(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void handleInput(GameInput gameInput) {
        if (!gameInput.isDragConsumed()
                && getBounds().contains(gameInput.getLastFingerXConstrained(), gameInput.getLastFingerYConstrained())) {
            gameInput.consumeDrag();
            dragging = true;
        }

        if (dragging) {
            boundingBox.setPosition(gameInput.getLastFingerXConstrained(),
                    gameInput.getLastFingerYConstrained());

            if (!gameInput.isFingerDown()) {
                dragging = false;
                snapToCell();
            }
        }
    }
}
