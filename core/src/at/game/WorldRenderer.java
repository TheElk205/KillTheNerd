package at.game;

import java.util.concurrent.TimeUnit;

import at.game.gamemechanics.Item;
import at.game.managers.Assets;
import at.game.maps.AbstractLevel;
import at.game.utils.Constants;
import at.game.visuals.GameObject;
import at.game.visuals.hud.Scoreboard;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Herkt Kevin
 */
public class WorldRenderer implements Disposable {
	private final OrthographicCamera	camera;
	private final OrthographicCamera	b2dCamera;
	private final OrthographicCamera	cameraGUI;
	private final SpriteBatch			batch;
	private final Box2DDebugRenderer	debugRenderer;
	private final BitmapFont			font;
	private final TextureRegion			coinTexture;
	private final TextureRegion			redbullTexture;
	private final TextureRegion			victory;
	private final float					bookPosition			= Constants.HEIGHT_8P;
	// private final int redBullPosition = 40;
	private final Scoreboard			score;
	private boolean						winMusicAlreadyStarted	= false;
	private final AbstractLevel			level;
	private final World					world;
	private final WorldController		worldController;

	/**
	 * @param level
	 * @param worldController
	 */
	public WorldRenderer(final AbstractLevel level, final WorldController worldController) {
		this.world = WorldController.getB2World();
		this.worldController = worldController;
		System.out.println("WorldRenderer - Constructor");
		this.level = level;
		// Gdx.graphics.getWidth() gets the width defined in DesktopLauncher.java
		// final float width = Gdx.graphics.getWidth();
		// final float height = Gdx.graphics.getHeight();
		// this.camera = new OrthographicCamera(width, height * (height / width));
		// this.camera = new OrthographicCamera(width, height * (height / width));
		this.debugRenderer = new Box2DDebugRenderer();

		this.coinTexture = Assets.getInstance(new AssetManager()).findRegion("book_green_small");
		this.redbullTexture = Assets.getInstance(new AssetManager()).findRegion("single_cup_coffee");
		this.victory = Assets.getInstance(new AssetManager()).findRegion("victory_basic");
		this.victory.flip(false, true);

		this.coinTexture.flip(false, true);
		this.redbullTexture.flip(false, true);
		this.score = new Scoreboard();
		// camera
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Constants.SCREEN_WIDTH_IN_PIXEL, Constants.SCREEN_HEIGHT_IN_PIXEL);
		/*this.camera.position.set(0, 0, 0);
		this.camera.update();*/

		// GUI camera
		this.cameraGUI = new OrthographicCamera();
		this.cameraGUI.setToOrtho(true, Constants.SCREEN_WIDTH_IN_PIXEL, Constants.SCREEN_HEIGHT_IN_PIXEL); // flip y
		/*this.cameraGUI.position.set(0, 0, 0);
		this.cameraGUI.update();*/

		// b2d camera - collision debugger - Box2D - view is for example 10x17 meters
		this.b2dCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH_IN_METER, Constants.VIEWPORT_HEIGHT_IN_METER);
		// this.b2dCamera.position.set(0, 0, 0);
		this.b2dCamera.update();

		this.font = new BitmapFont(true); // default 15pt Arial
		final float scale = (float) Constants.SCREEN_WIDTH_IN_PIXEL / (float) Constants.REF_WIDTH_IN_PIXEL;
		this.font.getData().setScale(scale, scale);
		this.batch = new SpriteBatch();
	}

	private void drawMunition() {
		this.font.draw(this.batch, "Mun: " + Integer.toString(this.level.getPlayer1().getItemCount()), Constants.WIDTH_1P, Constants.HEIGHT_4P);
		final float sizeOfImage = Constants.WIDTH_2P;
		// this.font.draw(this.batch, Integer.toString(WorldController.playerWake.getItemCount()), 980, 40);
		float offset = 0;
		/*for (int i = 0; i < WorldController.playerWake.getItemCount(); i++) {
			this.batch.draw(this.redbullTexture, 1000, this.redBullPosition + offset, sizeOfImage, sizeOfImage);
			offset += 30;
		}*/

		offset = 0;
		for (int i = 0; i < this.level.getPlayer1().getItemCount(); i++) {
			this.batch.draw(this.coinTexture, Constants.WIDTH_2P, this.bookPosition + offset, sizeOfImage, sizeOfImage);
			offset += sizeOfImage;
		}
	}

	private void drawScoreboard() {
		this.score.update(0);
		this.score.render(this.batch);

		if (this.score.won() != 0) {
			// WorldController.getInputManager().setEnabled(false);
			this.batch.draw(this.victory, 300, 300, 400, 150);
			if (!this.winMusicAlreadyStarted && (GameTitle.winMusic != null)) {
				GameTitle.ingameMusic.dispose();
				GameTitle.winMusic.play();
				this.winMusicAlreadyStarted = true;
			}
		}
	}

	public void renderAll() {
		// this.renderWorld(); // TODO einkommentiern wenn grafiken vorhanden, vorerst nur Box2D rendern
		this.renderGUI();
		// this.level.getCameraHelper().applyTo(this.b2dCamera);
		// System.out.println(this.level.getCameraHelper());
		if (Constants.DEBUG) {
			this.b2dCamera.position.set(this.level.getPlayer1().position.x, this.level.getPlayer1().position.y, 0f); // camera should follow player1
			// System.out.println("WorldRenderer - player1 pos: " + this.level.getPlayer1().position.x + ":" + this.level.getPlayer1().position.y);
			this.b2dCamera.update();
			this.debugRenderer.render(this.world, this.b2dCamera.combined);
		}
	}

	/**
	 *
	 */
	public void renderWorld() {
		this.batch.setProjectionMatrix(this.camera.combined);
		this.level.getCameraHelper().setTarget(this.level.getPlayer1().getB2Body());
		this.batch.begin();
		this.level.render(this.batch);
		this.batch.end();

		System.out.println(this.level.getCameraHelper());
	}

	private void renderGUI() {
		this.batch.setProjectionMatrix(this.cameraGUI.combined);
		this.batch.begin();
		final String mmss = String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(this.worldController.getTimeElapsed()) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(this.worldController.getTimeElapsed()) % TimeUnit.MINUTES.toSeconds(1));
		this.font.draw(this.batch, mmss, Constants.SCREEN_WIDTH_IN_PIXEL - Constants.HEIGHT_5P, 10);

		if (Constants.DEBUG) {
			this.font.draw(this.batch, "total Objects: " + Integer.toString(GameObject.totalObjects.size()), Constants.SCREEN_WIDTH_IN_PIXEL / 2,
					Constants.HEIGHT_5P);
			this.font.draw(this.batch, "all Items: " + Integer.toString(Item.getAllItems().size()), Constants.SCREEN_WIDTH_IN_PIXEL / 2,
					Constants.SCREEN_HEIGHT_IN_PIXEL * 0.075f);
			// this.font.draw(this.batch, Integer.toString(RedBull.itemCount), Constants.VIEWPORT_GUI_WIDTH / 2, 60);
			// this.font.draw(this.batch, Integer.toString(Thesis.itemCount), Constants.VIEWPORT_GUI_WIDTH / 2, 80);
		}
		this.drawMunition();
		this.drawScoreboard();
		this.batch.end();
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
	}
}
