package at.gamejam.ktn.game.entites;

import java.util.Vector;

import at.gamejam.ktn.game.WorldController;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Player extends InteractiveObject {
	protected Vector<Item>		items;
	protected int maxItems = 50;
	protected int itemCount = 50;

	protected int				points			= 0;
	protected float				startSpeed		= 3f;
	protected float				currentSpeed	= this.startSpeed;

	protected float				throwingSpeed	= 75;

	protected TextureRegion		texture;
	protected World				b2World;
	protected WorldController	worldController;
	protected Body				b2Body;

	protected Direction			directionMoving;
	protected Direction			directionLooking;

	protected String			name			= "Player";
	private boolean				right;
	private boolean				up;
	private boolean				left;
	private boolean				down;

	protected void initConstructor(final Vector2 position, final WorldController worldcontroller) {
		this.worldController = worldcontroller;
		this.b2World = this.worldController.getB2World();
		this.position = position;
		this.init();
	}

	protected void init() {
		this.items = new Vector<Item>();

		this.dimension.set(0.2f, 0.2f);
		this.origin.x = 0;
		this.origin.y = 0;
		// this.texture = this.assets.findRegion("player");
		this.directionMoving = Direction.STAY;
		this.directionLooking = Direction.S;
		this.loadAsset();
		this.initPhysics();
	}

	private void initPhysics() {
		// create body definition
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(this.position.x, this.position.y);

		// create body in world
		this.b2Body = this.b2World.createBody(bodyDef);

		// create shape
		final CircleShape circleShape = new CircleShape();
		circleShape.setRadius(this.dimension.x / 2);

		// create fixture to attach shape to body
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0f;
		// fixtureDef.friction = 1f;
		// fixtureDef.restitution = 0;

		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setLinearDamping(1f);
		this.b2Body.setBullet(true);

		circleShape.dispose(); // clean up!!
		this.b2Body.setUserData(this);
	}

	protected abstract void loadAsset();

	public enum Direction {
		N, S, W, E, NE, SE, SW, NW, STAY
	}

	public void addPoint() {
		this.points++;
	}

	public void subPoint() {
		this.points--;
		if (this.points < 0) {
			this.points = 0;
		}
	}

	public void getNear(final Object o) {
		if (o instanceof Item) {
			((Item) o).grabbed(this);
		} else
			if (o instanceof Pupil) {
				((Pupil) o).isNear(this);
			}
	}

	public void setDirectionMoving(final Direction d) {
		this.directionMoving = d;
		if (d != Direction.STAY) {
			this.directionLooking = d;
		}
	}

	@Override
	public void render(final SpriteBatch batch) {
		batch.draw(this.texture, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y, this.scale.x,
				this.scale.y, this.rotation);
	}

	@Override
	public void update(final float deltaTime) {
		this.move();
		this.position = this.b2Body.getPosition();
		this.rotation = this.b2Body.getAngle() * MathUtils.radiansToDegrees;
	}

	public void move() {
		final Vector2 toApply = new Vector2();
		/*switch (this.directionMoving) {
			case N:
				toApply.y = this.currentSpeed;
				break;
			case S:
				toApply.y = -this.currentSpeed;
				break;
			case E:
				toApply.x = this.currentSpeed;
				break;
			case W:
				toApply.x = -this.currentSpeed;
				break;
			default:
				break;
		}*/

		if (this.up) {
			toApply.y = this.currentSpeed;
		}
		if (this.down) {
			toApply.y = -this.currentSpeed;
		}
		if (this.right) {
			toApply.x = this.currentSpeed;
		}
		if (this.left) {
			toApply.x = -this.currentSpeed;
		}
		// System.out.println("currentSpeed: " + this.currentSpeed);
		this.b2Body.applyForceToCenter(toApply, true);
	}

	public boolean addItem(final Item item) {
		if(this.itemCount < this.maxItems) {
			//this.items.add(item);
			if(item.collectable) {
				item.collectable = false;
				this.itemCount++;
				System.out.println("Got an Item! " + this.itemCount);
				return true;
			}
		}
		return false;
	}

	public void stop() {
		// System.out.println("Stop");
		// this.b2Body.applyForceToCenter(this.b2Body.get, wake);
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		this.b2Body.setLinearVelocity(new Vector2(0, 0));
		this.b2Body.setAngularVelocity(0);
		this.directionMoving = Direction.STAY;
	}

	public void throwItem() {
		if(itemCount > 0) {
			final RedBull bull = new RedBull(this.position, this.b2World);
			//bull.init(true);
			bull.collected = false;
			bull.collectable = false;
			final Vector2 toApply = new Vector2();
			// System.out.println(bull.position);
			switch (this.directionLooking) {
				case N:
					// toApply.y = throwingSpeed;
					bull.position.y += 2;
					// System.out.print("UP");
					break;
				case S:
					// toApply.y = -throwingSpeed;
					bull.position.y -= 2;
					break;
				case E:
					// toApply.x = throwingSpeed;
					bull.position.x += 2;
					break;
				case W:
					// toApply.x = -throwingSpeed;
					bull.position.x -= 2;
					break;
				default:
					System.out.println("What the fuck");
					break;
			}
			System.out.println(bull.position);
			System.out.println(this.position);
			bull.getBody().applyForceToCenter(toApply, true);
			this.worldController.addRedBull(bull);
			// this.items.remove(this.items.size()-1);
			itemCount--;
			System.out.println("Item Thrown: " +itemCount);
		}
	}

	public Body getBody() {
		return this.b2Body;
	}

	public abstract void hitByItem(Item item);

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String toStrign() {
		return this.name;
	}

	public void setRight(final boolean b) {
		this.right = b;
	}

	public void setUp(final boolean b) {
		this.up = b;
	}

	public void setLeft(final boolean b) {
		this.left = b;
	}

	public void setDown(final boolean b) {
		this.down = b;
	}

	public boolean getRight() {
		return this.right;
	}

	public boolean getUp() {
		return this.up;
	}

	public boolean getLeft() {
		return this.left;
	}

	public boolean getDown() {
		return this.down;
	}
}
