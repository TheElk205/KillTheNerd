package at.gamejam.ktn.game.entites;

import java.util.Vector;

import at.gamejam.ktn.game.WorldController;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Player extends InteractiveObject {
	private Sound				sound;
	protected Vector<Item>		items;
	protected int				maxItems			= 15;
	private int					itemCount			= Constants.START_ITEM_COUNT;

	protected int				points				= 0;
	
	protected float				startSpeed			= 5f;
	protected float				currentSpeed		= this.startSpeed;

	protected float				throwingSpeed		= 200;

	protected World				b2World;
	protected WorldController	worldController;

	protected Direction			directionMoving;
	protected Direction			directionLooking;

	protected String			name				= "Player";
	private boolean				right;
	private boolean				up;
	private boolean				left;
	private boolean				down;
	private boolean				shoot;

	protected int				health				= 100;

	private float				timeSinceLastShoot	= 0;

	protected ItemType			itemType			= ItemType.REDBULL;

	protected float				factor				= 0.0f;
	protected float handicap = 1f;	//0: nix geht mehr, 1: alles Normal, >1: Besser
	protected float dealHandicap = 0.5f;
	
	protected float handicapSetAt = 0.0f;
	protected float handicapDuration = 5f;
	
	protected float time = 0.0f;
	
	protected Animation		aMoveUp,	aMoveDown,		aMoveLeft,		aMoveRight;
	protected TextureRegion tRMoveUp[], tRMoveDown[], 	tRMoveLeft[], 	tRMoveRight[];
	protected TextureRegion tRStandUp,	tRStandDown,	tRStandLeft,	tRStandRight;
	protected TextureRegion tUp,		tDown,			tLeft,			tRight;
	

	public enum ItemType {
		REDBULL, THESIS
	}

	protected void initAnimations() {
		TextureRegion[][] tmpUp = getSinglePictures(tUp, 4);
		this.tRMoveUp = getRegionMoving(tmpUp);
		this.aMoveUp = getAnimation(tRMoveUp, 0.2f);
		this.tRStandUp = tRMoveUp[0];
		
		TextureRegion[][] tmpDown = getSinglePictures(tDown, 4);
		this.tRMoveDown = getRegionMoving(tmpDown);
		this.aMoveDown = getAnimation(tRMoveDown, 0.2f);
		this.tRStandDown = tRMoveDown[0];
		
		TextureRegion[][] tmpLeft = getSinglePictures(tLeft, 4);
		this.tRMoveLeft = getRegionMoving(tmpLeft);
		this.aMoveLeft = getAnimation(tRMoveLeft, 0.2f);
		this.tRStandLeft = tRMoveLeft[0];
		
		TextureRegion[][] tmpRight = getSinglePictures(tRight, 4);
		this.tRMoveRight = getRegionMoving(tmpRight);
		this.aMoveRight = getAnimation(tRMoveRight, 0.2f);
		this.tRStandRight = tRMoveRight[0];
		
		if(aMoveUp == null) {
			System.out.println("Fehler");
		}
	}
	
	protected void setInitialPictures(TextureRegion tUp, TextureRegion tDown, TextureRegion tLeft, TextureRegion tRight) {
		this.tUp = tUp;
		this.tDown = tDown;
		this.tLeft = tLeft;
		this.tRight = tRight;
		if((tUp != null) && tDown != null && tLeft != null && tRight != null){
			System.out.println("Animationen richtig gesetzt");
		}
		initAnimations();
	}
	
	protected TextureRegion[][] getSinglePictures(TextureRegion originalPicture, int numPictures) {
		TextureRegion[][] tmp = null;
		try {
			if(originalPicture == null) {
				System.out.println("is null");
				return null;
			}
			tmp = originalPicture.split(originalPicture.getRegionWidth() / numPictures, originalPicture.getRegionHeight());
		} catch (Exception e) {
			int a;
			System.out.println("sad");
		}
		return tmp;
	}
	
	protected TextureRegion[] getRegionMoving(TextureRegion[][] pictures) {
		TextureRegion[] tmp = new TextureRegion[pictures[0].length];
		for (int j = 0; j < pictures[0].length; j++) {
			tmp[j] = pictures[0][j];
		}
		return tmp;
	}
	
	protected Animation getAnimation(TextureRegion[] pictures, float time) {
		return new Animation(time, pictures);
	}
	
//	protected void createAnimations (TextureRegion originalPicture, int numPictures, TextureRegion standing, TextureRegion[] moving, Animation animation) {
//		TextureRegion[][] tmp = null;
//		try {
//			if(originalPicture == null) {
//				System.out.println("is null");
//				return;
//			}
//			tmp = originalPicture.split(originalPicture.getRegionWidth() / numPictures, originalPicture.getRegionHeight());
//		} catch (Exception e) {
//			int a;
//			System.out.println("sad");
//		}
//		moving = new TextureRegion[numPictures];
//		int index = 0;
//		for (int j = 0; j < numPictures; j++) {
//			moving[index++] = tmp[0][j];
//		}
//		
//		standing = tmp[0][0];
//		if(standing == null) {
//			System.out.println("Statische NICHT gesetzt");
//		}
//		this.animation = new Animation(0.2f, moving);
//		System.out.println("Animation added");
//	}
	
	protected void initConstructor(final Vector2 position, final WorldController worldcontroller) {
		this.worldController = worldcontroller;
		this.b2World = this.worldController.getB2World();
		this.position = position;
		this.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.THROW_SOUND));
		this.init(true, true);
	}

	protected void init(boolean animation, boolean initPhysics) {
		this.items = new Vector<Item>();

		this.dimension.set(0.75f, 0.75f);
		this.origin.x = 0;
		this.origin.y = 0;
		// this.texture = this.assets.findRegion("player");
		this.directionMoving = Direction.STAY;
		this.directionLooking = Direction.S;
		this.loadAsset();
		if (initPhysics) {
			this.initPhysics();
		}
		//initAnimations();
	}

	@Override
	public void initPhysics() {
		if (!this.physicsAlreadyInit) {
			this.physicsAlreadyInit = true;
			// create body definition
			final BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.DynamicBody;
			bodyDef.position.set(this.position.x, this.position.y);

			// create body in world
			this.b2Body = this.b2World.createBody(bodyDef);

			// create shape
			final CircleShape circleShape = new CircleShape();
			circleShape.setRadius(this.dimension.x / 4);

			// create fixture to attach shape to body
			final FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circleShape;
			fixtureDef.density = 0f;
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 0;

			this.b2Body.createFixture(fixtureDef);
			this.b2Body.setLinearDamping(1f);
			this.b2Body.setBullet(false);

			circleShape.dispose(); // clean up!!
			this.b2Body.setUserData(this);
		}
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
			((Item) o).grabbedBy(this);
		} else
			if (o instanceof Pupil) {
				((Pupil) o).isNear(this);
			}
	}

	public void setDirectionMoving(final Direction d) {
		this.directionMoving = d;
//		if (d != Direction.STAY) {
//			this.directionLooking = d;
//		}
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (this.toRender) {
			if(this.directionMoving == Direction.STAY) {
				switch(this.directionLooking) {
				case S:
					batch.draw(this.tRStandDown, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				case N:
					batch.draw(this.tRStandUp, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				case E:
					batch.draw(this.tRStandRight, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				case W:
					batch.draw(this.tRStandLeft, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				default:
					break;
					
				}
			}
			else if(this.directionMoving != Direction.STAY) {
				switch(this.directionLooking) {
				case S:
					batch.draw(this.aMoveDown.getKeyFrame(time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				case N:
					batch.draw(this.aMoveUp.getKeyFrame(time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				case E:
					batch.draw(this.aMoveRight.getKeyFrame(time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				case W:
					batch.draw(this.aMoveLeft.getKeyFrame(time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x, this.dimension.y,
						this.scale.x, this.scale.y, this.rotation);
					break;
				default:
					break;
					
				}
			}
		}
	}

	@Override
	public void update(final float deltaTime) {
		this.time += deltaTime;
		
		this.move();
		this.throwItem(deltaTime);
		this.position = this.b2Body.getPosition();
		this.rotation = this.b2Body.getAngle() * MathUtils.radiansToDegrees;
		
		if(this.time - this.handicapSetAt > this.handicapDuration) {
			this.resetHandycap();
		}
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
			toApply.y = this.currentSpeed*this.handicap;
		}
		if (this.down) {
			toApply.y = -(this.currentSpeed*this.handicap);
		}
		if (this.right) {
			toApply.x = this.currentSpeed*this.handicap;
		}
		if (this.left) {
			toApply.x = -this.currentSpeed*this.handicap;
		}
		// System.out.println("currentSpeed: " + this.currentSpeed);
		this.b2Body.applyForceToCenter(toApply, true);
	}

	public boolean addItem(final Item item) {
		if (this.itemCount < this.maxItems) {
			// this.items.add(item);
			if (item.collectable) {
				item.collectable = false;
				this.incrItemCount();
				// this.itemCount++;
				// System.out.println("Got an Item! " + this.itemCount);
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

	public void throwItem(float deltaTime) {
		this.timeSinceLastShoot += deltaTime;
		if ((this.itemCount > 0) && this.shoot && (this.timeSinceLastShoot > 0.2f)) {
			this.sound.play();
			this.timeSinceLastShoot = 0;
			Vector2 initPos = this.position;
			Vector2 toApply = new Vector2();
			initPos.x -= this.dimension.x / 2f;
			initPos.y -= this.dimension.x / 2f;

			float offset = 0.0f;
			switch (this.directionLooking) {
				case N:
					toApply.y = this.throwingSpeed;
					initPos.y += this.dimension.y + offset;
					break;
				case S:
					toApply.y = -this.throwingSpeed;
					initPos.y -= (this.dimension.y + offset);
					break;
				case E:
					toApply.x = this.throwingSpeed;
					initPos.x += this.dimension.x + offset;
					break;
				case W:
					toApply.x = -this.throwingSpeed;
					initPos.x -= (this.dimension.x + offset);
					break;
				default:
					break;
			}
			if (this.itemType == ItemType.REDBULL) {
				RedBull bull = new RedBull(initPos, this.b2World, true);
				bull.getB2Body().applyForceToCenter(toApply, true);
				this.worldController.addTempGameObject(bull);
				bull.itemIsThrownBy = this;
				// this.worldController.redBullCount--;
				this.decrItemCount();
				bull.collectable = false;
				bull.isFlying = true;
			} else
				if (this.itemType == ItemType.THESIS) {
					Thesis thesis = new Thesis(initPos, this.b2World, true);
					thesis.getB2Body().applyForceToCenter(toApply, true);
					this.worldController.addTempGameObject(thesis);
					thesis.itemIsThrownBy = this;
					// this.worldController.coinCount--;
					this.decrItemCount();
					thesis.collectable = false;
					thesis.isFlying = true;
				}

			// System.out.println("Item Thrown: " + itemCount);
		}
	}

	/*public Body getBody() {
		return this.b2Body;
	}*/

	/**
	 * @param item
	 * @return returns true if player "dead"
	 */
	public boolean hitByItem(Item item) {
		// System.out.println(this + " hit by " + item);
		if ((item instanceof RedBull) && (this instanceof PlayerSleep)) {
			//this.health = this.health - 50;
		}
		/*TODO: else if(item instanceof Thesis && this instanceof PlayerWake) {
			health = health - 20;
		}*/

		if (this.health <= 0) {
			return true;
			// this.getBody().setActive(false);
			// this.b2World.destroyBody(getBody());
		}
		return false;
	}

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
		setLooking();
		this.setDirectionMoving(Player.Direction.E);
	}

	public void setUp(final boolean b) {
		this.up = b;
		setLooking();
		this.setDirectionMoving(Player.Direction.N);
	}

	public void setLeft(final boolean b) {
		this.left = b;
		setLooking();
		this.setDirectionMoving(Player.Direction.W);
	}

	public void setDown(final boolean b) {
		this.down = b;
		setLooking();
		this.setDirectionMoving(Player.Direction.S);
	}
	
	public void setLooking() {
		if(this.up){
			this.directionLooking = Direction.N;
		}
		if(this.down) {
			this.directionLooking = Direction.S;
		}
		if(this.left) {
			this.directionLooking = Direction.W;
		}
		if(this.right) {
			this.directionLooking = Direction.E;
		}
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

	public void setShoot(boolean b) {
		this.shoot = b;
	}

	public boolean getShoot() {
		return this.shoot;
	}

	public float getFactor() {
		return this.factor*handicap;
	}
	
	public void setHandicap(float handicap) {
		this.handicap = Math.abs(handicap);
		this.handicapSetAt = this.time;
	}
	
	public void resetHandycap() {
		this.handicap = 1;
	}
	
	public float dealHandicap() {
		return this.dealHandicap;
	}
	private void incrItemCount() {
		this.itemCount++;
	}

	private void decrItemCount() {
		this.itemCount--;
	}

	/**
	 * @return the itemCount
	 */
	public int getItemCount() {
		return this.itemCount;
	}
	
	public float getHandicapduration() {
		return this.handicapDuration;
	}
	
	public float getHandicap() {
		return this.handicap;
	}
}
