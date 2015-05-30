package at.game;

import at.game.utils.Constants;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter {
	protected static Sound	ingameMusic;
	protected static Sound	winMusic;
	private boolean			isLoading	= true;
	private WorldController	worldController;
	private WorldRenderer	worldRenderer;
	/** used to simulate loading screen */
	private LoadRenderer	loadRenderer;
	/** used to simulate loading screen */
	private int				count		= 0;

	@Override
	public void create() {
		this.worldController = new WorldController();
		this.worldRenderer = new WorldRenderer();
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
				this.isLoading = !Game.loadMusic();
			} else {// update game world
				this.worldController.update(Gdx.graphics.getDeltaTime());
				this.worldRenderer.render();
			}
	}

	/**
	 * TODO: should be in level
	 *
	 * @return true if done
	 */
	private static boolean loadMusic() {
		Game.ingameMusic = Gdx.audio.newSound(Gdx.files.internal(Constants.MUSIC2));
		Game.winMusic = Gdx.audio.newSound(Gdx.files.internal(Constants.VICTORY));
		if (Game.ingameMusic != null) {
			Game.ingameMusic.play();
			Game.ingameMusic.setVolume(1, 0.5f);
		}
		return true;
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
