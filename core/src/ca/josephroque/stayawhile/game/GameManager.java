package ca.josephroque.stayawhile.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

import ca.josephroque.stayawhile.game.entity.Entity;
import ca.josephroque.stayawhile.game.entity.Grabbable;
import ca.josephroque.stayawhile.game.entity.Plant;
import ca.josephroque.stayawhile.game.entity.Player;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;

public class GameManager {

    private GameScreen gameScreen;
    private GameInput gameInput;
    private Textures textures;

    int level = 0;

    private final Player player;
    private final List<Grabbable> interactiveObjects;

    public GameManager(GameScreen gameScreen, GameInput input, Textures textures) {
        this.gameScreen = gameScreen;
        this.gameInput = input;
        this.textures = textures;

        player = new Player(0, 0);
        interactiveObjects = new ArrayList<Grabbable>();
    }

    public void tick(GameScreen.GameState gameState, float delta) {
        if (gameState == GameScreen.GameState.GameStarting) {
            player.resetLocation();
            gameScreen.setState(GameScreen.GameState.GamePlaying);
            updateLevel();
        }

        player.handleInput(gameInput);
        player.tick(delta);

        for (Entity entity : interactiveObjects) {
            entity.handleInput(gameInput);
            entity.tick(delta);
        }
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

                if (gameInput.isFingerDown()) {
                    spriteBatch.draw(textures.getColor(Textures.Color.Yellow),
                            gameInput.getLastFingerXCell() * GameScreen.BLOCK_SIZE,
                            gameInput.getLastFingerYCell() * GameScreen.BLOCK_SIZE,
                            GameScreen.BLOCK_SIZE,
                            GameScreen.BLOCK_SIZE);
                }

                for (Entity entity : interactiveObjects) {
                    entity.draw(textures, spriteBatch);
                }
                break;
        }
    }

    private void updateLevel() {
        level++;

        interactiveObjects.clear();

        switch (level) {
            case 1:
                interactiveObjects.add(new Plant(GameScreen.BLOCK_SIZE * 10, GameScreen.BLOCK_SIZE * 2, 1, 2));
        }
    }
}
