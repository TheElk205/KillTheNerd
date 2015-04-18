package at.gamejam.ktn.game;

import java.util.concurrent.TimeUnit;

import at.gamejam.ktn.utils.Assets;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Lukas on 11.04.2015.
 */
public class WorldRenderer implements Disposable {
	private OrthographicCamera		camera;
	private OrthographicCamera		cameraGUI;
	private SpriteBatch				batch;
	private final WorldController	worldController;
	private Box2DDebugRenderer		debugRenderer;
	private BitmapFont				font;
	private TextureRegion			coinTexture;

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
		this.coinTexture = Assets.getInstance(new AssetManager()).findRegion("coinGold");
	}

	public void renderGUI(final SpriteBatch batch) {
		batch.setProjectionMatrix(this.cameraGUI.combined);
		batch.begin();
		final String mmss = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(this.worldController.timeElapsed) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(this.worldController.timeElapsed) % TimeUnit.MINUTES.toSeconds(1));
		this.font.draw(batch, mmss, 10, 10);
		batch.draw(this.coinTexture, 10, 40, 20, 20);
		this.font.draw(batch, Integer.toString(this.worldController.coinCount), 35, 40);
		batch.end();
	}

	public void render() {
		this.worldController.cameraHelper.applyTo(this.camera);
		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		this.worldController.getLevel().render(this.batch);
		this.worldController.playerSleep.render(this.batch);
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
