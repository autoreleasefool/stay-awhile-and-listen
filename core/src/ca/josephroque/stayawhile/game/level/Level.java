package ca.josephroque.stayawhile.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import ca.josephroque.stayawhile.game.entity.Entity;
import ca.josephroque.stayawhile.game.entity.Grabbable;
import ca.josephroque.stayawhile.graphics.Textures;
import ca.josephroque.stayawhile.input.GameInput;
import ca.josephroque.stayawhile.screen.GameScreen;
import ca.josephroque.stayawhile.util.DisplayUtils;

public class Level {

    private Cell[][] cells;
    private final List<Grabbable> interactiveObjects;
    private float drawOffset = 0;

    private Level() {
        interactiveObjects = new ArrayList<Grabbable>();
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

    public float getDrawOffset() {
        return drawOffset;
    }

    public void handleInput(GameInput gameInput) {
        for (Entity entity : interactiveObjects) {
            entity.handleInput(gameInput);
        }
    }

    public void tick(float delta) {
        for (Entity entity : interactiveObjects) {
            entity.tick(delta);
        }
    }

    public void drawBackground(Textures textures, SpriteBatch spriteBatch, float playerX) {
        if (playerX >= getWidth() - GameScreen.WORLD_WIDTH / 2) {
            drawOffset = getWidth() - GameScreen.WORLD_WIDTH;
        } else if (playerX > GameScreen.WORLD_WIDTH / 2) {
            drawOffset = playerX - GameScreen.WORLD_WIDTH / 2;
        }

        int off = (int) (drawOffset / GameScreen.BLOCK_SIZE);

        for (int y = 0; y < cells.length; y++) {
            for (int x = off; x - off < GameScreen.HORIZONTAL_BLOCKS + 1 && x < cells[y].length; x++) {
                boolean drawn = false;
                if (cells[y][x] != null) {
                  if (cells[y][x].type == CellType.Floor) {
                      drawn = true;
                      spriteBatch.draw(textures.getColor(Textures.Color.Black),
                              x * GameScreen.BLOCK_SIZE - drawOffset,
                              y * GameScreen.BLOCK_SIZE,
                              GameScreen.BLOCK_SIZE,
                              GameScreen.BLOCK_SIZE);
                  } else if (cells[y][x].type == CellType.LowerFloor) {
                      drawn = true;
                      spriteBatch.draw(textures.getColor(Textures.Color.Black),
                              x * GameScreen.BLOCK_SIZE - drawOffset,
                              y * GameScreen.BLOCK_SIZE,
                              GameScreen.BLOCK_SIZE,
                              GameScreen.BLOCK_SIZE);
                  }
                }

                if (!drawn) {
                    spriteBatch.draw(textures.getColor(Textures.Color.Yellow),
                            x * GameScreen.BLOCK_SIZE - drawOffset,
                            y * GameScreen.BLOCK_SIZE,
                            GameScreen.BLOCK_SIZE,
                            GameScreen.BLOCK_SIZE);
                }
            }
        }
    }

    public void drawForeground(Textures textures, SpriteBatch spriteBatch) {
        for (Entity entity : interactiveObjects) {
            if (DisplayUtils.isOnScreen(this, entity.getX(), entity.getWidth())) {
                entity.draw(textures, spriteBatch);
            }
        }
    }

    public static Level loadLevel(int levelNumber) {
        // Load the level file into a JSON object
        String levelString = String.format("%1$2s", Integer.toString(levelNumber)).replaceAll("\\s", "0");
        FileHandle file = Gdx.files.internal(String.format("levels/%s.json", levelString));
        JsonValue levelJson = new JsonReader().parse(file);

        Level level = new Level();

        String levelType = levelJson.getString("type");
        int levelLength = levelJson.getInt("length");

        level.cells = new Cell[GameScreen.VERTICAL_BLOCKS][levelLength];
        if (levelType.equals("hallway")) {
            for (int i = 0; i < levelLength; i++) {
                level.cells[0][i] = new Cell(i, 0, true, CellType.LowerFloor);
                level.cells[1][i] = new Cell(i, 1, true, CellType.Floor);
            }
        }

        for (JsonValue grabbable : levelJson.get("grabbable").iterator()) {
            level.interactiveObjects.add(Grabbable.create(level, grabbable));
        }

        return level;
    }

    private enum CellType {
        Wall,
        Floor,
        LowerFloor,
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
