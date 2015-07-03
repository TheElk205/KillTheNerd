package at.game.visuals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Coin extends GameObject {
	private static final int	FRAME_COLS	= 8;
	private TextureRegion[]		animationFrames;
	private float				startTime	= 0;
	private boolean				collected	= false;
	private Animation			animation;

	public Coin(final Vector2 position) {
		super();
		this.position = position;
		this.renderDimension = new Vector2(0.25f, 0.2f);
		this.init();
	}

	private void init() {
		final TextureRegion textureRetion = GameObject.assets.findRegion("coin_gold");
		final TextureRegion[][] tmp = textureRetion.split(textureRetion.getRegionWidth() / Coin.FRAME_COLS, textureRetion.getRegionHeight()); // #8
		this.animationFrames = new TextureRegion[Coin.FRAME_COLS];
		int index = 0;
		for (int j = 0; j < Coin.FRAME_COLS; j++) {
			this.animationFrames[index++] = tmp[0][j];
		}
		this.startTime = 0;
		this.animation = new Animation(0.1f, this.animationFrames);
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (!this.collected) {
			batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.renderDimension.x, this.renderDimension.y);
		}
	}

	@Override
	public void update(final float deltaTime) {
		// super.update(deltaTime);
		this.startTime += deltaTime;
	}

	public boolean isCollected() {
		return this.collected;
	}

	public void setCollected(final boolean b) {
		this.collected = b;
	}

	@Override
	public void initPhysics() {
		// nothing to init
	}
}
