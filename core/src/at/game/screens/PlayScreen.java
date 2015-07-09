package at.game.screens;

import at.game.PlayRenderer;
import at.game.WorldController;
import at.game.managers.InputManager;
import at.game.maps.AbstractLevel;
import at.game.maps.Level1;
import at.game.mechanics.movement.BodyFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen implements Screen {
	private final AbstractLevel	level	= new Level1();
	private WorldController		worldController;
	private PlayRenderer		playRenderer;
	private InputManager		inputManager;

	@Override
	public void show() { // act like a Constructor
		// do not change the order !
		this.worldController = new WorldController(this.level); // MVC
		BodyFactory.init(this.worldController.getB2World());
		this.playRenderer = new PlayRenderer(this.level, this.worldController); // MVC - view gets the controller
		this.level.init();
		this.inputManager = new InputManager(this.level.getPlayer1().getPlayerController());
		Gdx.input.setInputProcessor(this.inputManager);
	}

	@Override
	public void render(final float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // needs to be before render
		this.playRenderer.renderAll();
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
