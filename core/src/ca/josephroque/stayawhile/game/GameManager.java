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

            currentLevel.resetPlayerLocation(player);
            gameScreen.setState(GameScreen.GameState.GamePlaying);
        }

        player.tick(delta);

        currentLevel.handleInput(gameInput);
        currentLevel.tick(delta);
        if (currentLevel.hasLost() || currentLevel.hasWon()) {
            gameScreen.setState(GameScreen.GameState.GameStarting);
        }

        player.handleInput(gameInput);
    }

    public void draw(GameScreen.GameState gameState, SpriteBatch spriteBatch) {
        if (currentLevel != null) {
            currentLevel.drawBackground(textures, spriteBatch, player.getCenterX());

            switch (gameState) {
                case GamePlaying:
                    currentLevel.drawForeground(textures, spriteBatch);
                    player.draw(textures, spriteBatch);
                    break;
            }
        }
    }

    private void updateLevel() {
        levelNumber++;
        currentLevel = Level.loadLevel(textures, levelNumber);
        player.setLevel(currentLevel);
    }
}
