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
	private float				lastHitTime			= 0;
	private boolean				hitAbleAgain		= true;
	protected float				startSpeed			= 4f;
	protected float				currentSpeed		= this.startSpeed;
	protected float				throwingSpeed		= 100;
	protected World				b2World;
	protected WorldController	worldController;
	private Direction			directionMoving;
	protected Direction			directionLooking;
	private Direction			newestDirection;
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
	protected float				handicap			= 1f;							// 0: nix geht mehr, 1: alles Normal, >1: Besser
	protected float				dealHandicap		= 0.8f;
	protected float				handicapSetAt		= 0.0f;
	protected float				handicapDuration	= 5f;
	protected float				time				= 0.0f;
	protected Animation			aMoveUp, aMoveDown, aMoveLeft, aMoveRight;
	protected TextureRegion		tRMoveUp[], tRMoveDown[], tRMoveLeft[], tRMoveRight[];
	protected TextureRegion		tRStandUp, tRStandDown, tRStandLeft, tRStandRight;
	protected TextureRegion		tUp, tDown, tLeft, tRight;

	public enum ItemType {
		REDBULL, THESIS
	}

	protected void initAnimations() {
		final TextureRegion[][] tmpUp = Player.getSinglePictures(this.tUp, 4);
		this.tRMoveUp = Player.getRegionMoving(tmpUp);
		this.aMoveUp = Player.getAnimation(this.tRMoveUp, 0.2f);
		this.tRStandUp = this.tRMoveUp[0];

		final TextureRegion[][] tmpDown = Player.getSinglePictures(this.tDown, 4);
		this.tRMoveDown = Player.getRegionMoving(tmpDown);
		this.aMoveDown = Player.getAnimation(this.tRMoveDown, 0.2f);
		this.tRStandDown = this.tRMoveDown[0];

		final TextureRegion[][] tmpLeft = Player.getSinglePictures(this.tLeft, 4);
		this.tRMoveLeft = Player.getRegionMoving(tmpLeft);
		this.aMoveLeft = Player.getAnimation(this.tRMoveLeft, 0.2f);
		this.tRStandLeft = this.tRMoveLeft[0];

		final TextureRegion[][] tmpRight = Player.getSinglePictures(this.tRight, 4);
		this.tRMoveRight = Player.getRegionMoving(tmpRight);
		this.aMoveRight = Player.getAnimation(this.tRMoveRight, 0.2f);
		this.tRStandRight = this.tRMoveRight[0];

		if (this.aMoveUp == null) {
			System.out.println("Fehler");
		}
	}

	protected void setInitialPictures(final TextureRegion tUp, final TextureRegion tDown, final TextureRegion tLeft, final TextureRegion tRight) {
		this.tUp = tUp;
		this.tDown = tDown;
		this.tLeft = tLeft;
		this.tRight = tRight;
		this.initAnimations();
	}

	protected static TextureRegion[][] getSinglePictures(final TextureRegion originalPicture, final int numPictures) {
		TextureRegion[][] tmp = null;
		if (originalPicture == null) {
			return null;
		}
		tmp = originalPicture.split(originalPicture.getRegionWidth() / numPictures, originalPicture.getRegionHeight());
		return tmp;
	}

	protected static TextureRegion[] getRegionMoving(final TextureRegion[][] pictures) {
		final TextureRegion[] tmp = new TextureRegion[pictures[0].length];
		for (int j = 0; j < pictures[0].length; j++) {
			tmp[j] = pictures[0][j];
		}
		return tmp;
	}

	protected static Animation getAnimation(final TextureRegion[] pictures, final float time) {
		return new Animation(time, pictures);
	}

	// protected void createAnimations (TextureRegion originalPicture, int numPictures, TextureRegion standing, TextureRegion[] moving, Animation
	// animation) {
	// TextureRegion[][] tmp = null;
	// try {
	// if(originalPicture == null) {
	// System.out.println("is null");
	// return;
	// }
	// tmp = originalPicture.split(originalPicture.getRegionWidth() / numPictures, originalPicture.getRegionHeight());
	// } catch (Exception e) {
	// int a;
	// System.out.println("sad");
	// }
	// moving = new TextureRegion[numPictures];
	// int index = 0;
	// for (int j = 0; j < numPictures; j++) {
	// moving[index++] = tmp[0][j];
	// }
	//
	// standing = tmp[0][0];
	// if(standing == null) {
	// System.out.println("Statische NICHT gesetzt");
	// }
	// this.animation = new Animation(0.2f, moving);
	// System.out.println("Animation added");
	// }

	protected void initConstructor(final Vector2 position, final WorldController worldcontroller) {
		this.worldController = worldcontroller;
		this.b2World = this.worldController.getB2World();
		this.position = position;
		this.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.THROW_SOUND));
		this.init(true, true);
	}

	protected void init(final boolean animation, final boolean initPhysics) {
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
		// initAnimations();
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
			fixtureDef.restitution = -10;

			this.b2Body.createFixture(fixtureDef);
			this.b2Body.setLinearDamping(1f);
			this.b2Body.setBullet(true);

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

	private void setDirectionMoving(final Direction d) {
		this.directionMoving = d;
		// if (d != Direction.STAY) {
		// this.directionLooking = d;
		// }
	}

	@Override
	public void render(final SpriteBatch batch) {
		if (this.toRender) {
			if (this.directionMoving == Direction.STAY) {
				switch (this.directionLooking) {
					case S:
						batch.draw(this.tRStandDown, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					case N:
						batch.draw(this.tRStandUp, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					case E:
						batch.draw(this.tRStandRight, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					case W:
						batch.draw(this.tRStandLeft, this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					default:
						break;

				}
			} else
				if (this.directionMoving != Direction.STAY) {
					switch (this.directionLooking) {
						case S:
							batch.draw(this.aMoveDown.getKeyFrame(this.time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						case N:
							batch.draw(this.aMoveUp.getKeyFrame(this.time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						case E:
							batch.draw(this.aMoveRight.getKeyFrame(this.time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						case W:
							batch.draw(this.aMoveLeft.getKeyFrame(this.time, true), this.position.x - (this.dimension.x / 2), this.position.y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
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
		this.checkCanBeHitAgain(deltaTime);
		if ((this.time - this.handicapSetAt) > this.handicapDuration) {
			this.resetHandycap();
		}
	}

	private void checkCanBeHitAgain(final float deltaTime) {
		this.lastHitTime += deltaTime;
		if ((this.lastHitTime > (this.handicapDuration + 2)) && !this.hitAbleAgain) {
			this.lastHitTime = 0f;
			this.hitAbleAgain = true;
			// System.out.println(this.name + " is hit able again");
		}
		return;
	}

	public void setHitAbleAgain(final boolean b) {
		this.hitAbleAgain = b;
	}

	public boolean isHitAbleAgain() {
		return this.hitAbleAgain;
	}

	public void move() {
		final Vector2 toApply = new Vector2();
		final Vector2 lin = new Vector2();
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

		// if ((this.up && (this.newestDirection == Direction.N)) || (this.up && !this.left && !this.right)
		if (this.up) { // N
			lin.y = Math.round((this.currentSpeed / 1.5)) * this.handicap;
			toApply.y = this.currentSpeed * this.handicap;
		} else {
			if (this.down) { // S
				lin.y = -Math.round((this.currentSpeed / 1.5)) * this.handicap;
				toApply.y = -(this.currentSpeed * this.handicap);
			} else {
				lin.y = 0;
				// this.b2Body.setLinearVelocity(this.b2Body.getLinearVelocity().x, 0);
				toApply.y = 0;
			}
		}

		if (this.right) { // E
			lin.x = Math.round((this.currentSpeed / 1.5)) * this.handicap;
			toApply.x = this.currentSpeed * this.handicap;
		} else {
			if (this.left) { // W
				lin.x = -Math.round((this.currentSpeed / 1.5)) * this.handicap;
				// this.b2Body.setLinearVelocity(0, this.b2Body.getLinearVelocity().y);
				toApply.x = -this.currentSpeed * this.handicap;
			} else {
				lin.x = 0;
				// this.b2Body.setLinearVelocity(0, this.b2Body.getLinearVelocity().y);
				toApply.x = 0;
			}
		}
		// if ((this.right && (this.newestDirection == Direction.E)) || (this.right && !this.up && !this.down)) {

		// System.out.println("currentSpeed: " + this.currentSpeed);
		this.b2Body.setLinearVelocity(lin);
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

	private void throwItem(final float deltaTime) {
		this.timeSinceLastShoot += deltaTime; // TODO: check if overflow
		if ((this.itemCount > 0) && this.shoot && (this.timeSinceLastShoot > 0.2f)) {
			this.sound.play();
			this.timeSinceLastShoot = 0;
			final Vector2 initPos = this.position;
			final Vector2 toApply = new Vector2();
			initPos.x = this.position.x;
			initPos.y = this.position.y;

			switch (this.directionLooking) {
				case N:
					toApply.y = this.throwingSpeed;
					initPos.y += 0.2f; // dont add because image is not in middle? (this.dimension.y / 2) +
					initPos.x -= 0.05f;
					break;
				case E:
					toApply.x = this.throwingSpeed;
					initPos.x += 0.2f; // dont add because image is not in middle? (this.dimension.x / 2) +
					initPos.y -= 0.1f;
					break;
				case S:
					toApply.y = -this.throwingSpeed;
					initPos.y -= (this.dimension.y / 2);
					initPos.x -= 0.05f;
					break;
				case W:
					toApply.x = -this.throwingSpeed;
					initPos.x -= (this.dimension.x / 2);
					initPos.y -= 0.1f;
					break;
				default:
					break;
			}
			// System.out.println("throw spawn position: " + initPos + " playerPosition: " + this.position);
			if (this.itemType == ItemType.REDBULL) {
				final RedBull bull = new RedBull(initPos, this.b2World, true);
				bull.getB2Body().applyForceToCenter(toApply, true);
				this.worldController.addTempGameObject(bull);
				bull.itemIsThrownBy = this;
				bull.collected = false;
				this.decrItemCount();
				bull.collectable = false;
				bull.isFlying = true;
			} else
				if (this.itemType == ItemType.THESIS) {
					final Thesis thesis = new Thesis(initPos, this.b2World, true);
					thesis.getB2Body().applyForceToCenter(toApply, true);
					this.worldController.addTempGameObject(thesis);
					thesis.itemIsThrownBy = this;
					thesis.collected = false;
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
	public boolean hitByItem(final Item item) {
		// System.out.println(this + " hit by " + item);
		if ((item instanceof RedBull) && (this instanceof PlayerSleep)) {
			// this.health = this.health - 50;
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

	@Override
	public String toString() {
		return this.name;
	}

	public void setRight(final boolean b) {
		// if (!this.right) {
		this.right = b;
		if (b) {
			this.newestDirection = Direction.E;
		}
		this.setLooking();
		this.setDirectionMoving(Player.Direction.E);

		// }
	}

	public void setUp(final boolean b) {
		// if (!this.up) {
		this.up = b;
		if (b) {
			this.newestDirection = Direction.N;
		}
		this.setLooking();
		this.setDirectionMoving(Player.Direction.N);

		// }
	}

	public void setLeft(final boolean b) {
		// if (!this.left) {
		this.left = b;
		if (b) {
			this.newestDirection = Direction.W;
		}
		this.setLooking();
		this.setDirectionMoving(Player.Direction.W);

		// }
	}

	public void setDown(final boolean b) {
		// if (!this.down) {
		this.down = b;
		if (b) {
			this.newestDirection = Direction.S;
		}
		this.setLooking();
		this.setDirectionMoving(Player.Direction.S);

		// }
	}

	private void setLooking() {
		if ((this.up && (this.newestDirection == Direction.N)) || (this.up && !this.left && !this.right)) {
			this.directionLooking = Direction.N;
		}
		if ((this.down && (this.newestDirection == Direction.S)) || (this.down && !this.left && !this.right)) {
			this.directionLooking = Direction.S;
		}
		if ((this.left && (this.newestDirection == Direction.W)) || (this.left && !this.up && !this.down)) {
			this.directionLooking = Direction.W;
		}
		if ((this.right && (this.newestDirection == Direction.E)) || (this.right && !this.up && !this.down)) {
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

	public void setShoot(final boolean b) {
		this.shoot = b;
	}

	public boolean getShoot() {
		return this.shoot;
	}

	public float getFactor() {
		return this.factor * this.handicap;
	}

	protected float getHandicap() {
		return this.handicap;
	}

	public void setHandicap(final float handicap) {
		this.handicap = Math.abs(handicap);
		this.handicapSetAt = this.time;
	}

	public void resetHandycap() {
		this.handicap = 1;
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
}
