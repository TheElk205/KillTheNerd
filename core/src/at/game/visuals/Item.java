package at.game.visuals;

import java.util.ArrayList;
import java.util.List;

import at.game.WorldController;
import at.game.enums.ItemType;
import at.game.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Item extends InteractiveObject {
	private static final Sound	GRAB_SOUND		= Gdx.audio.newSound(Gdx.files.internal(Constants.GRAB_SOUND));
	private boolean				collected		= false;
	private boolean				collectable		= true;
	private Player				grabbedBy;
	private Player				itemIsThrownBy	= null;
	private final float			dmg				= 0.05f;
	// public boolean destroyed = false;
	public static List<Item>	itemList		= new ArrayList<Item>();
	private float				flyingTime		= 0;
	private boolean				isFlying		= false;
	private String				name			= "no Name";
	final private ItemType		itemType;

	public Item(final Vector2 position, final boolean initPhysics, final ItemType itemType, final String name) {
		this.itemType = itemType;
		switch (itemType) {
			case Wake_Item:
				this.numPictures = 6; // TODO: calc num on its own, depending on size or something
				this.texture = GameObject.assets.findRegion("cup_coffee");
				break;
			case Sleep_Item:
				this.numPictures = 6; // TODO: calc num on its own, depending on size or something
				this.texture = GameObject.assets.findRegion("book_green");
				break;
			default:
				break;
		}
		this.name = name;
		this.position = position;
		Item.itemList.add(this);
		// this.dimension = new Vector2(0.25f, 0.2f);
		this.dimension = new Vector2(0.15f, 0.15f);
		// this.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.THROW_SOUND));
		if (initPhysics) {
			this.init(true, initPhysics);
		}
	}

	protected void init(final boolean animated, final boolean initPhysics) {
		this.animated = animated;
		this.collected = false;
		if (this.animated) {
			this.initAnimated();
		} else {

		}
		if (initPhysics) {
			this.initPhysics();
		}
	}

	public void setFlying() {
		this.collected = false;
		this.collectable = false;
		this.isFlying = true;
	}

	public void reset() {
		this.isFlying = false;
		this.collectable = true;
		this.collected = false;
	}

	/*protected void init(final boolean animated, final boolean physics) {
		this.animated = animated;
		this.collected = false;
		if (this.animated) {
			this.initAnimated();
		}
		if (physics) {
			this.initPhysics();
		}
	}*/

	@Override
	public void initPhysics() {
		if (!this.physicsAlreadyInit) {
			this.physicsAlreadyInit = true;
			final BodyDef bodyDef = new BodyDef();
			// System.out.println(this.dimension + " " + this.position);
			bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 2f), this.position.y + (this.dimension.y / 2f)));
			bodyDef.type = BodyDef.BodyType.DynamicBody;
			this.b2Body = WorldController.topDown_b2World.createBody(bodyDef);
			final PolygonShape polygonShape = new PolygonShape();
			// System.out.println(this.dimension.x);
			polygonShape.setAsBox(this.dimension.x / 2f, this.dimension.y / 2f);
			final FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.isSensor = false;
			fixtureDef.density = 5f;
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 0;
			fixtureDef.shape = polygonShape;
			this.b2Body.setBullet(true);
			this.b2Body.createFixture(fixtureDef);
			this.b2Body.setUserData(this);
		}
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (this.animated && !this.collected && this.toRender) {
			batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		}
		if (!this.animated && !this.collectable && this.toRender) {

			batch.draw(this.texture, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
					this.scale.x, this.scale.y, this.rotation);

		}
	}

	@Override
	public void update(final float deltaTime) {
		// super.update(deltaTime);
		this.startTime += deltaTime;
		if (this.b2Body != null) {
			this.position = this.b2Body.getPosition();
			this.position.x = this.position.x - (this.dimension.x / 2);
			this.position.y = this.position.y - (this.dimension.y / 2);
			this.rotation = this.b2Body.getAngle() * MathUtils.radiansToDegrees;
		}
	}

	public boolean isCollected() {
		return this.collected;
	}

	public void setCollected(final boolean b) {
		if (this.collectable) {
			this.collected = b;
		}
	}

	// interactions
	public boolean grabbedBy(final Player player) {
		if (this.collectable && !this.collected && !this.isFlying) { // TODO: !this.collected prevent taking twice bug!?
			// System.out.println("Item grabbed by: " + player);
			// player.incrItemCount();
			this.grabbedBy = player;
			if (this.grabbedBy.addItem(this)) {
				Item.GRAB_SOUND.play();
				this.collected = true;
				// this.destroyed = false;
				this.toDelete = true;
				return true;
			}
			this.grabbedBy = null;
			this.collected = false;
			this.toDelete = false;
			return false;
		}
		return false;
	}

	public float getDmg() {
		return this.dmg;
	}

	public void toDelete(final boolean b) {
		this.toDelete = b;
	}

	public boolean isCollectable() {
		return this.collectable;
	}

	public void setCollectable(final boolean b) {
		this.collectable = b; // TODO sollte nicht veränderlich sein, und wenn flying anders gelöst werden
	}

	public boolean isFlying() {
		return this.isFlying;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public ItemType getItemType() {
		return this.itemType;
	}

	public Player getItemIsThrownBy() {
		return this.itemIsThrownBy;
	}

	protected void setItemIsThrownBy(final Player player) {
		this.itemIsThrownBy = player;
	}

	public float getFlyingTime() {
		return this.flyingTime;
	}

	public void setFlyingTime(final float f) {
		this.flyingTime = f;
	}
}
