package at.game.utils;

import java.awt.Toolkit;

/**
 * @author Herkt Kevin
 */
public class Constants {
	public static final String	THROW_SOUND					= "../core/assets/mgFast.mp3";																	// "Desktop/KillTheNerd/core/assets/mgFast.mp3";
	public static final String	GRAB_SOUND					= "../core/assets/beam3.mp3";																	// "Desktop/KillTheNerd/core/assets/beam3.mp3";
	public static final String	MUSIC2						= "../core/assets/Scumocide-With A Vengeance_Game.mp3";										// "Desktop/KillTheNerd/core/assets/Scumocide-With A Vengeance_Game.mp3";
	public static final String	VICTORY						= "../core/assets/Scumocide-Calamity Trigger_Game.mp3";										// "Desktop/KillTheNerd/core/assets/Scumocide-Calamity Trigger_Game.mp3";
	public static final String	GENERATEDMAP_MAP			= "../core/assets/map1.png";																	// "Desktop/KillTheNerd/core/assets/map1.png";
	public static final String	GENERATEDMAP_CONFIG			= "../core/assets/map1.txt";																	// "Desktop/KillTheNerd/core/assets/map1.txt";
	public static final String	ASSETS_RAW					= "assets_raw";
	public static final int		START_ITEM_COUNT			= 10;
	// public static final float VIEWPORT_WIDTH = 16.0f;
	// public static final float VIEWPORT_HEIGHT = 16.0f;
	public static final int		REF_HEIGHT_IN_PIXEL			= 768;
	public static final int		REF_WIDTH_IN_PIXEL			= 1366;
	public static final int		SCREEN_HEIGHT_IN_PIXEL		= Toolkit.getDefaultToolkit().getScreenSize().height / 2;										// 480;
	public static final int		SCREEN_WIDTH_IN_PIXEL		= Toolkit.getDefaultToolkit().getScreenSize().width / 2;										// 800;
	public static final float	RATIO						= Constants.SCREEN_WIDTH_IN_PIXEL / Constants.SCREEN_HEIGHT_IN_PIXEL;
	public static final float	VIEWPORT_HEIGHT_IN_METER	= 10;
	public static final float	VIEWPORT_WIDTH_IN_METER		= 10 / ((float) Constants.SCREEN_HEIGHT_IN_PIXEL / (float) Constants.SCREEN_WIDTH_IN_PIXEL);
	public static final String	ATLAS_FOLDER				= "atlas/";
	public static final String	ATLAS_NAME					= "allAssets";
	public static final int		MAX_FAMES					= 60;
	public static final boolean	IS_DOWN_GRAVITY				= true;
	// public static final float TIME_STEP = (float) 1 / (float) Constants.MAX_FAMES;
	public static final float	WIDTH_2P					= Constants.SCREEN_WIDTH_IN_PIXEL * 0.02f;
	public static final float	WIDTH_1P					= Constants.SCREEN_WIDTH_IN_PIXEL * 0.01f;
	public static final float	HEIGHT_2P					= Constants.SCREEN_HEIGHT_IN_PIXEL * 0.02f;
	public static final float	HEIGHT_4P					= Constants.SCREEN_HEIGHT_IN_PIXEL * 0.04f;
	public static final float	HEIGHT_8P					= Constants.SCREEN_HEIGHT_IN_PIXEL * 0.08f;
	public static final float	HEIGHT_5P					= Constants.SCREEN_HEIGHT_IN_PIXEL * 0.05f;
	public static final boolean	DEBUG						= true;
}
