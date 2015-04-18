package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class NPC extends InteractiveObject {

	/**
	 * @param startValue
	 *            0 awake, 100 sleeps
	 */
	public NPC(Vector2 position, World b2World, int startValue) {
		this.position = position;// super(position, b2World);
		this.dimension = new Vector2(0.15f, 0.15f);
		this.b2World = b2World;
		this.numPictures = 4;
		this.frameDuration = 0.25f;
		// this.numPictures = 5 * 13;
		// this.frameDuration = 0.15f;
		this.animated = true;
		if (this.animated) {
			this.loadAsset();
			this.initAnimated();
		}
		this.initPhysics();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(final SpriteBatch batch) {
		// if (this.toRender) {
		batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		// }
	}

	public void loadAsset() {
		this.texture = this.assets.findRegion("newRedBull_2_true"); // worker
	}

}
