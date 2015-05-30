package at.game.visuals;

import at.game.WorldController;
import at.game.enums.Direction;
import at.game.enums.ItemType;
import at.game.enums.PlayerType;
import at.game.utils.Constants;
import at.game.visuals.hud.DelayBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends InteractiveObject {
	private final DelayBar		delayBar;											// could be changed to healthbar
	private Sound				sound;
	// private Vector<Item> items;
	private final int			maxItems			= 15;
	private int					itemCount			= Constants.START_ITEM_COUNT;
	private int					points				= 0;
	private boolean				hitAbleAgain		= true;
	private final float			startSpeed			= 4f;
	private final float			currentSpeed		= this.startSpeed;
	private final float			throwingSpeed		= 100;
	private Direction			directionMoving;
	private Direction			directionLooking;
	private Direction			newestDirection;
	private String				name				= "Player";
	private boolean				right;
	private boolean				up;
	private boolean				left;
	private boolean				down;
	private boolean				shoot;
	protected int				health				= 100;
	private float				timeSinceLastShoot	= 0;
	protected ItemType			itemType			= null;
	protected float				factor				= 0.0f;
	protected float				handicap			= 1f;							// 0: nichts geht mehr, 1: alles Normal, >1: Besser
	protected float				dealHandicap		= 0.8f;
	protected float				handicapSetAt		= 0.0f;
	protected final float		handicapDuration	= 5f;
	protected float				time				= 0.0f;
	protected Animation			aMoveUp, aMoveDown, aMoveLeft, aMoveRight;
	protected TextureRegion		tRMoveUp[], tRMoveDown[], tRMoveLeft[], tRMoveRight[];
	protected TextureRegion		tRStandUp, tRStandDown, tRStandLeft, tRStandRight;
	protected TextureRegion		tUp, tDown, tLeft, tRight;
	private final PlayerType	playerType;

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

	public Player(final Vector2 position, final ItemType itemType, final int factor, final PlayerType playerType) {
		this.delayBar = new DelayBar(this);
		this.playerType = playerType;
		this.initConstructor(position);
		this.itemType = itemType;
		this.factor = factor;
		String prefix = "";
		switch (playerType) {
			case Wake:
				prefix = "george_";
				break;
			case Sleep:
				prefix = "betty_";
				break;
			default:
				break;
		}
		this.loadPictures(prefix);
	}

	protected void initConstructor(final Vector2 position) {
		this.position = position;
		final FileHandle handle = Gdx.files.internal(Constants.THROW_SOUND);
		this.sound = Gdx.audio.newSound(handle);
		this.init(true, true);
	}

	private void loadPictures(final String prefix) {
		TextureRegion up, down, left, right;
		up = GameObject.assets.findRegion(prefix + "back");
		down = GameObject.assets.findRegion(prefix + "front");
		left = GameObject.assets.findRegion(prefix + "left");
		right = GameObject.assets.findRegion(prefix + "right");
		this.setInitialPictures(up, down, left, right);
	}

	protected void init(final boolean animation, final boolean initPhysics) {
		// this.items = new Vector<Item>();

		this.dimension.set(0.75f, 0.75f);
		this.origin.x = 0;
		this.origin.y = 0;
		this.directionMoving = Direction.STAY;
		this.directionLooking = Direction.S;
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
			this.b2Body = WorldController.topDown_b2World.createBody(bodyDef);

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
		this.delayBar.render(batch);
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
		this.delayBar.update(deltaTime);
	}

	private void checkCanBeHitAgain(final float deltaTime) {
		if (((this.time - this.handicapSetAt) > (this.handicapDuration + 2))) {
			this.hitAbleAgain = true;
		}
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

		// final double maxDistance = 8f;
		// if (ldController.calcPossibleXPlayerDistance() <= maxDistance) {
		this.b2Body.setLinearVelocity(lin);
		this.b2Body.applyForceToCenter(toApply, true);
		/*} else {
			this.position.x = this.position.x - 1;
			this.position.y = this.position.y + 3;
			if (ldController.calcPossibleXPlayerDistance() >= maxDistance) {
				this.position.x = this.position.x + 1;
				this.position.y = this.position.y - 3;
			}
			// this.b2Body.setLinearVelocity(new Vector2(-lin.x * 3, -lin.y * 3));
		}*/
	}

	public boolean addItem(final Item item) {
		if (this.itemCount < this.maxItems) {
			// this.items.add(item);
			if (item.isCollectable()) {
				item.setCollectable(false);
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
			if (this.itemType == ItemType.Wake_Item) {
				final Item bull = new Item(initPos, true, ItemType.Wake_Item, "Wake_Item");
				bull.getB2Body().applyForceToCenter(toApply, true);
				WorldController.addTempGameObject(bull);
				bull.setItemIsThrownBy(this);
				bull.setFlying();
				this.decrItemCount();
			} else
				if (this.itemType == ItemType.Sleep_Item) {
					final Item thesis = new Item(initPos, true, ItemType.Sleep_Item, "Sleep_Item");
					thesis.getB2Body().applyForceToCenter(toApply, true);
					WorldController.addTempGameObject(thesis);
					thesis.setItemIsThrownBy(this);
					thesis.setFlying();
					this.decrItemCount();
				}

			// System.out.println("Item Thrown: " + itemCount);
		}
	}

	/**
	 * @param item
	 * @return returns true if player "dead"
	 */
	public boolean hitByItem(final Item item) {
		// System.out.println(this + " hit by " + item);
		/*if ((item instanceof RedBull) && (this instanceof PlayerSleep)) {
			// this.health = this.health - 50;
		}
		TODO: switch cases, because different items have different dmg else if(item instanceof Thesis && this instanceof PlayerWake) {
			health = health - 20;
		}*/

		if (this.health <= 0) {
			return true;
			// this.getBody().setActive(false);
			// WorldController.destroyBody(getBody());
		}
		return false;
	}

	public void setName(final String name) {
		this.name = name;
	}

	protected String getName() {
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
		this.setDirectionMoving(Direction.E);

		// }
	}

	public void setUp(final boolean b) {
		// if (!this.up) {
		this.up = b;
		if (b) {
			this.newestDirection = Direction.N;
		}
		this.setLooking();
		this.setDirectionMoving(Direction.N);

		// }
	}

	public void setLeft(final boolean b) {
		// if (!this.left) {
		this.left = b;
		if (b) {
			this.newestDirection = Direction.W;
		}
		this.setLooking();
		this.setDirectionMoving(Direction.W);

		// }
	}

	public void setDown(final boolean b) {
		// if (!this.down) {
		this.down = b;
		if (b) {
			this.newestDirection = Direction.S;
		}
		this.setLooking();
		this.setDirectionMoving(Direction.S);

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

	public float getHandicap() {
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

	public PlayerType getPlayerType() {
		return this.playerType;
	}
}
