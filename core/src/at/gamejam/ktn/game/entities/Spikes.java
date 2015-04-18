package at.gamejam.ktn.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Spikes extends GameObject {

	private TextureRegion	texture;
	private Body			b2Body;
	private final World		b2World;

	public Spikes(final Vector2 position, final World b2World) {
		super();
		this.position = position;
		this.dimension = new Vector2(0.5f, 0.25f);
		this.b2World = b2World;
		this.init();
	}

	private void init() {
		this.texture = this.assets.findRegion("spikes");
		this.initPhysics();
	}

	private void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 2f), this.position.y + (this.dimension.y / 2f)));
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		this.b2Body = this.b2World.createBody(bodyDef);
		final PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(this.dimension.x / 2f, this.dimension.y / 2f);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setUserData(this);
	}

	@Override
	public void render(final SpriteBatch batch) {
		batch.draw(this.texture, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
	}
}
