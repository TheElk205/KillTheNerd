package at.game;

import at.game.utils.Constants;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class LoadRenderer implements Disposable {
	private final OrthographicCamera	cameraGUI;
	private final SpriteBatch			batch;
	private final BitmapFont			font;

	public LoadRenderer() {
		this.font = new BitmapFont(true); // default 15pt Arial
		this.batch = new SpriteBatch();
		// GUI camera
		this.cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		this.cameraGUI.position.set(0, 0, 0);
		this.cameraGUI.setToOrtho(true); // flip y-axis
		this.cameraGUI.update();

	}

	public void render() {
		this.batch.setProjectionMatrix(this.cameraGUI.combined);
		this.batch.begin();
		this.font.draw(this.batch, "Loading ... NerdWars", (Constants.VIEWPORT_GUI_WIDTH / 2) - 70, Constants.VIEWPORT_GUI_HEIGHT / 2);
		this.batch.end();
	}

	public void resize(final int width, final int height) {
		this.cameraGUI.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width; // calculate aspect ratio
		this.cameraGUI.update();
	}

	@Override
	public void dispose() {
		this.batch.dispose();
	}
}
