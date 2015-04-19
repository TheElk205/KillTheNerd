package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;

import at.gamejam.ktn.game.entities.BasicBlock;
import at.gamejam.ktn.game.entities.Coin;
import at.gamejam.ktn.game.entities.DecoBlock;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.game.entities.JumpPad;
import at.gamejam.ktn.game.entities.Spikes;

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

		this.gameObjects.add(new DecoBlock(new Vector2(-2, 1), DecoBlock.DecoBlockType.Bush));
		this.gameObjects.add(new DecoBlock(new Vector2(3, 1), DecoBlock.DecoBlockType.Cactus));
		this.gameObjects.add(new DecoBlock(new Vector2(7, 3), DecoBlock.DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(1, 3), DecoBlock.DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(4, 2.6f), DecoBlock.DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(11, 3f), DecoBlock.DecoBlockType.Cloud));
		this.gameObjects.add(new DecoBlock(new Vector2(5, 1), DecoBlock.DecoBlockType.Plant));
		this.gameObjects.add(new DecoBlock(new Vector2(9, 2), DecoBlock.DecoBlockType.Rock));

		for (int i = -5; i < 20; i++) {
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

		}

		this.gameObjects.add(new BasicBlock(new Vector2(9, 1), BasicBlock.BasicBlockType.BlockGrass, this.b2World));
		this.gameObjects.add(new BasicBlock(new Vector2(8, 1), BasicBlock.BasicBlockType.BlockSlope, this.b2World));
		this.gameObjects.add(new BasicBlock(new Vector2(10, 1), BasicBlock.BasicBlockType.BlockSlope, this.b2World, true));
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
