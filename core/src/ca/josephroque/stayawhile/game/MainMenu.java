package ca.josephroque.stayawhile.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class MainMenu {

    private GameScreen screen;
    private Texture menu;

    public MainMenu(GameScreen gameScreen) {
        this.screen = gameScreen;
        menu = new Texture(Gdx.files.internal("title.png"));
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(menu, 0, 0);
    }

    public void tick(GameInput input, float delta) {
        if (input.clickOccurred()) {
            screen.setState(GameScreen.GameState.GameStarting);
        }
    }

    public void dispose() {
        menu.dispose();
    }
}
