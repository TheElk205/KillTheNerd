package at.game.screens;

import at.game.managers.MenuInputManager;
import at.game.mechanics.movement.MenuController;
import at.game.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen implements Screen {
	private MenuInputManager	inputManager;
	private Rectangle			button;
	private SpriteBatch			batch;
	private OrthographicCamera	b2dCamera;
	private TextButton			textButton;

	@Override
	public void show() {
		this.inputManager = new MenuInputManager(new MenuController());
		Gdx.input.setInputProcessor(this.inputManager);
		this.button = new Rectangle(0, 0, 10, 10);
		this.textButton = new TextButton("test", new Skin());
		this.batch = new SpriteBatch();
		this.b2dCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH_IN_METER, Constants.VIEWPORT_HEIGHT_IN_METER);
		// this.b2dCamera.position.set(0, 0, 0);
		this.b2dCamera.update();
	}

	@Override
	public void render(final float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // needs to be before render
		this.batch.setProjectionMatrix(this.b2dCamera.combined);
		// this.batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
