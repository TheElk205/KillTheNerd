package at.game.desktop;

import at.game.Constants;
import at.game.GameTitle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * @author Herkt Kevin
 */
public class DesktopLauncher {
	private final static boolean	REBUILD_ALTLAS	= true;

	/**
	 * @param arg
	 *            String array
	 */
	public static void main(final String[] arg) {
		if (DesktopLauncher.REBUILD_ALTLAS) {
			final TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.edgePadding = true;
			settings.maxWidth = 1024; // must be a power or 2, for performance reasons
			settings.maxHeight = 1024; // must be a power or 2, for performance reasons
			settings.debug = false;
			settings.duplicatePadding = true;
			TexturePacker.process(settings, Constants.ASSETS_RAW, Constants.ATLAS_FOLDER, Constants.ATLAS_NAME);
		}
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.fullscreen = false;
		config.useGL30 = false;
		config.backgroundFPS = 10; // TODO objects behave strange, if less than config.foregroundFPS
		config.title = "TheGame - Version_0.3_2015.07.13 - Created by Herkt Kevin, Ferdinand Koeppen and Philip Polczer";
		// size of the screen-window, not of the camera
		config.height = Constants.SCREEN_HEIGHT_IN_PIXEL;
		config.width = Constants.SCREEN_WIDTH_IN_PIXEL;
		config.resizable = false;
		config.foregroundFPS = Constants.MAX_FAMES;
		System.out.println("Starting game: " + config.width + " x " + config.height + " with ViewPort: " + Constants.VIEWPORT_WIDTH_IN_METER + "x"
				+ Constants.VIEWPORT_HEIGHT_IN_METER + " in meter");
		final LwjglApplication lwjglApplication = new LwjglApplication(new GameTitle(), config);
	}
}
