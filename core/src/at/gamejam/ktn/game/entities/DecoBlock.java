package at.gamejam.ktn.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lukas on 29.03.2015.
 */
public class DecoBlock extends GameObject {

	DecoBlockType			type;
	private TextureRegion	textureHanging;

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
				this.textureHanging = this.assets.findRegion("rock");
				break;
			case Cactus:
				this.textureHanging = this.assets.findRegion("cactus");
				break;
			case Cloud:
				this.textureHanging = this.assets.findRegion("cloud1");
				this.dimension = new Vector2(1, 0.5f);
				break;
			case Bush:
				this.textureHanging = this.assets.findRegion("bush");
				break;
			case Plant:
			default:
				this.textureHanging = this.assets.findRegion("plant");
				break;
		}
	}

	@Override
	public void render(final SpriteBatch batch) {
		batch.draw(this.textureHanging, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
	}

	public enum DecoBlockType {
		Rock, Cactus, Cloud, Bush, Plant
	}
}
