package at.game.visuals;

import java.util.Random;

import at.game.WorldController;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class NPC extends InteractiveObject {
	private static int		count				= 0;
	private final int		number;
	private float			energy				= 50;
	// private float factor = 0.0f;
	private final float		energyTime			= 0.5f;
	private double			timeSinceLasttick	= 0;
	private int				state				= 0;	// -1 asleep, 1 awake
	private final Player[]	areNear;

	/**
	 * @param position
	 */
	public NPC(final Vector2 position) {
		this.position = position;
		this.dimension = new Vector2(1.5f, 1.5f);
		this.numPictures = 5;

		final Random rnd = new Random();
		final int min = 60, max = 120;
		// int i = (int) ((Math.random() * 120) + 1);
		final int i = rnd.nextInt((max - min) + 1) + min;

		this.frameDuration = (float) i / 100;
		// this.numPictures = 5 * 13;
		// this.frameDuration = 0.15f;
		this.animated = true;
		if (this.animated) {
			this.loadAsset();
			this.initAnimated();
		}

		this.areNear = new Player[2];
		this.areNear[0] = null;
		this.areNear[1] = null;
		NPC.count++;
		this.number = NPC.count;
		this.initPhysics();
	}

	@Override
	public void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 2f), this.position.y + (this.dimension.y / 2f)));
		bodyDef.type = BodyDef.BodyType.StaticBody;
		this.b2Body = WorldController.topDown_b2World.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.75f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = circleShape;
		this.b2Body.createFixture(fixtureDef);

		circleShape = new CircleShape();
		circleShape.setRadius(0.10f);
		fixtureDef = new FixtureDef();
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = circleShape;
		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setUserData(this);
	}

	@Override
	public void update(final float deltaTime) {
		this.startTime += deltaTime;
		this.timeSinceLasttick += deltaTime;
		if (this.timeSinceLasttick > this.energyTime) {
			this.timeSinceLasttick = 0;
			float f1 = 0.0f;
			if (this.areNear[0] != null) {
				f1 = this.areNear[0].getFactor();
			}
			float f2 = 0.0f;
			if (this.areNear[1] != null) {
				f2 = this.areNear[1].getFactor();
			}
			this.energy += f1 + f2;
			if (this.energy > 100) {
				this.energy = 100;
			} else
				if (this.energy < 0) {
					this.energy = 0;
				}
			if ((this.state == -1) && (this.energy >= 50)) {
				this.state = 0;
			}
			if ((this.state == 1) && (this.energy <= 50)) {
				this.state = 0;
			}
			if (this.energy == 0) {
				this.state = -1;
			}
			if (this.energy == 100) {
				this.state = 1;
			}
			// System.out.println("New Energy: " + energy);
		}

	}

	@Override
	public void render(final SpriteBatch batch) {
		// if (this.toRender) {
		batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		// }
	}

	public void loadAsset() {
		this.texture = GameObject.assets.findRegion("worker"); // worker
	}

	/*public void addFactor(final float factor) {
		this.factor += factor;
		// System.out.println("New Factor" + factor);
	}

	public void resetFactor() {
		this.factor = 0;
	}*/

	public float getEnergy() {
		return this.energy;
	}

	public int getState() {
		return this.state;
	}

	public void addPlayer(final Player player) {
		if (this.areNear[0] != null) {
			if (this.areNear[1] != null) {
				return;
			}
			this.areNear[1] = player;
			return;
		}
		this.areNear[0] = player;
	}

	public void removePlayer(final Player player) {
		if (this.areNear[0] == player) {
			this.areNear[0] = this.areNear[1];
			this.areNear[1] = null;
		}
		if (this.areNear[1] == player) {
			this.areNear[1] = null;
		}
	}

	@Override
	public String toString() {
		return "NPC-" + this.number;
	}
}
