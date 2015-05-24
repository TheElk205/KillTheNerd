package at.game.visuals.maps;

import java.util.ArrayList;
import java.util.List;

import at.game.visuals.GameObject;
import at.game.visuals.tiles.BasicBlock;
import at.game.visuals.tiles.BasicBlockType;
import at.game.visuals.tiles.JumpPad;
import at.game.visuals.tiles.Spikes;
import at.gamejam.ktn.game.entites.RedBull;
import at.gamejam.ktn.game.entites.Thesis;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class TopDownLevel {
	private List<GameObject>	gameObjects;
	private List<RedBull>		redBulls;
	private final World			b2World;

	public TopDownLevel(final World b2World) {
		this.b2World = b2World;
		this.init();
	}

	private void init() {
		this.gameObjects = new ArrayList<GameObject>();
		this.redBulls = new ArrayList<RedBull>();

		for (int i = -5; i <= 5; i++) {
			this.gameObjects.add(new BasicBlock(new Vector2(i, 5), BasicBlockType.BlockBorder, this.b2World));
			this.gameObjects.add(new BasicBlock(new Vector2(i, -5), BasicBlockType.BlockBorder, this.b2World));
		}
		for (int i = -5; i <= 5; i++) {
			this.gameObjects.add(new BasicBlock(new Vector2(5, i), BasicBlockType.BlockBorder, this.b2World));
			this.gameObjects.add(new BasicBlock(new Vector2(-5, i), BasicBlockType.BlockBorder, this.b2World));
		}

		/*for (int i = -5; i < 20; i++) {
			if (i == 8) {
				this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlock.BasicBlockType.BlockSlopeStart, this.b2World));
			} else
				if (i == 9) {
					this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlock.BasicBlockType.BlockDirt, this.b2World));
				} else
					if (i == 10) {
						this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlock.BasicBlockType.BlockSlopeStart, this.b2World, true));
					} else {
						this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlock.BasicBlockType.BlockGrass, this.b2World));
					}

			this.gameObjects.add(new BasicBlock(new Vector2(i, -1), BasicBlock.BasicBlockType.BlockDirt, this.b2World));
			this.gameObjects.add(new BasicBlock(new Vector2(i, -2), BasicBlock.BasicBlockType.BlockDirt, this.b2World));
			this.gameObjects.add(new BasicBlock(new Vector2(i, -3), BasicBlock.BasicBlockType.BlockDirt, this.b2World));
		}*/

		this.gameObjects.add(new BasicBlock(new Vector2(9, 1), BasicBlockType.BlockGrass, this.b2World));
		this.gameObjects.add(new BasicBlock(new Vector2(8, 1), BasicBlockType.BlockSlope, this.b2World));
		this.gameObjects.add(new BasicBlock(new Vector2(10, 1), BasicBlockType.BlockSlope, this.b2World, true));
		this.gameObjects.add(new JumpPad(new Vector2(1, 1f), this.b2World));
		this.gameObjects.add(new Spikes(new Vector2(2f, 1f), this.b2World));

		this.gameObjects.add(new RedBull(new Vector2(1, -5), this.b2World, true));
		this.gameObjects.add(new RedBull(new Vector2(3, 0), this.b2World, true));
		this.gameObjects.add(new Thesis(new Vector2(1, -3), this.b2World, true));
		this.gameObjects.add(new RedBull(new Vector2(0, 2), this.b2World, true));
		this.gameObjects.add(new Thesis(new Vector2(2, 0), this.b2World, true));
		this.gameObjects.add(new Thesis(new Vector2(0, -4), this.b2World, true));
	}

	public void render(final SpriteBatch batch) {
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.render(batch);
		}
	}

	public void update(final float deltaTime) {
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.update(deltaTime);
		}
	}

	public void removeRedBull(final RedBull bull) {
		this.gameObjects.remove(bull);
		this.redBulls.remove(bull);
		this.b2World.destroyBody(bull.getB2Body());
	}

	/*public void addItem(final Item item) {
		if (item instanceof RedBull) {
			this.addRedBull((RedBull) item);
		}
	}

	private void addRedBull(final RedBull bull) {
		this.gameObjects.add(bull);
		this.redBulls.add(bull);
	}*/

	public void reset() {
		this.init();
	}

	public List<RedBull> getRedBulls() {
		return this.redBulls;
	}

	public List<GameObject> getGameObjects() {
		return this.gameObjects;
	}

	public void addGameObject(final GameObject gameObject) {
		this.gameObjects.add(gameObject);
	}
	/*public void addGameObject(final GameObject object) {
	this.gameObjects.add(object);
	}

	}*/
	/* List<GameObject>	getGameObjects() {
	return this.gameObjects;
	}*/
}
