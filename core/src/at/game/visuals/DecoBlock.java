package at.game.visuals;

import at.game.visuals.tiles.DecoBlockType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lukas on 29.03.2015.
 */
public class DecoBlock extends GameObject {
	private final DecoBlockType	type;
	private TextureRegion		textureHanging;

	public DecoBlock(final Vector2 position, final DecoBlockType type) {
		super();
		this.type = type;
		this.position = position;
		this.dimension = new Vector2(1f, 1f);
		this.init();
	}

	private void init() {
		switch (this.type) {
			case Rock:
				this.textureHanging = GameObject.assets.findRegion("rock");
				break;
			case Cactus:
				this.textureHanging = GameObject.assets.findRegion("cactus");
				break;
			case Cloud:
				this.textureHanging = GameObject.assets.findRegion("cloud1");
				this.dimension = new Vector2(1, 0.5f);
				break;
			case Bush:
				this.textureHanging = GameObject.assets.findRegion("bush");
				break;
			case NPC:
				this.textureHanging = GameObject.assets.findRegion("worker");
				break;
			case Plant:
			default:
				this.textureHanging = GameObject.assets.findRegion("plant");
				break;
		}
	}

	@Override
	public void render(final SpriteBatch batch) {
		batch.draw(this.textureHanging, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
	}

	@Override
	public void update(final float deltaTime) {
		// should extend other class, cause no update needed
	}

	@Override
	public void initPhysics() {
		// nothing to init
	}
}
