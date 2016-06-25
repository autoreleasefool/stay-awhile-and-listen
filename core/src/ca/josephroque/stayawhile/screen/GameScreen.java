package ca.josephroque.stayawhile.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ca.josephroque.stayawhile.game.GameManager;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;

public class GameScreen
        implements Screen {

    /** Width of the screen. */
    private static int sScreenWidth;
    /** Height of the screen. */
    private static int sScreenHeight;

    public static final int BLOCK_SIZE = 16;
    public static final int VERTICAL_BLOCKS = 16;
    public static final int HORIZONTAL_BLOCKS = 12;
    public static final int WORLD_WIDTH = BLOCK_SIZE * VERTICAL_BLOCKS;
    public static final int WORLD_HEIGHT = BLOCK_SIZE * HORIZONTAL_BLOCKS;;

    private SpriteBatch mSpriteBatch;
    private OrthographicCamera mPrimaryCamera;
    private Viewport mPrimaryViewport;

    private GameInput mGameInput;
    private GameManager mGameManager;
    private Textures mTextures;

    private GameState mGameState;
    private GameState mPausedState;

    @Override
    public void show() {
        sScreenWidth = Gdx.graphics.getWidth();
        sScreenHeight = Gdx.graphics.getHeight();

        // Setting up the game rendering
        mPrimaryCamera = new OrthographicCamera();
        mPrimaryCamera.translate(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        mPrimaryCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        mPrimaryViewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, mPrimaryCamera);
        mPrimaryViewport.apply();

        // Preparing UI objects
        mSpriteBatch = new SpriteBatch();

        // Creating gesture handler
        mGameInput = new GameInput();
        Gdx.input.setInputProcessor(mGameInput);

        // Setting up the game and menu
        mTextures = new Textures();
        mGameManager = new GameManager(this, mGameInput, mTextures);

        // Displaying the main menu
        setState(GameState.GameStarting);
    }

    @Override
    public void render(float delta) {
        mPrimaryCamera.update();
        tick(delta);

        // Clear the screen to white
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        draw();
    }

    private void tick(float delta) {
        switch (mGameState) {
            case MainMenu:
                break;
            case GameStarting:
            case GamePlaying:
                mGameManager.tick(mGameState, delta);
                break;
            case GamePaused:
            case Ended:
                break;
            default:
                throw new IllegalStateException("invalid game state.");
        }

        // Clear up input
        mGameInput.tick();
    }

    private void draw() {
        mSpriteBatch.setProjectionMatrix(mPrimaryCamera.combined);
        mSpriteBatch.begin();

//        mBackgroundManager.draw(mSpriteBatch);
        mGameManager.draw(mGameState, mSpriteBatch);

        switch (mGameState) {
            case MainMenu:
                break;
            case GameStarting:
            case GamePlaying:
            case GamePaused:
                break;
            case Ended:
                break;
            default:
                throw new IllegalStateException("invalid game state.");
        }

        mSpriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        sScreenWidth = width;
        sScreenHeight = height;
        mPrimaryViewport.update(width, height);
    }

    @Override
    public void pause() {
        if (mGameState == GameState.GamePlaying || mGameState == GameState.GameStarting)
            setState(GameState.GamePaused);
    }

    @Override
    public void resume() {
        // does nothing
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        mSpriteBatch.dispose();
        mSpriteBatch = null;
    }

    public void setState(GameState newState) {
        if (newState == GameState.GamePaused)
            mPausedState = mGameState;
        mGameState = newState;
    }

    /**
     * Gets the current width of the screen.
     *
     * @return width of the screen
     */
    public static int getScreenWidth() {
        return sScreenWidth;
    }

    /**
     * Gets the current height of the screen.
     *
     * @return height of the screen
     */
    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public enum GameState {
        MainMenu,
        GameStarting,
        GamePlaying,
        GamePaused,
        Ended,
    }
}
