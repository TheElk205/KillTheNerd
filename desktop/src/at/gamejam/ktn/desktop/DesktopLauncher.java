package at.gamejam.ktn.desktop;

import at.gamejam.ktn.JumpAndRoll;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class DesktopLauncher {
	private final static boolean	rebuildAtlas	= true;

	public static void main(final String[] arg) {
		if (DesktopLauncher.rebuildAtlas) {
			final TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.edgePadding = true;
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = false;
			settings.duplicatePadding = true;
			TexturePacker.process(settings, "assets_raw", "images", "assets");
		}
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// new LwjglApplication(new KTN_Game(), config);

		config.height = Constants.VIEWPORT_GUI_HEIGHT;
		config.width = Constants.VIEWPORT_GUI_WIDTH;
		new LwjglApplication(new JumpAndRoll(), config);
	}
}
