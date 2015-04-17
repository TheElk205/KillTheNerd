package at.gamejam.ktn.game.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Lukas on 11.04.2015.
 */
public class Player extends GameObject {
	public static final float	JUMP_FORCE		= 9f;
	private static final float	ACCELERATION	= 0.5f;
	private static final float	MAX_SPEED		= 3f;
	private TextureRegion		texture;
	private final World			b2World;
	private Body				b2Body;
	private boolean				left;
	private boolean				right;
	private boolean				touchingGround;
	private Sound				sound;

	public Player(final Vector2 position, final World b2World) {
		super();
		this.b2World = b2World;
		this.position = position;
		this.init();
	}

	private void init() {
		this.dimension.set(0.2f, 0.2f);
		this.origin.x = this.dimension.x / 2;
		this.origin.y = this.dimension.y / 2;
		this.texture = this.assets.findRegion("player");
		// this.sound = Gdx.audio.newSound(Gdx.files.internal("bounce.mp3"));
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
		fixtureDef.density = 1f;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0;

		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setLinearDamping(1f);
		this.b2Body.setBullet(true);

		circleShape.dispose(); // clean up!!
		this.b2Body.setUserData(this);
	}

	@Override
	public void render(final SpriteBatch batch) {
		batch.draw(this.texture, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y, this.scale.x,
				this.scale.y, this.rotation);
	}

	@Override
	public void update(final float deltaTime) {
		this.touchingGround = false;
		this.move();
		this.position = this.b2Body.getPosition();
		this.rotation = this.b2Body.getAngle() * MathUtils.radiansToDegrees;
	}

	public void move() {
		final Vector2 toApply = new Vector2();
		if (this.left) {
			toApply.x = -Player.ACCELERATION;
		} else
			if (this.right) {
				toApply.x = Player.ACCELERATION;
			} else {
				toApply.x = 0;
			}
		/*if (((this.b2Body.getLinearVelocity().x > Player.MAX_SPEED) && (toApply.x > 0)) || ((this.b2Body.getLinearVelocity().x < -Player.MAX_SPEED) && (toApply.x < 0))) {
			toApply.x = 0;
		}*/
		this.b2Body.applyForceToCenter(toApply, true);
	}

	public void setLeft(final boolean left) {
		this.left = left;
	}

	public void setRight(final boolean right) {
		this.right = right;
	}

	public void jump() {
		this.testGround();
		if (this.touchingGround) {
			// this.sound.play();
			this.b2Body.applyForceToCenter(0, Player.JUMP_FORCE, true);
		}
	}

	public void testGround() {
		this.b2World.rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(final Fixture fixture, final Vector2 point, final Vector2 normal, final float fraction) {
				if (fixture != null) {
					if (fraction < 0.8f) {
						Player.this.touchingGround = true;
					}
				}
				return 0;
			}
		}, this.b2Body.getWorldCenter(), new Vector2(this.b2Body.getWorldCenter().x, this.b2Body.getWorldCenter().y - 0.2f));
	}

	public Body getBody() {
		return this.b2Body;
	}
}
