package at.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import at.game.managers.AssetStore;
import at.game.managers.Assets;
import at.game.maps.AbstractLevel;
import at.game.mechanics.Item;
import at.game.objects.AbstractGameObject;
import at.game.visuals.hud.AbstractHUDElement;
import at.game.visuals.hud.Scoreboard;
import at.game.visuals.hud.WeaponDisplay;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Herkt Kevin
 */
public class PlayRenderer implements Disposable {
	private final OrthographicCamera		camera;
	private final OrthographicCamera		cameraB2D;
	private final OrthographicCamera		cameraGUI;
	private final SpriteBatch				batch;
	private final Box2DDebugRenderer		debugRenderer;
	// private final BitmapFont font;
	// private final TextureRegion redbullTexture;
	private final TextureRegion				victory;
	// private final int redBullPosition = 40;
	private final Scoreboard				score;
	// private final boolean winMusicAlreadyStarted = false;
	private final AbstractLevel				level;
	private final World						b2World;
	private final WorldController			worldController;
	private final List<AbstractHUDElement>	hudElements	= new ArrayList<AbstractHUDElement>();
	private final Sprite					backgroundSprite;

	/**
	 * @param level
	 * @param worldController
	 */
	public PlayRenderer(final AbstractLevel level, final WorldController worldController) {
		this.b2World = worldController.getB2World();
		this.worldController = worldController;
		this.level = level;
		this.level.init();

		// Gdx.graphics.getWidth() gets the width defined in DesktopLauncher.java
		// final float width = Gdx.graphics.getWidth();
		// final float height = Gdx.graphics.getHeight();
		// this.camera = new OrthographicCamera(width, height * (height / width));
		// this.camera = new OrthographicCamera(width, height * (height / width));
		this.debugRenderer = new Box2DDebugRenderer();
		this.victory = Assets.getInstance().findRegion("victory_basic");
		this.victory.flip(false, true);

		this.score = new Scoreboard();
		this.hudElements.add(new WeaponDisplay(this.level.getPlayer1()));
		// camera
		this.camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH_IN_METER, Constants.VIEWPORT_HEIGHT_IN_METER);
		this.camera.position.set(0, 0, 0);
		this.camera.update();

		// GUI camera
		this.cameraGUI = new OrthographicCamera();
		this.cameraGUI.setToOrtho(true, Constants.SCREEN_WIDTH_IN_PIXEL, Constants.SCREEN_HEIGHT_IN_PIXEL); // flip y
		this.cameraGUI.update();

		// b2d camera - collision debugger - Box2D - view is for example 10x17.78 meters
		this.cameraB2D = new OrthographicCamera(Constants.VIEWPORT_WIDTH_IN_METER, Constants.VIEWPORT_HEIGHT_IN_METER);
		// this.b2dCamera.position.set(0, 0, 0);
		// this.b2dCamera.update();

		this.batch = new SpriteBatch();

		this.backgroundSprite = new Sprite(AssetStore.background);
		this.backgroundSprite.setSize(1, 1);
	}

	private void renderWorld() {
		this.batch.setProjectionMatrix(this.camera.combined);
		// this.level.getCameraHelper().setTarget(this.level.getPlayer1().getB2Body());
		this.batch.begin();
		this.level.render(this.batch);
		this.batch.end();

		// System.out.println(this.level.getCameraHelper());
	}

	/**
	 * DONT modify the order of this methods
	 */
	public void render() {
		this.updateCams(); // update must be done first, otherwise lag
		this.renderBackground();
		// this.renderWorld();
		// this.renderObjects();
		this.renderGUI();
		this.batch.end();
		if (Constants.DEBUG) {
			this.debugRenderer.render(this.b2World, this.cameraB2D.combined);
		}
	}

	/**
	 *
	 */
	public void updateCams() {
		this.camera.position.set(this.level.getPlayer1().getPosition().x, this.level.getPlayer1().getPosition().y, 0f);
		this.camera.update();
		if (Constants.DEBUG) {
			// camera should follow the b2Player
			this.cameraB2D.position.set(this.level.getPlayer1().getPosition().x, this.level.getPlayer1().getPosition().y, 0f);
			this.cameraB2D.update();
		}
		// System.out.println("camera: " + this.camera.position + " cameraB2D: " + this.cameraB2D.combined);
	}

	private void renderBackground() {
		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		// a Sprite contains more info than TextureRegion, for example rotation
		// final Sprite backgroundSprite = new Sprite(AssetStore.background, 400, 200, 800 / 100, 400 / 100);

		// backgroundSprite.setPosition(0, 0);
		// backgroundSprite.setScale(backgroundSprite.getWidth(), backgroundSprite.getHeight());
		// backgroundSprite.setSize(backgroundSprite.getWidth() / 100f, backgroundSprite.getHeight() / 100f);
		// this.batch.draw(backgroundSprite, backgroundSprite.getX(), backgroundSprite.getY(), backgroundSprite.getWidth() / Constants.PIXEL_TO_WORLD,
		// backgroundSprite.getHeight() / Constants.PIXEL_TO_WORLD);
		this.batch.draw(AssetStore.background, 0, 0, 20, 10);
		// this.batch.draw(AssetStore.background, 0, 0);
		this.batch.end();
	}

	private void renderGUI() { // TODO problem mit reihenfolge
		this.batch.setProjectionMatrix(this.cameraGUI.combined);
		this.batch.begin();
		for (final AbstractHUDElement element : this.hudElements) {
			element.render(this.batch);
		}
		final String mmss = String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(this.worldController.getTimeElapsed()) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(this.worldController.getTimeElapsed()) % TimeUnit.MINUTES.toSeconds(1));
		Constants.FONT.draw(this.batch, mmss, Constants.SCREEN_WIDTH_IN_PIXEL - Constants.HEIGHT_5P, 10);
		if (Constants.DEBUG) {
			Constants.FONT.draw(this.batch, "total Objects: " + Integer.toString(AbstractGameObject.getTotalObjects().size()),
					Constants.SCREEN_WIDTH_IN_PIXEL / 2, Constants.HEIGHT_5P);
			Constants.FONT.draw(this.batch, "all Items: " + Integer.toString(Item.getAllItems().size()), Constants.SCREEN_WIDTH_IN_PIXEL / 2,
					Constants.SCREEN_HEIGHT_IN_PIXEL * 0.075f);
		}
		this.drawScoreboard();
	}

	private void drawScoreboard() {
		this.score.update(0);
		this.score.render(this.batch);
	}

	/*protected void resize(final int width, final int height) {
		this.b2dCamera.viewportWidth = (Constants.VIEWPORT_HEIGHT_IN_METER / height) * width;
		this.camera.viewportWidth = (Constants.SCREEN_HEIGHT_IN_PIXEL / height) * width;
		// this.camera.viewportHeight = height;
		this.cameraGUI.viewportWidth = (Constants.SCREEN_HEIGHT_IN_PIXEL / height) * width;
		// this.cameraGUI.viewportHeight = height;

		// this.camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width; // calculate aspect ratio
		// this.camera.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height; // calculate aspect ratio
		this.b2dCamera.update();
		this.camera.update();
		this.cameraGUI.update();
	}*/

	@Override
	public void dispose() {
		this.batch.dispose();
		this.b2World.dispose();
	}
}
