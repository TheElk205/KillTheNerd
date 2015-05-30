package at.game.visuals.tiles;

import java.util.ArrayList;
import java.util.List;

import at.game.utils.Constants;
import at.game.visuals.GameObject;
import at.game.visuals.NPC;
import at.game.visuals.hud.EnergyBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

// TODO: should implement something because to render and to update
public class GeneratedLevel {
	private final List<GameObject>	gameObjects;
	private final World				b2world;
	private String					pathConfig	= Constants.GENERATEDMAP_CONFIG;	// TODO make absolute
	private String					pathMap		= Constants.GENERATEDMAP_MAP;
	private final MapParser			mapParser;
	private int						sleepingcount	= 0, awakecount = 0;
	private int						npcCount		= 2;

	public GeneratedLevel(final World b2World) {
		FileHandle handle = Gdx.files.internal(Constants.GENERATEDMAP_MAP);
		this.pathMap = handle.file().getAbsolutePath();
		handle = Gdx.files.internal(Constants.GENERATEDMAP_CONFIG);
		this.pathConfig = handle.file().getAbsolutePath();
		this.b2world = b2World;
		this.mapParser = new MapParser(this.pathMap, this.pathConfig);
		this.mapParser.parseMap();
		this.gameObjects = new ArrayList<GameObject>();
		this.addTilesToMap();
		this.addNPCs();
	}

	private void addTilesToMap() {
		final TileData[][] tDataArray = this.mapParser.getTileData();
		final int height = this.mapParser.getMapImageHeight();
		final int width = this.mapParser.getMapImageWidth();
		final int heigthLimit = (int) Math.ceil(height / 4f);
		for (float y = -5.0f, stepy = height - 1; y < heigthLimit; y += 0.5, stepy--) {
			final int widthLimit = (int) Math.ceil(width / 4f) + 1; // TODO: +1 is just a bugfix, ansonsten wird nicht ganze map nach rechts
			// gezeichnet
			for (float x = -7.5f, stepX = 0; x < widthLimit; x += 0.5, stepX++) { // 7.5
				final Vector2 position = new Vector2(x, y);
				final TileData tData = tDataArray[(int) stepX][(int) stepy];
				final Tile t = new Tile(position, this.b2world, tData);
				if ((t != null) && (t.getTiledata() != null)) {
					final String tileName = t.getTiledata().getName();

					if (tileName.startsWith("table") || tileName.equalsIgnoreCase("chair.png")) {
						final Tile tf = new Tile(new Vector2(x, y), this.b2world, new TileData("floor.png"));
						this.gameObjects.add(tf);
					}
					/*if (!tileName.startsWith("table") && !tileName.equalsIgnoreCase("chair.png") && tileName.equalsIgnoreCase("black.png")) {
						i++;
						System.out.println(i + " position: " + position + " name: " + tileName);
					}*/
				}
				this.gameObjects.add(t);
			}
		}
	}

	private void addNPCs() {
		final NPC npc1 = new NPC(new Vector2(1, -5));
		final EnergyBar energy1 = new EnergyBar(npc1);
		this.gameObjects.add(npc1);
		this.gameObjects.add(energy1);

		final NPC npc2 = new NPC(new Vector2(1, 0));
		final EnergyBar energy2 = new EnergyBar(npc2);
		this.gameObjects.add(npc2);
		this.gameObjects.add(energy2);

		final NPC npc3 = new NPC(new Vector2(-4, -3));
		final EnergyBar energy3 = new EnergyBar(npc3);
		this.gameObjects.add(npc3);
		this.gameObjects.add(energy3);

		final NPC npc4 = new NPC(new Vector2(-2, 2));
		final EnergyBar energy4 = new EnergyBar(npc4);
		this.gameObjects.add(npc4);
		this.gameObjects.add(energy4);

		final NPC npc5 = new NPC(new Vector2(5, -1));
		final EnergyBar energy5 = new EnergyBar(npc5);
		this.gameObjects.add(npc5);
		this.gameObjects.add(energy5);

		final NPC npc6 = new NPC(new Vector2(9, -3));
		final EnergyBar energy6 = new EnergyBar(npc6);
		this.gameObjects.add(npc6);
		this.gameObjects.add(energy6);

		this.npcCount = 6;

	}

	/**
	 * called in WorldController
	 *
	 * @param object
	 */
	public void addGameObject(final GameObject object) {
		this.gameObjects.add(object);
	}

	/**
	 * called in WorldController
	 *
	 * @param deltaTime
	 */
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
	}

	/**
	 * Called in WorldRenderer - renders the list of this class
	 *
	 * @param batch
	 */
	public void render(final SpriteBatch batch) {
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.render(batch);
		}
	}

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
