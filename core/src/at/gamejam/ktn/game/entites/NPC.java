package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class NPC extends InteractiveObject {

	private float energy = 50;
	private float factor = 0.0f;
	private float energyTime = 0.5f; //Zeitlcihe änderung nach
	private double timeSinceLasttick = 0;
	
	private int state = 0; //-1 asleep, 1 awake
	
	/**
	 * @param startValue
	 *            0 awake, 100 sleeps
	 */
	public NPC(Vector2 position, World b2World, int startValue) {
		this.position = position;// super(position, b2World);
		this.dimension = new Vector2(1.5f, 1.5f);
		this.b2World = b2World;
		this.numPictures = 5;
		this.frameDuration = 0.25f;
		// this.numPictures = 5 * 13;
		// this.frameDuration = 0.15f;
		this.animated = true;
		if (this.animated) {
			this.loadAsset();
			this.initAnimated();
		}
		
		this.initPhysics();
	}
	

	@Override
	public void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 2f), this.position.y + (this.dimension.y / 2f)));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		this.b2Body = this.b2World.createBody(bodyDef);
		
		final CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.75f);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0;
		fixtureDef.shape = circleShape;
		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setUserData(this);
	}
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		this.startTime += deltaTime;
		timeSinceLasttick += deltaTime;
		if(timeSinceLasttick > energyTime) {
			timeSinceLasttick = 0;
			this.energy += factor;
			if(this.energy > 100) {
				this.energy = 100;
			}
			else if(this.energy< 0) {
				this.energy = 0;
			}
			if(state == -1 && energy >= 50) {
				state = 0;
			}
			if(state == 1 && energy <= 50) {
				state = 0;
			}
			if(energy == 0) {
				state = -1;
			}
			if(energy == 100) {
				state = 1;
			}
			//System.out.println("New Energy: " + energy);
		}

	}

	@Override
	public void render(final SpriteBatch batch) {
		// if (this.toRender) {
		batch.draw(this.animation.getKeyFrame(this.startTime, true), this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		// }
	}

	public void loadAsset() {
		this.texture = this.assets.findRegion("worker"); // worker
	}

	public void addFactor(float factor) {
		this.factor += factor;
		//System.out.println("New Factor" + factor);
	}
	
	public void resetFactor() {
		this.factor = 0;
	}
	
	public float getEnergy() {
		return energy;
	}
	
	public int getState() {
		return this.state;
	}
}
