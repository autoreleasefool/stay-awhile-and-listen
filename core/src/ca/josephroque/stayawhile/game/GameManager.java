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
            player.resetLocation();
            gameScreen.setState(GameScreen.GameState.GamePlaying);
            updateLevel();
        }

        player.handleInput(gameInput);
        player.tick(delta);

        currentLevel.handleInput(gameInput);
        currentLevel.tick(delta);
    }

    public void draw(GameScreen.GameState gameState, SpriteBatch spriteBatch) {

        currentLevel.drawBackground(textures, spriteBatch, player.getCenterX());

        switch (gameState) {
            case GamePlaying:
                player.draw(textures, spriteBatch);

                if (gameInput.isFingerDown()) {
                    spriteBatch.draw(textures.getColor(Textures.Color.Yellow),
                            gameInput.getLastFingerXCell() * GameScreen.BLOCK_SIZE,
                            gameInput.getLastFingerYCell() * GameScreen.BLOCK_SIZE,
                            GameScreen.BLOCK_SIZE,
                            GameScreen.BLOCK_SIZE);
                }

                currentLevel.drawForeground(textures, spriteBatch);
                break;
        }
    }

    private void updateLevel() {
        levelNumber++;
        currentLevel = Level.loadLevel(levelNumber);
        player.setLevel(currentLevel);
    }
}
