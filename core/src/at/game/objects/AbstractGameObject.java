package at.game.objects;

import java.util.ArrayList;
import java.util.List;

import at.game.WorldController;
import at.game.components.Geometrics;
import at.game.managers.AnimationContainer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractGameObject {
	/* ----- control values -----*/
	protected boolean							toDelete			= false;
	protected boolean							toRender			= true;
	protected boolean							physicsAlreadyInit	= false;

	/* ----- statics ----- */
	// public static Assets assets;
	private static List<AbstractGameObject>		totalObjects		= new ArrayList<AbstractGameObject>();
	protected static int						totalObjectCount	= 0;
	protected static ArrayList<TextureRegion>	textureList			= new ArrayList<TextureRegion>();		// TODO: 05.07 is not used?
	protected static ArrayList<String>			textureNameList		= new ArrayList<String>();

	/* ----- Animation stuff -----*/
	protected AnimationContainer				animationCon;
	protected boolean							animated			= false;

	/* ----- box2D stuff ----- */
	protected final Geometrics					geometrics;
	// protected Body b2Body;
	// protected Vector2 position;
	// protected Vector2 renderDimension = null; // = new Vector2(1, 1);
	// protected Vector2 origin = new Vector2();
	// protected Vector2 scale = new Vector2();
	// protected float rotation = 0;

	/** ashley entity - entity component system */
	protected Entity							entity;

	/*static {
		AbstractGameObject.assets = Assets.getInstance(new AssetManager());
	}*/

	public AbstractGameObject(final Body b2Body, final Vector2 position, final float approxB2Width, final float approxB2Height) {
		this(new Geometrics(b2Body, position, new Vector2(approxB2Width, approxB2Height), approxB2Width, approxB2Height));
		// this.position = new Vector2();
		// this.origin = new Vector2();
		// this.scale = new Vector2(1, 1);

		AbstractGameObject.totalObjects.add(this);
		AbstractGameObject.totalObjectCount++;
		this.entity = new Entity();
		WorldController.ashleyEngine.addEntity(this.entity);
		// System.out.println(AbstractGameObject.totalObjectCount + ": " + this);
	}

	/*private AbstractGameObject(final Vector2 position, final Vector2 renderDimension, final float approxB2Width, final float approxB2Height) {
		this(null, position, approxB2Width, approxB2Height);
	}*/

	private AbstractGameObject(final Geometrics geometrics) {
		this.geometrics = geometrics;
		geometrics.getB2Body().setUserData(this);
		AbstractGameObject.totalObjects.add(this);
		AbstractGameObject.totalObjectCount++;
	}

	/*
	public void render(final SpriteBatch batch) {
		if (this.toRender) {
			batch.draw(this.texture, this.position.x - (this.renderDimension.x / 2), this.position.y - (this.renderDimension.y / 2), this.origin.x, this.origin.y, this.renderDimension.x, this.renderDimension.y,
					this.scale.x, this.scale.y, this.rotation);
		}
	}*/

	// protected abstract void loadAsset();

	public abstract void render(SpriteBatch batch);

	public abstract void update(final float deltaTime);

	public abstract void initPhysics();

	public void onCollision(final AbstractGameObject agObject) // TODO double-dispatch collision detection (visitor pattern)
	{
		if (true)// agObject.isAggro && agObject.canHurt()
		{
			// agObject.getDmg()
			// agObject.getDmgType()
			// this.getHP()
			// this.getDefense()

			if (agObject.willBeRemovedOnCollision()) {
				agObject.toDelete = true;
				agObject.toRender = false;
			}
		}
	}

	private boolean willBeRemovedOnCollision() {
		return true;
	}

	/*
	 * @return the toRender

	private boolean isToRender() {
		return this.toRender;
	}

	/*
	 * @param toRender
	 *           the toRender to set
	 *
	private void setToRender(final boolean toRender) {
		this.toRender = toRender;
	}*/

	/**
	 * @return the b2Body
	 */
	public Body getB2Body() {
		return this.geometrics.getB2Body();
	}

	public Vector2 getPosition() {
		return this.geometrics.getB2Body().getPosition();
	}

	public Vector2 getRenderDimension() {
		return this.geometrics.getRenderDimension();
	}

	public static List<AbstractGameObject> getTotalObjects() {
		return AbstractGameObject.totalObjects;
	}

	public static ArrayList<TextureRegion> getTextureList() {
		return AbstractGameObject.textureList;
	}

	public static ArrayList<String> getTextureNameList() {
		return AbstractGameObject.textureNameList;
	}

	public void setToDelete(final boolean toDelete) {
		this.toDelete = toDelete;
	}

	public boolean isToDelete() {
		return this.toDelete;
	}
}
