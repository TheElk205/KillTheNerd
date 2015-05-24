package at.game.visuals.tiles;

import at.game.visuals.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Lukas on 28.03.2015.
 */
public class JumpPad extends GameObject {

	private static final float	JUMP_FORCE	= 10f;
	private static float		UP_TIME		= 0.5f;
	private TextureRegion		textureDown;
	private TextureRegion		textureUp;
	private boolean				triggered;
	private final World			b2World;
	private Body				b2Body;
	private float				startTime	= 0;

	public JumpPad(final Vector2 position, final World b2World) {
		super();
		this.position = position;
		this.dimension = new Vector2(0.5f, 0.2f);
		this.b2World = b2World;
		this.init();
	}

	private void init() {
		this.triggered = false;
		this.textureDown = GameObject.assets.findRegion("springboardDown");
		this.textureUp = GameObject.assets.findRegion("springboardUp");
		this.startTime = 0;
		this.initPhysics();
	}

	@Override
	public void update(final float deltaTime) {
		this.startTime += deltaTime;
		if (this.startTime > JumpPad.UP_TIME) {
			this.triggered = false;
		}
		this.checkBodies();
	}

	private void checkBodies() {
		final float border = this.dimension.x / 2.5f;
		final float lowerX = this.position.x + border;
		final float lowerY = this.position.y + (this.dimension.y / 2f);
		this.b2World.QueryAABB(new QueryCallback() {
			@Override
			public boolean reportFixture(final Fixture fixture) {
				/*if (fixture.getBody().getUserData() instanceof Player) {
					JumpPad.this.trigger();
					fixture.getBody().setLinearVelocity(JumpPad.this.b2Body.getLinearVelocity().x, 0);
					fixture.getBody().applyForceToCenter(0, JumpPad.JUMP_FORCE * 1.5f, true);
				}*/
				return false;
			}
		}, lowerX, lowerY, (lowerX + this.dimension.x) - (2 * border), lowerY + 0.1f);
	}

	@Override
	public void initPhysics() {
		final BodyDef jumpPad = new BodyDef();
		jumpPad.position.set(new Vector2(this.position.x + (this.dimension.x / 2f), this.position.y + (this.dimension.y / 2f)));
		jumpPad.type = BodyDef.BodyType.StaticBody;
		this.b2Body = this.b2World.createBody(jumpPad);
		final PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(this.dimension.x / 2f, this.dimension.y / 2f);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.friction = 0.5f;
		this.b2Body.createFixture(fixtureDef);
		polygonShape.dispose();
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (this.triggered) {
			batch.draw(this.textureUp, this.position.x, this.position.y, this.dimension.x, this.dimension.y * 1.5f);
		} else {
			batch.draw(this.textureDown, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		}

	}

	public void trigger() {
		this.triggered = true;
		this.startTime = 0;
	}

	public boolean isTriggered() {
		return this.triggered;
	}
}
