package at.game.visuals.maps;

import java.util.ArrayList;
import java.util.List;

import at.game.visuals.Coin;
import at.game.visuals.DecoBlock;
import at.game.visuals.GameObject;
import at.game.visuals.tiles.BasicBlock;
import at.game.visuals.tiles.BasicBlockType;
import at.game.visuals.tiles.DecoBlockType;
import at.game.visuals.tiles.JumpPad;
import at.game.visuals.tiles.Spikes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Level {
	private List<GameObject>	gameObjects;
	private List<Coin>			coins;
	private final World			b2World;

	public Level(final World b2World) {
		this.b2World = b2World;
		this.init();
	}

	private void init() {
		this.gameObjects = new ArrayList<GameObject>();
		this.coins = new ArrayList<Coin>();

		this.gameObjects.add(new DecoBlock(new Vector2(-2, 1), DecoBlockType.Bush));
		this.gameObjects.add(new DecoBlock(new Vector2(3, 1), DecoBlockType.Cactus));
		this.gameObjects.add(new DecoBlock(new Vector2(7, 3), DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(1, 3), DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(4, 2.6f), DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(11, 3f), DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(5, 1), DecoBlockType.Plant));
		this.gameObjects.add(new DecoBlock(new Vector2(9, 2), DecoBlockType.Rock));

		for (int i = -5; i < 20; i++) {
			if (i == 8) {
				this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlockType.BlockSlopeStart, this.b2World));
			} else
				if (i == 9) {
					this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlockType.BlockDirt, this.b2World));
				} else
					if (i == 10) {
						this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlockType.BlockSlopeStart, this.b2World, true));
					} else {
						this.gameObjects.add(new BasicBlock(new Vector2(i, 0), BasicBlockType.BlockGrass, this.b2World));
					}

			this.gameObjects.add(new BasicBlock(new Vector2(i, -1), BasicBlockType.BlockDirt, this.b2World));
			this.gameObjects.add(new BasicBlock(new Vector2(i, -2), BasicBlockType.BlockDirt, this.b2World));
			this.gameObjects.add(new BasicBlock(new Vector2(i, -3), BasicBlockType.BlockDirt, this.b2World));

		}

		this.gameObjects.add(new BasicBlock(new Vector2(9, 1), BasicBlockType.BlockGrass, this.b2World));
		this.gameObjects.add(new BasicBlock(new Vector2(8, 1), BasicBlockType.BlockSlope, this.b2World));
		this.gameObjects.add(new BasicBlock(new Vector2(10, 1), BasicBlockType.BlockSlope, this.b2World, true));
		this.gameObjects.add(new JumpPad(new Vector2(1, 1f), this.b2World));
		this.gameObjects.add(new Spikes(new Vector2(2f, 1f), this.b2World));
		Coin c = new Coin(new Vector2(2.2f, 2f));
		this.gameObjects.add(c);
		this.coins.add(c);
		c = new Coin(new Vector2(10, 2f));
		this.gameObjects.add(c);
		this.coins.add(c);
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

	public void reset() {
		this.init();
	}

	public List<Coin> getCoins() {
		return this.coins;
	}
}
