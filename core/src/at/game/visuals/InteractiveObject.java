package at.game.visuals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class InteractiveObject extends GameObject {
	protected float				frameDuration	= 0.1f;
	protected TextureRegion[]	textureRegion;
	protected float				startTime		= 0;
	protected Animation			animation;
	protected boolean			animated		= false;
	protected TextureRegion		texture;
	protected int				numPictures		= 0;

	protected void initAnimated() {
		TextureRegion[][] tmp = null;
		try {
			tmp = this.texture.split(this.texture.getRegionWidth() / this.numPictures, this.texture.getRegionHeight());
		} catch (final Exception e) {

		}
		this.textureRegion = new TextureRegion[this.numPictures];
		int index = 0;
		for (int j = 0; j < this.numPictures; j++) {
			this.textureRegion[index++] = tmp[0][j];
		}
		this.startTime = 0;
		this.animation = new Animation(this.frameDuration, this.textureRegion);
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (this.toRender) {
			batch.draw(this.texture, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
					this.scale.x, this.scale.y, this.rotation);
		}
	}

	// protected abstract void loadAsset();

	@Override
	public abstract void update(final float deltaTime);
}
