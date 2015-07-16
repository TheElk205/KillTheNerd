package at.game;

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
		this.cameraGUI = new OrthographicCamera(Constants.SCREEN_WIDTH_IN_PIXEL, Constants.SCREEN_HEIGHT_IN_PIXEL);
		this.cameraGUI.position.set(0, 0, 0);
		this.cameraGUI.setToOrtho(true); // flip y-axis
		this.cameraGUI.update();

	}

	public void render() {
		this.batch.setProjectionMatrix(this.cameraGUI.combined);
		this.batch.begin();
		this.font.draw(this.batch, "Loading ... NerdWars", (Constants.SCREEN_WIDTH_IN_PIXEL / 2) - 70, Constants.SCREEN_HEIGHT_IN_PIXEL / 2);
		this.batch.end();
	}

	/*protected void resize(final int width, final int height) {
		this.cameraGUI.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width; // calculate aspect ratio
		this.cameraGUI.update();
	}*/

	@Override
	public void dispose() {
		this.batch.dispose();
	}
}
