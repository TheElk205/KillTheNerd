package at.game.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationContainer {
	private final float				frameDuration	= 0.1f;
	private final TextureRegion[]	textureRegion;
	private float					stateTime		= 0;
	private final Animation			animation;
	private final AtlasRegion		wholeTexture;
	private int						numPictures		= 0;

	public AnimationContainer(final int numPictures, final String image) {
		this.numPictures = numPictures;// TODO: calc num on its own, depending on size or something
		this.wholeTexture = Assets.getInstance().findRegion(image);

		TextureRegion[][] tmp = null;
		tmp = this.wholeTexture.split(this.wholeTexture.getRegionWidth() / this.numPictures, this.wholeTexture.getRegionHeight());
		this.textureRegion = new TextureRegion[this.numPictures];
		int index = 0;
		for (int j = 0; j < this.numPictures; j++) {
			this.textureRegion[index++] = tmp[0][j];
		}
		this.stateTime = 0;
		this.animation = new Animation(this.frameDuration, this.textureRegion);
	}

	public void update(final float deltaTime) {
		this.stateTime += deltaTime;
	}

	public TextureRegion getKeyFrame() {
		// this.stateTime, true
		return this.animation.getKeyFrame(this.stateTime, true);
	}
}
