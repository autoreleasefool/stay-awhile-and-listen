package ca.josephroque.stayawhile.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Textures {

    private Texture[] colors;

    public Textures() {
        Color[] colorValues = Color.values();
        colors = new Texture[colorValues.length];

        for (int i = 0; i < colorValues.length; i++) {
            colors[i] = new Texture(Gdx.files.internal(colorValues[i].name() + ".png"));
        }
    }

    public Texture getColor(Color color) {
        return colors[color.ordinal()];
    }

    public enum Color {
        Black,
        White,
        Red,
        Green,
        Yellow,
    }
}
