package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;

import at.gamejam.ktn.game.entites.EnergyBar;
import at.gamejam.ktn.game.entites.NPC;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.mapbuilder.MapParser;
import at.gamejam.ktn.mapbuilder.Tile;
import at.gamejam.ktn.mapbuilder.TileData;
import at.gamejam.ktn.mapbuilder.TileParser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GeneratedLevel {
	private List<GameObject>	gameObjects;

	private final World			b2world;

	// TODO make absolute
	private final String		pathConfig	= "D://tiles.txt";
	private final String		pathMap		= "D://map1.png";

	private final TileParser	tileParser;
	private final MapParser		mapParser;

	int							sleepingcount	= 0, awakecount = 0;

	int							npcCount		= 2;

	public GeneratedLevel(final World b2World) {
		this.b2world = b2World;
		this.tileParser = new TileParser(this.pathConfig);
		this.tileParser.readTileData();
		this.mapParser = new MapParser(this.pathMap, this.tileParser);
		this.mapParser.parseMap();
		this.gameObjects = new ArrayList<GameObject>();
		this.addTilesToMap();
		this.addNPCs();
	}

	public void init() {
		this.gameObjects = new ArrayList<GameObject>();

	}

	public void addTilesToMap() {
		// -5--> 5
		// 30 tiles
		// Tile floor = new Tile(new Vector2(0, 0), this.b2world, new TileData("floor.png"));
		// TODO what if map.png is greater than this?
		final TileData[][] tDataArray = this.mapParser.getTileData();
		final int height = this.mapParser.getMapImageHeight();
		final int width = this.mapParser.getMapImageWidth();
		for (float y = -5.0f, stepy = height - 1; y < (height / 4); y += 0.5, stepy--) {
			final float limit = (width / 4f);
			for (float x = -7.5f, stepX = 0; x < limit; x += 0.5, stepX++) { // 7.5
				final Vector2 position = new Vector2(x, y);
				final TileData tData = tDataArray[(int) stepX][(int) stepy];
				final Tile t = new Tile(position, this.b2world, tData);
				if ((t != null) && (t.getTiledata() != null)) {
					if (t.getTiledata().getName().startsWith("table") || t.getTiledata().getName().equalsIgnoreCase("chair.png")) {
						final Tile tf = new Tile(new Vector2(x, y), this.b2world, new TileData("floor.png"));
						this.gameObjects.add(tf);
					}
				}
				this.gameObjects.add(t);
			}
		}
	}

	public void addNPCs() {
		final NPC npc1 = new NPC(new Vector2(1, -5), this.b2world, 50);
		final EnergyBar energy1 = new EnergyBar(npc1);

		this.gameObjects.add(npc1);
		this.gameObjects.add(energy1);

		final NPC npc2 = new NPC(new Vector2(1, 0), this.b2world, 50);
		final EnergyBar energy2 = new EnergyBar(npc2);

		this.gameObjects.add(npc2);
		this.gameObjects.add(energy2);

		final NPC npc3 = new NPC(new Vector2(-4, -3), this.b2world, 50);
		final EnergyBar energy3 = new EnergyBar(npc3);

		this.gameObjects.add(npc3);
		this.gameObjects.add(energy3);

		final NPC npc4 = new NPC(new Vector2(-2, 2), this.b2world, 50);
		final EnergyBar energy4 = new EnergyBar(npc4);

		this.gameObjects.add(npc4);
		this.gameObjects.add(energy4);

		final NPC npc5 = new NPC(new Vector2(5, -1), this.b2world, 50);
		final EnergyBar energy5 = new EnergyBar(npc5);

		this.gameObjects.add(npc5);
		this.gameObjects.add(energy5);

		// this.gameObjects.add(new NPC(new Vector2(3, 0), this.b2world, 50));
		// this.gameObjects.add(new NPC(new Vector2(1, -3), this.b2world, 50));
		// this.gameObjects.add(new NPC(new Vector2(0, 2), this.b2world, 50));
		// this.gameObjects.add(new NPC(new Vector2(2, 0), this.b2world, 50));
		// this.gameObjects.add(new NPC(new Vector2(0, -4), this.b2world, 50));
		// this.gameObjects.add(new NPC(new Vector2(-2, 1), this.b2world, 0));
		// this.gameObjects.add(new NPC(new Vector2(3, 1), this.b2world, 0));
		// this.gameObjects.add(new NPC(new Vector2(2, 1), this.b2world, 0));
		// this.gameObjects.add(new NPC(new Vector2(1, 3), this.b2world, 0));
		// this.gameObjects.add(new NPC(new Vector2(4, 2.6f), this.b2world, 100));
		// this.gameObjects.add(new NPC(new Vector2(1, 3f), this.b2world, 100));
		// this.gameObjects.add(new NPC(new Vector2(5, 1), this.b2world, 100));
		// this.gameObjects.add(new NPC(new Vector2(2, 2), this.b2world, 100));
		this.npcCount = 5;

	}

	public List<GameObject> getGameObjects() {
		return this.gameObjects;
	}

	public void addGameObject(final GameObject object) {
		this.gameObjects.add(object);
	}

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

	public void render(final SpriteBatch batch) {
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.render(batch);
		}
	}

	public int getSleepingcount() {
		return this.sleepingcount;
	}

	public int getAwakecoutn() {
		return this.awakecount;
	}

	public int getNpcCount() {
		return this.npcCount;
	}
}
