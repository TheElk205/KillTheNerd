package at.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Herkt Kevin
 */
public abstract class RenderObject {
	private final boolean	animatedObject	= true;
	private Animation		animation;
	private final float		startTime		= 0;
	protected Vector2		position;
	protected Vector2		dimension		= new Vector2(1, 1);
	protected TextureRegion	wholeTexture;

	/*public void render(final SpriteBatch batch) {
		// batch.draw(this.tRStandDown, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x,
		// this.origin.y, this.dimension.x, this.dimension.y,
		// this.scale.x, this.scale.y, this.rotation);
		try {
			if (this.animatedObject) {
				batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
			} else {
				batch.draw(this.wholeTexture, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
			}
		} catch (final Exception e) {
			System.out.println("cant render");
			System.exit(0);
		}
	}*/

	public abstract void render(final SpriteBatch batch);
}
