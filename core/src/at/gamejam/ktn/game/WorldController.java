package at.gamejam.ktn.game;

import java.util.Vector;

import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.RedBull;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.utils.CameraHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldController {
	public CameraHelper					cameraHelper;
	public PlayerSleep					player;
	public long							timeElapsed;
	protected int						coinCount	= 0;
	private World						b2World;
	private TopDownLevel				level;
	private final boolean				debug		= true;
	private boolean						reset;

	private final Vector<GameObject>	newObjects	= new Vector<GameObject>();

	public WorldController() {
		this.init();
	}

	public void init() {

		this.cameraHelper = new CameraHelper();
		// this.b2World = new World(new Vector2(0, -9.81f), true);
		this.b2World = new World(new Vector2(0, 0), true); // no gravity
		this.player = new PlayerSleep(new Vector2(0.5f, 1.5f), this);
		this.cameraHelper.setTarget(this.player.getBody());
		// this.level = new Level(this.b2World);
		this.level = new TopDownLevel(this.b2World);
		this.b2World.setContactListener(new MyContactListener(this));
		Gdx.input.setInputProcessor(new InputManager(this.player, this.cameraHelper));
	}

	public void update(final float deltaTime) {
		// Add Items
		if (this.newObjects.size() > 0) {

			for (final GameObject o : this.newObjects) {
				this.level.addGameObject(o);
			}
			this.newObjects.clear();
		}
		this.timeElapsed += deltaTime * 1000;
		this.b2World.step(1 / 60f, 3, 8); // timeStep, velocityIteration, positionIteration
		this.cameraHelper.update(deltaTime);
		// delete items
//		final Vector<Integer> tmp = new Vector<Integer>();
//		for (int i = 0; i < this.level.getRedBulls().size(); i++) {
//			if (this.level.getRedBulls().get(i).isCollected()) {
//				this.b2World.destroyBody(this.level.getRedBulls().get(i).getBody());
//				tmp.add(i);
//			}
//		}
//		for (int i = tmp.size() - 1; i >= 0; i--) {
//			this.level.removeRedBull(this.level.getRedBulls().get(i));
//		}
		for(RedBull b: this.level.getRedBulls()) {
			if(b.isCollected() && !b.destroyed) {
				this.b2World.destroyBody(b.getBody());
				b.destroyed = true;
			}
		}
			newObjects.clear();
		if (this.reset) {
			this.reset();
		}
		this.player.update(deltaTime);
		this.level.update(deltaTime);
		/*if (this.player.getBody().getPosition().y < -3) {
			this.reset = true;
		}*/
		// this.testCoins();
	}

	private void reset() {
		this.reset = false;
		this.player.getBody().setTransform(new Vector2(0.5f, 1.5f), 0);
		this.player.getBody().setLinearVelocity(new Vector2(0, 0));
		this.player.getBody().setAngularVelocity(0);
		this.timeElapsed = 0;
		this.coinCount = 0;
	}

	/*private void testCoins() {
		final Rectangle playerRect = new Rectangle();
		final Rectangle coinRect = new Rectangle();
		playerRect.set(this.player.position.x, this.player.position.y, this.player.dimension.x, this.player.dimension.y);
		for (final RedBull red : this.level.getRedBulls()) {
			if (red.isCollected()) {
				continue;
			}
			coinRect.set(red.position.x, red.position.y, red.dimension.x, red.dimension.y);
			if (!playerRect.overlaps(coinRect)) {
				continue;
			}
			red.grabbed(this.player);
			this.coinCount += 1;
		}
	}*/

	public void addTempGameObject(final GameObject object) {
		this.newObjects.add(object);
		System.out.println("temp new Objects:" + this.newObjects.size());
	}

	public void addRedBull(final RedBull bull) {
		this.addTempGameObject(bull);
		//this.level.addItem(item);
	}

	public boolean isDebug() {
		return this.debug;
	}

	public World getB2World() {
		return this.b2World;
	}

	protected TopDownLevel getLevel() {
		return this.level;
	}

	/*protected void addAbstractItem(final GameObject object) {
		this.level.addGameObject(object);
	}*/
}
