package at.game.visuals;

import java.util.ArrayList;
import java.util.List;

import at.game.managers.Assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject {
	public static ArrayList<TextureRegion>	textureList			= new ArrayList<TextureRegion>();
	public static ArrayList<String>			textureNameList		= new ArrayList<String>();
	public Vector2							position;
	public Body								b2Body;
	public Vector2							dimension;
	public Vector2							origin;
	public Vector2							scale;
	public float							rotation;
	public static Assets					assets;
	protected boolean						toRender			= true;
	protected boolean						physicsAlreadyInit	= false;
	public static List<GameObject>			totalObjects		= new ArrayList<GameObject>();
	public static int						totalObjectCount	= 0;
	public boolean							toDelete			= false;

	static {
		GameObject.assets = Assets.getInstance(new AssetManager());
	}

	public GameObject() {
		this.position = new Vector2();
		this.dimension = new Vector2(1, 1);
		this.origin = new Vector2();
		this.scale = new Vector2(1, 1);
		this.rotation = 0;

		GameObject.totalObjects.add(this);
		GameObject.totalObjectCount++;
		// System.out.println(GameObject.totalObjectCount + ": " + this);
	}

	public abstract void render(SpriteBatch batch);

	public abstract void update(final float deltaTime);

	public abstract void initPhysics();

	/**
	 * @return the toRender
	 */
	protected boolean isToRender() {
		return this.toRender;
	}

	/**
	 * @param toRender
	 *            the toRender to set
	 */
	public void setToRender(final boolean toRender) {
		this.toRender = toRender;
	}

	/**
	 * @return the b2Body
	 */
	public Body getB2Body() {
		return this.b2Body;
	}
}
