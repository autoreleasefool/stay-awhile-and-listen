package ca.josephroque.stayawhile.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.entity.Player;
import ca.josephroque.stayawhile.game.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class GameManager {

    private GameScreen gameScreen;
    private Textures textures;

    int level = 0;

    private final Player player;

    public GameManager(GameScreen gameScreen, Textures textures) {
        this.gameScreen = gameScreen;
        this.textures = textures;
        player = new Player(0, 0);
    }

    public void tick(GameScreen.GameState gameState, GameInput gameInput, float delta) {
        if (gameState == GameScreen.GameState.GameStarting) {
            player.resetLocation();
            gameScreen.setState(GameScreen.GameState.GamePlaying);
            updateLevel();
        }

        player.tick(gameInput, delta);
    }

    public void draw(GameScreen.GameState gameState, SpriteBatch spriteBatch) {

        // Draw the background
        spriteBatch.draw(textures.getColor(Textures.Color.Black),
                0,
                0,
                GameScreen.WORLD_WIDTH,
                GameScreen.BLOCK_SIZE * 2);

        switch (gameState) {
            case GamePlaying:
                player.draw(textures, spriteBatch);
                break;
        }
    }

    private void updateLevel() {
        level++;
    }
}
