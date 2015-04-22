package at.gamejam.ktn.desktop;

import at.gamejam.ktn.JumpAndRoll;
import at.gamejam.ktn.utils.Constants;

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
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = false;
			settings.duplicatePadding = true;
			TexturePacker.process(settings, Constants.ASSETS_RAW, Constants.ATLAS_FOLDER, Constants.ATLAS_NAME);
		}
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.fullscreen = false;
		config.backgroundFPS = 1;
		config.title = "Nerd Wars - Version_0.1_2015.04.20 - Created by Herkt Kevin, Ferdinand Köppen and Philip Polczer";
		config.height = Constants.VIEWPORT_GUI_HEIGHT;
		config.width = Constants.VIEWPORT_GUI_WIDTH;
		final LwjglApplication lwjglApplication = new LwjglApplication(new JumpAndRoll(), config);
	}
}
