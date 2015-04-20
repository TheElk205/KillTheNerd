package at.gamejam.ktn.game.entites;

import java.util.ArrayList;
import java.util.List;

import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Item extends InteractiveObject {
	private static final Sound	GRAB_SOUND		= Gdx.audio.newSound(Gdx.files.internal(Constants.GRAB_SOUND));
	public boolean				collected		= false;
	public boolean				collectable		= true;
	private Player				grabbedBy;
	public Player				itemIsThrownBy	= null;
	private final float			dmg				= 0.05f;
	public boolean				destroyed		= false;
	public static List<Item>	itemList		= new ArrayList<Item>();
	public float				flyingTime		= 0;
	public boolean				isFlying		= false;

	public Item(final Vector2 position, final World b2World) {
		this.position = position;
		Item.itemList.add(this);
		// this.dimension = new Vector2(0.25f, 0.2f);
		this.dimension = new Vector2(0.15f, 0.15f);
		// this.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.THROW_SOUND));
		this.b2World = b2World;
	}

	protected void init(final boolean animated, final boolean initPhysics) {
		this.animated = animated;
		this.collected = false;
		if (this.animated) {
			this.loadAsset();
			this.initAnimated();
		} else {

		}
		if (initPhysics) {
			this.initPhysics();
		}
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
			this.b2Body = this.b2World.createBody(bodyDef);
			final PolygonShape polygonShape = new PolygonShape();
			// System.out.println(this.dimension.x);
			polygonShape.setAsBox(this.dimension.x / 2f, this.dimension.y / 2f);
			final FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.density = 5f;
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 0;
			fixtureDef.shape = polygonShape;
			this.b2Body.setBullet(true);
			this.b2Body.createFixture(fixtureDef);
			this.b2Body.setUserData(this);
		}
	}

	protected abstract void loadAsset();

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
	public void grabbedBy(final Player player) {
		if (this.collectable && !this.collected) { // TODO: !this.collected prevent taking twice bug!?

			// System.out.println("Item grabbed by: " + player);
			// player.incrItemCount();
			this.grabbedBy = player;
			if (this.grabbedBy.addItem(this)) {
				Item.GRAB_SOUND.play();
				this.collected = true;
				this.destroyed = false;
			} else {
				this.grabbedBy = null;
			}
			// this.disablePhysics();
		}
	}

	public float getDmg() {
		return this.dmg;
	}
}
