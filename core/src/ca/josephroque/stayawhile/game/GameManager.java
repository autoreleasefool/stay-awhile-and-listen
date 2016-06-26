package ca.josephroque.stayawhile.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.game.entity.Player;
import ca.josephroque.stayawhile.game.level.Level;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class GameManager {

    private GameScreen gameScreen;
    private GameInput gameInput;
    private Textures textures;

    int levelNumber = 0;
    Level currentLevel;

    private final Player player;

    public GameManager(GameScreen gameScreen, GameInput input, Textures textures) {
        this.gameScreen = gameScreen;
        this.gameInput = input;
        this.textures = textures;

        player = new Player(null, 0, 0);
    }

    public void tick(GameScreen.GameState gameState, float delta) {
        if (gameState == GameScreen.GameState.GameStarting) {
            boolean reset = false;
            if (currentLevel != null) {
                if (currentLevel.hasLost()) {
                    reset = true;
                    currentLevel.reset();
                }
            }

            if (!reset) {
                updateLevel();
            }

            player.resetLocation();

            gameScreen.setState(GameScreen.GameState.GamePlaying);
        }

        player.handleInput(gameInput);
        player.tick(delta);

        currentLevel.handleInput(gameInput);
        currentLevel.tick(delta);
        if (currentLevel.hasLost()) {
            gameScreen.setState(GameScreen.GameState.GameStarting);
        }
    }

    public void draw(GameScreen.GameState gameState, SpriteBatch spriteBatch) {

        currentLevel.drawBackground(textures, spriteBatch, player.getCenterX());

        switch (gameState) {
            case GamePlaying:
                currentLevel.drawForeground(textures, spriteBatch);
                player.draw(textures, spriteBatch);
                break;
        }
    }

    private void updateLevel() {
        levelNumber++;
        currentLevel = Level.loadLevel(textures, levelNumber);
        player.setLevel(currentLevel);
    }
}
