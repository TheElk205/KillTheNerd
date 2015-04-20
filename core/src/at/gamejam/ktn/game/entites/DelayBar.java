package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DelayBar extends InteractiveObject {
	private TextureRegion	green;
	private TextureRegion	red;
	private final Player	belongsTo;
	private float			duration	= 0f;
	private float			startedAt	= 0f;
	private float			time		= 0f;
	private boolean			started		= false;

	public DelayBar(final Player belongsto) {
		this.belongsTo = belongsto;
		this.duration = this.belongsTo.getHandicapduration();
		this.loadAssets();
		this.dimension = new Vector2(0.1f, 0.1f);
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (!this.started) {
			return;
		}
		final float length = this.belongsTo.dimension.y;
		final float value = (this.time - this.startedAt) / this.duration;
		if (value >= 1) {
			this.started = false;
		}
		final float pos = length * value;

		batch.draw(this.green, this.belongsTo.position.x, this.belongsTo.position.y + (this.belongsTo.dimension.y / 2), pos, this.dimension.y);
		batch.draw(this.red, this.belongsTo.position.x + pos, this.belongsTo.position.y + (this.belongsTo.dimension.y / 2), length - pos, this.dimension.y);

	}

	@Override
	public void update(final float deltaTime) {
		this.time += deltaTime;
		if (this.belongsTo.getHandicap() != 1) {
			this.start(this.time);
		}
	}

	public void start(final float time) {
		if (!this.started) {
			this.started = true;
			this.startedAt = time;
		}
	}

	private void loadAssets() {
		this.green = this.assets.findRegion("green_pixels");
		this.red = this.assets.findRegion("red_pixels");
	}

}
