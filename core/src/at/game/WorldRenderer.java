package at.game;

import java.util.concurrent.TimeUnit;

import at.game.managers.Assets;
import at.game.utils.Constants;
import at.game.visuals.GameObject;
import at.game.visuals.hud.Scoreboard;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
	private final OrthographicCamera	camera;
	private final OrthographicCamera	cameraGUI;
	private final SpriteBatch			batch;
	private Box2DDebugRenderer			debugRenderer;
	private final BitmapFont			font;
	private TextureRegion				coinTexture;
	private TextureRegion				redbullTexture;
	private TextureRegion				victory;
	private final int					bookPosition			= 40;
	private final int					redBullPosition			= 40;
	private Scoreboard					score;
	private boolean						winMusicAlreadyStarted	= false;

	public WorldRenderer() {
		this.camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		this.camera.position.set(0, 0, 0);
		this.camera.update();
		this.font = new BitmapFont(true); // default 15pt Arial
		this.batch = new SpriteBatch();
		// GUI camera
		this.cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		this.cameraGUI.position.set(0, 0, 0);
		this.cameraGUI.setToOrtho(true); // flip y-axis
		this.cameraGUI.update();

		this.init();
	}

	private void init() {
		this.debugRenderer = new Box2DDebugRenderer();

		this.coinTexture = Assets.getInstance(new AssetManager()).findRegion("book_green_small");
		this.redbullTexture = Assets.getInstance(new AssetManager()).findRegion("single_cup_coffee");
		this.victory = Assets.getInstance(new AssetManager()).findRegion("victory_basic");
		this.victory.flip(false, true);

		this.coinTexture.flip(false, true);
		this.redbullTexture.flip(false, true);
		this.score = new Scoreboard(WorldController.getLevel());
		this.score.setPosition(280, 10);
	}

	private void renderGUI() {
		this.batch.setProjectionMatrix(this.cameraGUI.combined);
		this.batch.begin();
		final String mmss = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(WorldController.getTimeElapsed()) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(WorldController.getTimeElapsed()) % TimeUnit.MINUTES.toSeconds(1));
		this.font.draw(this.batch, mmss, 10, 10);

		if (WorldController.isDebug()) {
			this.font.draw(this.batch, Integer.toString(GameObject.totalObjects.size()), Constants.VIEWPORT_GUI_WIDTH / 2, 40);
			// this.font.draw(this.batch, Integer.toString(RedBull.itemCount), Constants.VIEWPORT_GUI_WIDTH / 2, 60);
			// this.font.draw(this.batch, Integer.toString(Thesis.itemCount), Constants.VIEWPORT_GUI_WIDTH / 2, 80);
		}

		this.font.draw(this.batch, Integer.toString(WorldController.playerSleep.getItemCount()), 35, 40);
		this.font.draw(this.batch, Integer.toString(WorldController.playerWake.getItemCount()), 980, 40);

		this.drawMunition();
		this.drawScoreboard();

		this.batch.end();
	}

	private void drawMunition() {
		int offset = 0;
		for (int i = 0; i < WorldController.playerWake.getItemCount(); i++) {
			this.batch.draw(this.redbullTexture, 1000, this.redBullPosition + offset, 30, 30);
			offset += 30;
		}

		offset = 0;
		for (int i = 0; i < WorldController.playerSleep.getItemCount(); i++) {
			this.batch.draw(this.coinTexture, 10, this.bookPosition + offset, 30, 30);
			offset += 30;
		}
	}

	private void drawScoreboard() {
		this.score.update(0);
		this.score.render(this.batch);

		if (this.score.won() != 0) {
			WorldController.getInputManager().setEnabled(false);
			this.batch.draw(this.victory, 300, 300, 400, 150);
			if (!this.winMusicAlreadyStarted && (Game.winMusic != null)) {
				Game.ingameMusic.dispose();
				Game.winMusic.play();
				this.winMusicAlreadyStarted = true;
			}
		}
	}

	protected void render() {
		WorldController.getCameraHelper().applyTo(this.camera);
		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		WorldController.getLevel().render(this.batch);
		WorldController.playerSleep.render(this.batch);
		WorldController.playerWake.render(this.batch); // TODO make global list.. for all players, for level for bars...
		this.batch.end();
		this.renderGUI();
		if (WorldController.isDebug()) {
			this.debugRenderer.render(WorldController.getB2World(), this.camera.combined);
		}
	}

	protected void resize(final int width, final int height) {
		this.camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width; // calculate aspect ratio
		// this.camera.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height; // calculate aspect ratio
		this.camera.update();
	}

	@Override
	public void dispose() {
		this.batch.dispose();
	}
}
