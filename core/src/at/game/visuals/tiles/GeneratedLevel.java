package at.game.visuals.tiles;

import at.game.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

// TODO: should implement something because to render and to update
/**
 * @author Herkt Kevin
 */
public class GeneratedLevel {
	private String		pathConfig	= Constants.GENERATEDMAP_CONFIG;	// TODO make absolute
	private String		pathMap		= Constants.GENERATEDMAP_MAP;
	private MapParser	mapParser;
	private final int	sleepingcount	= 0, awakecount = 0;
	private final int	npcCount		= 2;

	public GeneratedLevel() {
		FileHandle handle = Gdx.files.internal(Constants.GENERATEDMAP_MAP);
		this.pathMap = handle.file().getAbsolutePath();
		handle = Gdx.files.internal(Constants.GENERATEDMAP_CONFIG);
		this.pathConfig = handle.file().getAbsolutePath();
	}

	public void init() {
		this.mapParser = new MapParser(this.pathMap, this.pathConfig);
		this.mapParser.parseMap();
		this.addTilesToMap();
		this.addNPCs();
	}

	private void addTilesToMap() {
		final TileData[][] tDataArray = this.mapParser.getTileData();
		final int height = this.mapParser.getMapImageHeight();
		final int width = this.mapParser.getMapImageWidth();
		final int heigthLimit = (int) Math.ceil(height / 4f);
		for (float y = -5.0f, stepy = height - 1; y < heigthLimit; y += 0.5, stepy--) { // TODO changed from 0.5 to 2
			final int widthLimit = (int) Math.ceil(width / 4f) + 1; // TODO: +1 is just a bugfix, ansonsten wird nicht ganze map nach rechts
			// gezeichnet
			for (float x = -7.5f, stepX = 0; x < widthLimit; x += 0.5, stepX++) { // 7.5 // TODO changed from 0.5 to 2
				final Vector2 position = new Vector2(x, y);
				final TileData tData = tDataArray[(int) stepX][(int) stepy];
				final Tile t = new Tile(position, tData);
				if ((t != null) && (t.getTiledata() != null)) {
					final String tileName = t.getTiledata().getName();

					if (tileName.startsWith("table") || tileName.equalsIgnoreCase("chair.png")) {
						final Tile tf = new Tile(new Vector2(x, y), new TileData("floor.png"));
						// this.levelObjects.add(tf);
					}
					/*if (!tileName.startsWith("table") && !tileName.equalsIgnoreCase("chair.png") && tileName.equalsIgnoreCase("black.png")) {
						i++;
						System.out.println(i + " position: " + position + " name: " + tileName);
					}*/
				}
				// this.levelObjects.add(t);
			}
		}
	}

	private void addNPCs() {
		/*final NPC npc1 = new NPC(new Vector2(1, -5));
		final EnergyBar energy1 = new EnergyBar(npc1);
		this.gameObjects.add(npc1);
		this.renderObjects.add(energy1);

		final NPC npc2 = new NPC(new Vector2(1, 0));
		final EnergyBar energy2 = new EnergyBar(npc2);
		this.gameObjects.add(npc2);
		this.renderObjects.add(energy2);

		final NPC npc3 = new NPC(new Vector2(-4, -3));
		final EnergyBar energy3 = new EnergyBar(npc3);
		this.gameObjects.add(npc3);
		this.renderObjects.add(energy3);

		final NPC npc4 = new NPC(new Vector2(-2, 2));
		final EnergyBar energy4 = new EnergyBar(npc4);
		this.gameObjects.add(npc4);
		this.renderObjects.add(energy4);

		final NPC npc5 = new NPC(new Vector2(5, -1));
		final EnergyBar energy5 = new EnergyBar(npc5);
		this.gameObjects.add(npc5);
		this.renderObjects.add(energy5);

		final NPC npc6 = new NPC(new Vector2(9, -3));
		final EnergyBar energy6 = new EnergyBar(npc6);
		this.gameObjects.add(npc6);
		this.renderObjects.add(energy6);

		this.npcCount = renderObjects.size();*/
	}

	/*
	 * called in WorldController
	 *
	 * @param deltaTime

	public void update(final float deltaTime) {
		this.sleepingcount = 0;
		this.awakecount = 0;
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.update(deltaTime);
			if (gameObject instanceof NPC) {
				final NPC npc = (NPC) gameObject;
				if (npc.getState() == 1) {
					this.awakecount++;
				}
				if (npc.getState() == -1) {
					this.sleepingcount++;
				}
			}
		}
	}*/

	public int getSleepingCount() {
		return this.sleepingcount;
	}

	public int getAwakeCount() {
		return this.awakecount;
	}

	/**
	 * Called in Scoreboard
	 *
	 * @return npcCount
	 */
	public int getNpcCount() {
		return this.npcCount;
	}
}
