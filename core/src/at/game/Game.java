package at.gamejam.ktn;

import at.gamejam.ktn.game.LoadRenderer;
import at.gamejam.ktn.game.WorldController;
import at.gamejam.ktn.game.WorldRenderer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class JumpAndRoll extends ApplicationAdapter {
	private boolean			isLoading	= true;
	private WorldController	worldController;
	private WorldRenderer	worldRenderer;
	private LoadRenderer	loadRenderer;
	private int				count		= 0;

	@Override
	public void create() {
		this.worldController = new WorldController();
		this.worldRenderer = new WorldRenderer(this.worldController);
		this.loadRenderer = new LoadRenderer();

		Gdx.gl.glClearColor(0, 0, 0, 1); // 135 / 255f, 206 / 255f, 235 / 255f,
	}

	/**
	 * the game loop
	 */
	@Override
	public void render() {
		this.count++;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // needs to be before render
		if (this.count == 1) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			this.loadRenderer.render();
		} else
			if (this.isLoading) {
				this.isLoading = !this.worldController.loadMusic();
			} else {// update game world
				this.worldController.update(Gdx.graphics.getDeltaTime());
				this.worldRenderer.render();
			}
	}

	@Override
	public void resize(final int width, final int height) {
		this.worldRenderer.resize(width, height);
	}

	@Override
	public void dispose() {
		this.worldRenderer.dispose();
	}
}
