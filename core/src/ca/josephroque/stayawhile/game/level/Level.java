package ca.josephroque.stayawhile.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import ca.josephroque.stayawhile.game.entity.Doorway;
import ca.josephroque.stayawhile.game.entity.Entity;
import ca.josephroque.stayawhile.game.entity.Grabbable;
import ca.josephroque.stayawhile.game.entity.Player;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public class Level {

    private Cell[][] cells;
    private final List<Grabbable> interactiveObjects;
    private final List<Doorway> doorways;
    private float drawOffset = 0;
    private float playerX, playerY;
    private boolean lost;
    private boolean won;
    private Type type;

    private Level() {
        interactiveObjects = new ArrayList<Grabbable>();
        doorways = new ArrayList<Doorway>();
    }

    public boolean canEnterCell(int x, int y) {
        return cells[y][x] != null && !cells[y][x].solid;
    }

    public boolean canEnterArea(float x, float y) {
        return canEnterCell((int) (x / GameScreen.BLOCK_SIZE), (int) (y / GameScreen.BLOCK_SIZE));
    }

    public int getWidth() {
        return cells[0].length * GameScreen.BLOCK_SIZE;
    }

    public int getHeight() {
        return cells.length * GameScreen.BLOCK_SIZE;
    }

    public float getPlayerX() {
        return playerX;
    }
    public float getPlayerY() {
        return playerY;
    }

    public void setPlayerLocation(float x, float y) {
        this.playerX = x;
        this.playerY = y;
    }

    public float getDrawOffset() {
        return drawOffset;
    }

    public void reset() {
        lost = false;
        drawOffset = 0;
        for (Entity entity : interactiveObjects) {
            entity.reset();
        }

        for (Entity entity : doorways) {
            entity.reset();
        }
    }

    public void handleInput(GameInput gameInput) {
        for (Entity entity : interactiveObjects) {
            entity.handleInput(gameInput);
        }

        for (Entity entity : doorways) {
            entity.handleInput(gameInput);
        }

        if (type == Type.Room && getPlayerX() > getWidth() - GameScreen.BLOCK_SIZE * 2 && gameInput.clickOccurred()) {
            gameInput.consumeClick();
            win();
        }
    }

    public void tick(float delta) {
        for (Entity entity : interactiveObjects) {
            entity.tick(delta);
        }

        for (Doorway doorway : doorways) {
            if (DisplayUtils.isOnScreen(this, doorway.getX(), doorway.getWidth())) {
                doorway.tick(interactiveObjects, delta);
                if (doorway.hasLost()) {
                    lose();
                } else if (doorway.hasWon()) {
                    win();
                }
            }
        }
    }

    private void lose() {
        lost = true;
    }

    private void win() {
        won = true;
    }

    public boolean hasLost() {
        return lost;
    }

    public boolean hasWon() {
        return won;
    }

    public void drawBackground(Textures textures, SpriteBatch spriteBatch, float playerX) {
        if (playerX >= getWidth() - GameScreen.WORLD_WIDTH / 2) {
            drawOffset = getWidth() - GameScreen.WORLD_WIDTH;
        } else if (playerX > GameScreen.WORLD_WIDTH / 2) {
            drawOffset = playerX - GameScreen.WORLD_WIDTH / 2;
        }

        int off = (int) (drawOffset / GameScreen.BLOCK_SIZE);

        if (type == Type.Hallway) {
            for (int y = 0; y < cells.length; y++) {
                for (int x = off; x - off < GameScreen.HORIZONTAL_BLOCKS + 1 && x < cells[y].length; x++) {
                    boolean drawn = false;
                    if (cells[y][x] != null) {
                        if (cells[y][x].type == CellType.Floor) {
                            drawn = true;
                            spriteBatch.draw(textures.getFloor(true),
                                    x * GameScreen.BLOCK_SIZE - drawOffset,
                                    y * GameScreen.BLOCK_SIZE,
                                    GameScreen.BLOCK_SIZE,
                                    GameScreen.BLOCK_SIZE);
                        } else if (cells[y][x].type == CellType.LowerFloor) {
                            drawn = true;
                            spriteBatch.draw(textures.getFloor(false),
                                    x * GameScreen.BLOCK_SIZE - drawOffset,
                                    y * GameScreen.BLOCK_SIZE,
                                    GameScreen.BLOCK_SIZE,
                                    GameScreen.BLOCK_SIZE);
                        }
                    }

                    if (!drawn) {
                        spriteBatch.draw(textures.getWall(y == 2),
                                x * GameScreen.BLOCK_SIZE - drawOffset,
                                y * GameScreen.BLOCK_SIZE,
                                GameScreen.BLOCK_SIZE,
                                GameScreen.BLOCK_SIZE);
                    }
                }
            }
        } else {
            spriteBatch.draw(textures.getBedroom(), 0, 0);
        }
    }

    public void drawForeground(Textures textures, SpriteBatch spriteBatch) {
        for (Entity entity : doorways) {
            if (DisplayUtils.isOnScreen(this, entity.getX(), entity.getWidth())) {
                entity.draw(textures, spriteBatch);
            }
        }

        for (Entity entity : interactiveObjects) {
            if (DisplayUtils.isOnScreen(this, entity.getX(), entity.getWidth())) {
                entity.draw(textures, spriteBatch);
            }
        }
    }

    public void resetPlayerLocation(Player player) {
        if (type == Type.Hallway) {
            player.setPosition(0, GameScreen.BLOCK_SIZE * 2);
        } else if (type == Type.Room) {
            player.setPosition(GameScreen.BLOCK_SIZE, GameScreen.BLOCK_SIZE / 2);
        }
    }

    public static Level loadLevel(Textures textures, int levelNumber) {
        // Load the level file into a JSON object
        String levelString = String.format("%1$2s", Integer.toString(levelNumber)).replaceAll("\\s", "0");
        FileHandle file = Gdx.files.internal(String.format("levels/%s.json", levelString));
        JsonValue levelJson = new JsonReader().parse(file);

        Level level = new Level();

        String levelType = levelJson.getString("type");

        if (levelType.equals("hallway")) {
            int levelLength = levelJson.getInt("length");
            level.cells = new Cell[GameScreen.VERTICAL_BLOCKS][levelLength];
            for (int i = 0; i < levelLength; i++) {
                level.cells[0][i] = new Cell(i, 0, true, CellType.LowerFloor);
                level.cells[1][i] = new Cell(i, 1, true, CellType.Floor);
            }
            level.type = Type.Hallway;
        } else if (levelType.equals("room")) {
            level.cells = new Cell[GameScreen.VERTICAL_BLOCKS][GameScreen.HORIZONTAL_BLOCKS];
            level.type = Type.Room;
        }

        for (JsonValue grabbable : levelJson.get("grabbable").iterator()) {
            level.interactiveObjects.add(Grabbable.create(level, grabbable));
        }

        if (levelJson.get("doorways") != null) {
            for (JsonValue doorway : levelJson.get("doorways").iterator()) {
                level.doorways.add(Doorway.create(level, textures, doorway));
            }
        }

        return level;
    }

    private enum CellType {
        Wall,
        Floor,
        LowerFloor,
    }

    private enum Type {
        Hallway,
        Room,
    }

    private static class Cell {
        int x;
        int y;
        boolean solid;
        CellType type;

        public Cell(int x, int y, boolean solid, CellType type) {
            this.x = x;
            this.y = y;
            this.solid = solid;
            this.type = type;
        }
    }
}
