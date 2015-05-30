package at.game.visuals;

import at.game.enums.ItemType;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ThrowableObject extends Item {
	public ThrowableObject(final Vector2 position, final ItemType itemType, final String name) {
		super(position, true, itemType, name);
		// TODO Auto-generated constructor stub
	}

	TextureRegion[]		animationFrames;
	float				startTime	= 0;
	boolean				collected	= false;
	private Animation	animation;

	/*private void init() {
		final TextureRegion t = this.assets.findRegion("coin_gold");
		final TextureRegion[][] tmp = t.split(t.getRegionWidth() / ThrowableObject.FRAME_COLS, t.getRegionHeight()); // #8
		this.animationFrames = new TextureRegion[ThrowableObject.FRAME_COLS];
		int index = 0;
		for (int j = 0; j < ThrowableObject.FRAME_COLS; j++) {
			this.animationFrames[index++] = tmp[0][j];
		}
		this.startTime = 0;
		this.animation = new Animation(0.1f, this.animationFrames);
	}*/

	@Override
	public void render(final SpriteBatch batch) {
		if (!this.collected) {
			batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		}
	}

	@Override
	public void update(final float deltaTime) {
		super.update(deltaTime);
		this.startTime += deltaTime;
	}

	@Override
	public boolean isCollected() {
		return this.collected;
	}

	@Override
	public void setCollected(final boolean b) {
		this.collected = b;
	}
}
