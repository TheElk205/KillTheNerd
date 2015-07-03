package at.game.gamemechanics.movement;

import at.game.WorldController;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Herkt Kevin
 */
public class BodyFactory {
	private static World	world;
	private static boolean	initialized	= false;

	public static void init(final World world) {
		BodyFactory.world = world;
		BodyFactory.initialized = true;
	}

	public static Body createCircle() {

		final BodyDef testBodyDef2 = new BodyDef();
		testBodyDef2.position.set(2, 3);
		testBodyDef2.type = BodyDef.BodyType.DynamicBody;
		testBodyDef2.fixedRotation = true;
		final Body body2 = WorldController.getB2World().createBody(testBodyDef2);
		final CircleShape testShape2 = new CircleShape();
		testShape2.setRadius(1f);
		final FixtureDef testFixtureDef2 = new FixtureDef();
		testFixtureDef2.shape = testShape2;
		testFixtureDef2.density = 25f;
		testFixtureDef2.friction = 1f;
		testFixtureDef2.restitution = 0;
		body2.createFixture(testFixtureDef2);
		final MassData md = body2.getMassData();
		md.mass = 80; // kg
		body2.setMassData(md);
		// System.out.println("circle - mass: " + body2.getMassData().mass + " density: " + body2.getFixtureList().get(0).getDensity());
		testShape2.dispose();
		return body2;
	}

	public static Body createRect(final Vector2 position, final Vector2 renderDimension) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(position.x, position.y));
		bodyDef.type = BodyDef.BodyType.StaticBody;
		final Body b2Body = BodyFactory.world.createBody(bodyDef);

		final PolygonShape shape = new PolygonShape();
		shape.setAsBox(renderDimension.x / 2, renderDimension.y / 2);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = false;
		fixtureDef.density = 0f;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = shape;
		b2Body.createFixture(fixtureDef);

		return b2Body;
	}

	public static Body createNPCBody(final Vector2 position, final Vector2 renderDimension) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2((position.x) + (renderDimension.x / 2f), (position.y) + (renderDimension.y / 2f)));
		bodyDef.type = BodyDef.BodyType.StaticBody;
		final Body b2Body = BodyFactory.world.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.75f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = false;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = circleShape;
		b2Body.createFixture(fixtureDef);

		circleShape = new CircleShape();
		circleShape.setRadius(0.10f);
		fixtureDef = new FixtureDef();
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = circleShape;
		b2Body.createFixture(fixtureDef);
		return b2Body;
	}

	public static Body createItemBody(final Vector2 position, final Vector2 renderDimension) {
		final BodyDef bodyDef = new BodyDef();
		// System.out.println(this.dimension + " " + this.position);
		bodyDef.position.set(new Vector2((position.x) + (renderDimension.x / 2f), (position.y) + (renderDimension.y / 2f)));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		final Body b2Body = BodyFactory.world.createBody(bodyDef);
		final PolygonShape polygonShape = new PolygonShape();
		// System.out.println(this.dimension.x);
		polygonShape.setAsBox(renderDimension.x / 2f, renderDimension.y / 2f);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = false;
		fixtureDef.density = 5f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = polygonShape;
		b2Body.setBullet(true);
		b2Body.createFixture(fixtureDef);
		return b2Body;
	}

	/**
	 * @param position
	 * @param width
	 * @param height
	 * @return Body
	 */
	public static Body createPlayerPlatformerBody1(final Vector2 position, final float width, final float height) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		// create body with world

		final Body b2Body = BodyFactory.world.createBody(bodyDef);

		// create shape
		final PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width / 2f, height / 2f); // width: 90cm, heigth: 1,8meters ... div 2, because setAsBox(...) calcs in half sizes

		// create fixture to attach shape to body
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxShape;
		fixtureDef.density = 25f; // BMI of human?
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0;
		b2Body.createFixture(fixtureDef).setUserData("player user data");

		final MassData md = b2Body.getMassData();
		md.mass = 80; // kg
		b2Body.setMassData(md);
		boxShape.dispose(); // clean up!!

		// create foot sensor, used for jumping
		final PolygonShape shape = new PolygonShape();
		shape.setAsBox(height / 20, width / 10, new Vector2((+width / 20), -height / 2), 0);
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		b2Body.createFixture(fixtureDef).setUserData("foot");

		shape.setAsBox(height / 20, width / 10, new Vector2((-width / 20), -height / 2), 0);
		b2Body.createFixture(fixtureDef).setUserData("foot");

		shape.setAsBox(height / 20, width / 10, new Vector2((+width / 2), -height / 2), 0);
		b2Body.createFixture(fixtureDef).setUserData("outerFootRight");

		shape.setAsBox(height / 20, width / 10, new Vector2((-width / 2), -height / 2), 0);
		b2Body.createFixture(fixtureDef).setUserData("outerFootLeft");
		return b2Body;
	}

	/**
	 * @param position
	 * @return Body
	 */
	public static Body createPlayerPlatformerBody2(final Vector2 position) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		// create body with world
		final Body b2Body = BodyFactory.world.createBody(bodyDef);

		// create shape
		final PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.4f / 2f, 1.6f / 2f); // width: 50cm, heigth: 1,8meters ... div 2, because setAsBox(...) calcs in half sizes

		// create fixture to attach shape to body
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxShape;
		fixtureDef.density = 20f; // BMI of human?
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.1f;
		b2Body.createFixture(fixtureDef);

		final MassData md = b2Body.getMassData();
		md.mass = 60; // kg
		b2Body.setMassData(md);
		boxShape.dispose(); // clean up!!
		return b2Body;
	}

	// this.b2Body.setLinearDamping(1f);
	// this.b2Body.setBullet(true);

	// create foot sensor, used for jumping
	/*final PolygonShape shape = new PolygonShape();
	shape.setAsBox(80 / Constants.PPM, 100 / Constants.PPM, new Vector2(0, -this.dimension.y), 0);
	fixtureDef.shape = shape;
	fixtureDef.isSensor = true;
	this.b2Body.createFixture(fixtureDef).setUserData("foot");*/

}
