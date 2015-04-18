package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import at.gamejam.ktn.game.entities.GameObject;
import at.gmaejam.ktn.mapbuilder.MapParser;
import at.gmaejam.ktn.mapbuilder.Tile;
import at.gmaejam.ktn.mapbuilder.TileData;
import at.gmaejam.ktn.mapbuilder.TileParser;

public class GeneratedLevel {
	private List<GameObject> gameObjects;
	
	private final World b2world;
	
	private String pathConfig = "D:\\tiles.txt";
	private String pathMap = "D:\\map1.png";
	
	TileParser tileParser;
	MapParser mapParser;
	
	public GeneratedLevel(final World b2World) {
		this.b2world = b2World;
		tileParser = new TileParser(pathConfig);
		this.tileParser.readTileData();
		this.mapParser = new MapParser(pathMap, tileParser);
		mapParser.parseMap();
		this.gameObjects = new ArrayList<GameObject>();
		this.addTilesToMap();
	}
	
	public void init() {
		this.gameObjects = new ArrayList<GameObject>();
		
	}
	
	public void addTilesToMap() {
		//-5--> 5
		//30 tiles
		Tile floor = new Tile(new Vector2(0,0),b2world,new TileData("floor.png"));
		for(double y = -5.0, stepy = 19; y < 4.9; y+=0.5, stepy--){
			for(double x = -7.5, step = 0; x < 7.5; x+=0.5, step++) {
				Tile t = new Tile(new Vector2((float) x,(float)y), this.b2world, mapParser.getTileData()[(int)step][(int)stepy]);
				if(t.getTiledata().getName().startsWith("table") || t.getTiledata().getName().equalsIgnoreCase("chair.png")) {
					Tile tf = new Tile(new Vector2((float) x,(float)y),b2world,new TileData("floor.png"));
					this.gameObjects.add(tf);
				}
				this.gameObjects.add(t);
			}
		}
	}
	
	public List<GameObject> getGameObjects() {
		return this.gameObjects;
	}
	
	public void addGameObject(GameObject object) {
		this.gameObjects.add(object);
	}
	
	public void update(final float deltaTime) {
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.update(deltaTime);
		}
	}
	
	public void render(final SpriteBatch batch) {
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.render(batch);
		}
	}
}

