package at.game;

import at.game.screens.PlayScreen;

import com.badlogic.gdx.Game;

/**
 * @author Herkt Kevin
 */
public class GameTitle extends Game {
	private final boolean	isLoading	= true;
	// private PlayRenderer worldRenderer;
	/** used to simulate loading screen */
	private LoadRenderer	loadRenderer;
	/** used to simulate loading screen */
	private final int		count		= 0;
	// private Overlap2DLevel overlap2DLevel;
	private final boolean	overlap2D	= false;

	@Override
	public void create() {
		Constants.init();
		this.setScreen(new PlayScreen());
		// this.setScreen(new MenuScreen());
		// this.worldRenderer = new PlayRenderer();
		// this.loadRenderer = new LoadRenderer();
		// TODO overlap2D
		// this.overlap2DLevel = new Overlap2DLevel();

		// Gdx.gl.glClearColor(0, 0, 0, 1); // 135 / 255f, 206 / 255f, 235 / 255f,
	}

	/*
	 * the game loop

	@Override
	public void render() {
		this.count++;
		// Gdx.gl.glClearColor(0, 0, 0, 1); // 135 / 255f, 206 / 255f, 235 / 255f,
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // needs to be before render
		if (this.count == 1) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			this.loadRenderer.render();
		} else
			if (this.isLoading) {
				this.isLoading = !GameTitle.loadMusic();
			} else {// update game world
				if (!this.overlap2D) {
					this.worldRenderer.renderAll();
					WorldController.update(deltaTime);
				} else {
					this.overlap2DLevel.getGameStage().act(deltaTime);
					this.overlap2DLevel.getGameStage().draw();
				}
			}
	}*/

	/*
	 * TODO: should be in level
	 *
	 * @return true if done

	private static boolean loadMusic() {
		if (SoundManager.ingameMusic != null) {
			SoundManager.ingameMusic.play();
			SoundManager.ingameMusic.setVolume(1, 0.5f);
		}
		return true;
	}*/

	/*@Override
	public void resize(final int width, final int height) {
		this.worldRenderer.resize(width, height);
	}

	@Override
	public void dispose() {
		this.worldRenderer.dispose();
	}*/
}
