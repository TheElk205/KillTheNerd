package at.gamejam.ktn.game.entites;

import java.util.Vector;

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
	protected int maxItems;
	
	protected int points = 0;
	protected float startSpeed = 3f;
	protected float currentSpeed = this.startSpeed;
	
	protected TextureRegion texture;
	protected World b2World;
	protected Body b2Body;
	
	protected Direction direction;
	
	protected void initConstructor(final Vector2 position, final World b2World) {
		this.b2World = b2World;
		this.position = position;
		this.init();
	}
	
	protected void init() {
		this.dimension.set(0.2f, 0.2f);
		this.origin.x = this.dimension.x / 2;
		this.origin.y = this.dimension.y / 2;
		//this.texture = this.assets.findRegion("player");
		this.direction = Direction.STAY;
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
		//fixtureDef.density = 1f;
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
			((Item)o).grabbed();
		}
		else if(o instanceof Pupil) {
			((Pupil)o).isNear(this);
		}
	}
	
	public void setDirection(Direction d) {
		this.direction = d;
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
		switch(this.direction) {
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
	
	public void stop() {
		System.out.println("Stop");
		//this.b2Body.applyForceToCenter(this.b2Body.get, wake);
		this.b2Body.setLinearVelocity(new Vector2(0,0));
		this.b2Body.setAngularVelocity(0);
		this.direction = Direction.STAY;
	}
	
	public void throwItem(Item item) {
		
	}
	
	public Body getBody() {
		return this.b2Body;
	}
	
	public abstract void hitByItem(Item item);
	
}
