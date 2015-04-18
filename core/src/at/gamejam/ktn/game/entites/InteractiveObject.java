package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.entities.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class InteractiveObject extends GameObject {

	public InteractiveObject() {
		super();
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (toRender) {
			batch.draw(null, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y, this.scale.x,
					this.scale.y, this.rotation);
		}
	}

	@Override
	public abstract void update(final float deltaTime);
}
