package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;

import at.gamejam.ktn.game.entites.EnergyBar;
import at.gamejam.ktn.game.entites.NPC;
import at.gamejam.ktn.game.entites.Scoreboard;
import at.gamejam.ktn.game.entities.GameObject;
import at.gmaejam.ktn.mapbuilder.MapParser;
import at.gmaejam.ktn.mapbuilder.Tile;
import at.gmaejam.ktn.mapbuilder.TileData;
import at.gmaejam.ktn.mapbuilder.TileParser;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GeneratedLevel {
	private List<GameObject>	gameObjects;

	private final World			b2world;

	// TODO make absolute
	private String				pathConfig	= "D://tiles.txt";
	private String				pathMap		= "D://map1.png";

	private TileParser			tileParser;
	private MapParser			mapParser;

	int sleepingcount = 0, awakecount = 0;
	
	int npcCount = 2;
	
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
		for (double y = -5.0, stepy = 19; y < 4.9; y += 0.5, stepy--) {
			for (double x = -7.5, step = 0; x < 7.5; x += 0.5, step++) {
				Tile t = new Tile(new Vector2((float) x, (float) y), this.b2world, this.mapParser.getTileData()[(int) step][(int) stepy]);
				if (t.getTiledata().getName().startsWith("table") || t.getTiledata().getName().equalsIgnoreCase("chair.png")) {
					Tile tf = new Tile(new Vector2((float) x, (float) y), this.b2world, new TileData("floor.png"));
					this.gameObjects.add(tf);
				}
				this.gameObjects.add(t);
			}
		}
	}

	public void addNPCs() {
		NPC npc1 = new NPC(new Vector2(1, -5), this.b2world, 50);
		EnergyBar energy1 = new EnergyBar(npc1);
		
		this.gameObjects.add(npc1);
		this.gameObjects.add(energy1);
		
		NPC npc2 = new NPC(new Vector2(1, 0), this.b2world, 50);
		EnergyBar energy2 = new EnergyBar(npc2);
		
		this.gameObjects.add(npc2);
		this.gameObjects.add(energy2);
		
		NPC npc3 = new NPC(new Vector2(-4, -3), this.b2world, 50);
		EnergyBar energy3 = new EnergyBar(npc3);
		
		this.gameObjects.add(npc3);
		this.gameObjects.add(energy3);
		
		NPC npc4 = new NPC(new Vector2(-2, 2), this.b2world, 50);
		EnergyBar energy4 = new EnergyBar(npc4);
		
		this.gameObjects.add(npc4);
		this.gameObjects.add(energy4);
		
//		this.gameObjects.add(new NPC(new Vector2(3, 0), this.b2world, 50));
//		this.gameObjects.add(new NPC(new Vector2(1, -3), this.b2world, 50));
//		this.gameObjects.add(new NPC(new Vector2(0, 2), this.b2world, 50));
//		this.gameObjects.add(new NPC(new Vector2(2, 0), this.b2world, 50));
//		this.gameObjects.add(new NPC(new Vector2(0, -4), this.b2world, 50));
//		this.gameObjects.add(new NPC(new Vector2(-2, 1), this.b2world, 0));
//		this.gameObjects.add(new NPC(new Vector2(3, 1), this.b2world, 0));
//		this.gameObjects.add(new NPC(new Vector2(2, 1), this.b2world, 0));
//		this.gameObjects.add(new NPC(new Vector2(1, 3), this.b2world, 0));
//		this.gameObjects.add(new NPC(new Vector2(4, 2.6f), this.b2world, 100));
//		this.gameObjects.add(new NPC(new Vector2(1, 3f), this.b2world, 100));
//		this.gameObjects.add(new NPC(new Vector2(5, 1), this.b2world, 100));
//		this.gameObjects.add(new NPC(new Vector2(2, 2), this.b2world, 100));
		this.npcCount = 4;
		
	}
	
	public List<GameObject> getGameObjects() {
		return this.gameObjects;
	}

	public void addGameObject(GameObject object) {
		this.gameObjects.add(object);
	}

	public void update(final float deltaTime) {
		sleepingcount = 0;
		awakecount = 0;
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.update(deltaTime);
			if(gameObject instanceof NPC) {
				NPC npc = (NPC) gameObject;
				if(npc.getState() == 1) {
					awakecount++;
				}
				if(npc.getState() == -1) {
					sleepingcount++;
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
		return sleepingcount;
	}
	
	public int getAwakecoutn() {
		return awakecount;
	}
	
	public int getNpcCount() {
		return npcCount;
	}
}
