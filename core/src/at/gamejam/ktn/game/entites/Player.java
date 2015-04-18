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
	protected Vector<Item> items;
	protected int maxItems = 5;
	
	protected int points = 0;
	protected float startSpeed = 3f;
	protected float currentSpeed = this.startSpeed;
	
	protected float throwingSpeed = 75;
	
	protected TextureRegion texture;
	protected World b2World;
	protected WorldController worldController;
	protected Body b2Body;
	
	protected Direction directionMoving;
	protected Direction directionLooking;
	
	protected String name = "Player";
	
	protected void initConstructor(final Vector2 position, WorldController worldcontroller) {
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
		//this.texture = this.assets.findRegion("player");
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
		//fixtureDef.friction = 1f;
		//fixtureDef.restitution = 0;

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
		points++;
	}
	
	public void subPoint() {
		points--;
		if(points < 0) {
			points = 0;
		}
	}
	
	public void getNear(Object o) {
		if(o instanceof Item) {
			((Item)o).grabbed(this);
		}
		else if(o instanceof Pupil) {
			((Pupil)o).isNear(this);
		}
	}
	
	public void setDirectionMoving(Direction d) {
		this.directionMoving = d;
		if(d != Direction.STAY) {
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
		Vector2 toApply = new Vector2();
		switch(this.directionMoving) {
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
		}
		this.b2Body.applyForceToCenter(toApply, true);
	}
	
	public boolean addItem(Item item) {
		if(this.items.size() < this.maxItems) {
			this.items.add(item);
			System.out.println("Got an Item!");
			return true;
		}
		return false;
	}
	
	public void stop() {
		//System.out.println("Stop");
		//this.b2Body.applyForceToCenter(this.b2Body.get, wake);
		this.b2Body.setLinearVelocity(new Vector2(0,0));
		this.b2Body.setAngularVelocity(0);
		this.directionMoving = Direction.STAY;
	}
	
	public void throwItem() {
		if(this.items.size() >= 0) {
			RedBull bull = new RedBull(this.position, this.b2World);
			bull.init(true);
			bull.collected = false;
			//bull.collectable = false;
			Vector2 toApply = new Vector2();
			System.out.println(bull.position);
			switch(directionLooking) {
			case N:
				//toApply.y = throwingSpeed;
				bull.position.y += 2;
				System.out.print("UP");
				break;
			case S:
				//toApply.y = -throwingSpeed;
				bull.position.y -= 2;
				break;
			case E:
				//toApply.x = throwingSpeed;
				bull.position.x += 2;
				break;
			case W:
				//toApply.x = -throwingSpeed;
				bull.position.x -= 2;
				break;
			default:
				System.out.println("What the fuck");
				break;
			}
			System.out.println(bull.position);
			System.out.println(this.position);
			bull.getBody().applyForceToCenter(toApply, true);
			worldController.addRedBull(bull);
			//this.items.remove(this.items.size()-1);
		}
	}
	
	public Body getBody() {
		return this.b2Body;
	}
	
	public abstract void hitByItem(Item item);
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toStrign() {
		return this.name;
	}
}
