package at.gamejam.ktn.game.entities;

import at.gamejam.ktn.utils.Assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Lukas on 11.04.2015.
 */
public abstract class GameObject {
	public Vector2		position;
	protected Body		b2Body;
	public Vector2		dimension;
	public Vector2		origin;
	public Vector2		scale;
	public float		rotation;
	protected Assets	assets;
	protected boolean	toRender			= true;
	protected boolean	physicsAlreadyInit	= false;

	public GameObject() {
		this.position = new Vector2();
		this.dimension = new Vector2(1, 1);
		this.origin = new Vector2();
		this.scale = new Vector2(1, 1);
		this.rotation = 0;
		this.assets = Assets.getInstance(new AssetManager());
	}

	public abstract void render(SpriteBatch batch);

	public void update(final float deltaTime) {
	}

	public void initPhysics() {
	}

	/**
	 * @return the toRender
	 */
	protected boolean isToRender() {
		return toRender;
	}

	/**
	 * @param toRender
	 *            the toRender to set
	 */
	public void setToRender(boolean toRender) {
		this.toRender = toRender;
	}

	/**
	 * @return the b2Body
	 */
	public Body getB2Body() {
		return b2Body;
	}
}
