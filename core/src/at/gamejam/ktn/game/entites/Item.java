package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public abstract class Item extends InteractiveObject {

<<<<<<< HEAD
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
=======
	//not animated
	protected TextureRegion texture;
	
	//animated
	protected TextureRegion[] textureRegion;
	protected Animation animation;
	protected float startTime = 0;
	protected int numPictures = 0;
	
	//all
	boolean collected = false;
	boolean animated = false;
	
	Player grabbedBy;
	
	private Body b2Body;
	private World b2World;
	
	public Item(final Vector2 position, World b2World) {
>>>>>>> origin/master
		super();
		this.position = position;
		this.dimension = new Vector2(0.25f, 0.2f);
		this.b2World = b2World;
		
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
		initPhysics();
	}
	
	private void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 2f), this.position.y + (this.dimension.y / 2f)));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		this.b2Body = this.b2World.createBody(bodyDef);
		final PolygonShape polygonShape = new PolygonShape();
		System.out.println(this.dimension.x);
		polygonShape.setAsBox(this.dimension.x / 2f, this.dimension.y / 2f);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setUserData(this);
	}
	
	private void disablePhysics() {
		
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
<<<<<<< HEAD

	// interactions
	public void grabbed(final Player player) {
=======
	
	public Body getBody() {
		return this.b2Body;
	}
	
	//interactions
	public void grabbed(Player player) {
>>>>>>> origin/master
		System.out.println("Item grabbed by: " + player);
		this.collected = true;
		this.grabbedBy = player;
		this.grabbedBy.addItem(this);
		this.disablePhysics();
	}

}
