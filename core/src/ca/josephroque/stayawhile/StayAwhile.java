package ca.josephroque.stayawhile;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.josephroque.stayawhile.screen.GameScreen;

public class StayAwhile
        extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new GameScreen());
	}
}
