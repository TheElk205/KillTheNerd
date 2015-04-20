package at.gamejam.ktn.game;

import java.util.concurrent.TimeUnit;

import at.gamejam.ktn.game.entites.Scoreboard;
import at.gamejam.ktn.utils.Assets;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
	private OrthographicCamera		camera;
	private OrthographicCamera		cameraGUI;
	private SpriteBatch				batch;
	private final WorldController	worldController;
	private Box2DDebugRenderer		debugRenderer;
	private BitmapFont				font;
	private TextureRegion			coinTexture;
	private TextureRegion			redbullTexture;
	private TextureRegion			victory;
	public int						coinPosition			= 40;
	public int						redBullPosition			= 40;

	private Scoreboard				score;
	private boolean					winMusicAlreadyStarted	= false;

	public WorldRenderer(final WorldController worldController) {
		this.worldController = worldController;
		this.init();
	}

	private void init() {
		this.debugRenderer = new Box2DDebugRenderer();
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		this.camera.position.set(0, 0, 0);
		this.camera.update();

		// GUI camera
		this.cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		this.cameraGUI.position.set(0, 0, 0);
		this.cameraGUI.setToOrtho(true); // flip y-axis
		this.cameraGUI.update();

		this.font = new BitmapFont(true); // default 15pt Arial
		this.coinTexture = Assets.getInstance(new AssetManager()).findRegion("book_green_small");
		this.redbullTexture = Assets.getInstance(new AssetManager()).findRegion("single_cup_coffee");
		this.victory = Assets.getInstance(new AssetManager()).findRegion("victory_basic");
		this.victory.flip(false, true);

		this.coinTexture.flip(false, true);
		this.redbullTexture.flip(false, true);

		this.score = new Scoreboard(this.worldController.getLevel());
		this.score.setPosition(280, 10);
	}

	public void renderGUI(final SpriteBatch batch) {
		batch.setProjectionMatrix(this.cameraGUI.combined);
		batch.begin();
		final String mmss = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(this.worldController.timeElapsed) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(this.worldController.timeElapsed) % TimeUnit.MINUTES.toSeconds(1));
		this.font.draw(batch, mmss, 10, 10);

		this.font.draw(batch, Integer.toString(this.worldController.playerSleep.getItemCount()), 35, 40);
		this.font.draw(batch, Integer.toString(this.worldController.playerWake.getItemCount()), 980, 40);

		this.drawMunition();
		this.drawScoreboard();

		batch.end();
	}

	public void drawMunition() {
		int offset = 0;
		for (int i = 0; i < this.worldController.playerWake.getItemCount(); i++) {
			this.batch.draw(this.redbullTexture, 1000, this.redBullPosition + offset, 30, 30);
			offset += 30;
		}

		offset = 0;
		for (int i = 0; i < this.worldController.playerSleep.getItemCount(); i++) {
			this.batch.draw(this.coinTexture, 10, this.coinPosition + offset, 30, 30);
			offset += 30;
		}
	}

	public void drawScoreboard() {
		this.score.update(0);
		this.score.render(this.batch);

		if (this.score.won() != 0) {
			this.worldController.getInputManager().setEnabled(false);
			this.batch.draw(this.victory, 300, 300, 400, 150);
			if (!this.winMusicAlreadyStarted && (this.worldController.winMusic != null)) {
				this.worldController.ingameMusic.dispose();
				this.worldController.winMusic.play();
				this.winMusicAlreadyStarted = true;
			}
		}
	}

	public void render() {
		this.worldController.cameraHelper.applyTo(this.camera);
		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		this.worldController.getLevel().render(this.batch);
		this.worldController.playerSleep.render(this.batch);
		this.worldController.playerWake.render(this.batch); // TODO make global list.. forgot player wake..
		this.worldController.sleepBar.render(this.batch);
		this.worldController.wakeBar.render(this.batch);
		this.batch.end();
		this.renderGUI(this.batch);
		if (this.worldController.isDebug()) {
			this.debugRenderer.render(this.worldController.getB2World(), this.camera.combined);
		}
	}

	public void resize(final int width, final int height) {
		this.camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width; // calculate aspect ratio
		this.camera.update();
	}

	@Override
	public void dispose() {
		this.batch.dispose();
	}
}
