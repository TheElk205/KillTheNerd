package at.gamejam.ktn;

import at.gamejam.ktn.game.WorldController;
import at.gamejam.ktn.game.WorldRenderer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class JumpAndRoll extends ApplicationAdapter {

	private WorldController	worldController;
	private WorldRenderer	worldRenderer;

	@Override
	public void create() {
		this.worldController = new WorldController();
		this.worldRenderer = new WorldRenderer(this.worldController);
	}

	/**
	 * the game loop
	 */
	@Override
	public void render() {
		// update game world
		this.worldController.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1); // 135 / 255f, 206 / 255f, 235 / 255f,
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.worldRenderer.render();
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
