package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Item extends InteractiveObject {

	// not animated
	protected TextureRegion		texture;

	// animated
	protected TextureRegion[]	textureRegion;
	protected Animation			animation;
	protected float				startTime	= 0;
	protected int				numPictures	= 0;

	// all
	boolean						collected	= false;
	boolean						animated	= false;

	Player						grabbedBy;

	public Item() {

	}

	public Item(final Vector2 position) {
		super();
		this.position = position;
		this.dimension = new Vector2(0.25f, 0.2f);
	}

	// public Item(boolean animated) {
	// super();
	// this.animated = animated;
	// }

	protected void init(final boolean animated) {
		this.animated = animated;
		if (this.animated) {
			this.initAnimated();
		}
	}

	private void initAnimated() {
		this.loadAsset();
		final TextureRegion[][] tmp = this.texture.split(this.texture.getRegionWidth() / this.numPictures, this.texture.getRegionHeight()); // #8
		this.textureRegion = new TextureRegion[this.numPictures];
		int index = 0;
		for (int j = 0; j < this.numPictures; j++) {
			this.textureRegion[index++] = tmp[0][j];
		}
		this.startTime = 0;
		this.animation = new Animation(0.1f, this.textureRegion);
	}

	protected abstract void loadAsset();

	@Override
	public void render(final SpriteBatch batch) {
		if (this.animated) {
			if (!this.collected) {
				batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
			}
		}
	}

	@Override
	public void update(final float deltaTime) {
		super.update(deltaTime);
		this.startTime += deltaTime;
	}

	public boolean isCollected() {
		return this.collected;
	}

	public void setCollected(final boolean b) {
		this.collected = b;
	}

	// interactions
	public void grabbed(final Player player) {
		System.out.println("Item grabbed by: " + player);
		this.collected = true;
		this.grabbedBy = player;
		this.grabbedBy.addItem(this);
	}

}
