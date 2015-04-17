package at.gamejam.ktn.desktop;

import at.gamejam.ktn.JumpAndRoll;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class DesktopLauncher {
	private final static boolean	rebuildAtlas	= false;

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

		new LwjglApplication(new JumpAndRoll(), config);
	}
}
