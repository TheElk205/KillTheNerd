package at.game.screens;

import at.game.WorldController;
import at.game.WorldRenderer;
import at.game.gamemechanics.movement.BodyFactory;
import at.game.managers.InputManager;
import at.game.maps.AbstractLevel;
import at.game.maps.Level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen implements Screen {
	private final AbstractLevel		level			= new Level1();
	private final WorldController	worldController	= new WorldController(this.level);
	private final WorldRenderer		worldRenderer	= new WorldRenderer(this.level, this.worldController);
	private InputManager			inputManager;

	@Override
	public void show() { // act like a Constructor
		// do not change the order !
		BodyFactory.init(WorldController.getB2World());
		this.level.init();
		this.inputManager = new InputManager(this.level.getPlayer1().getPlayerController());
		Gdx.input.setInputProcessor(this.inputManager);
	}

	@Override
	public void render(final float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // needs to be before render
		this.worldRenderer.renderAll();
		this.worldController.update(deltaTime);
	}

	@Override
	public void resize(final int width, final int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

}
