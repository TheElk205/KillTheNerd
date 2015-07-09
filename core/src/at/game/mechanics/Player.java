package at.game.mechanics;

import at.game.WorldController;
import at.game.enums.ItemType;
import at.game.mechanics.actions.FighterType;
import at.game.mechanics.enums.DirectionEnum;
import at.game.mechanics.enums.HumanStateEnum;
import at.game.mechanics.movement.BodyFactory;
import at.game.mechanics.movement.PlayerController;
import at.game.objects.AbstractGameObject;
import at.game.objects.EquippedWeapon;
import at.game.utils.Constants;
import at.game.visuals.hud.DelayBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author Herkt Kevin
 */
public class Player extends AbstractGameObject {
	private final FighterType		fighterType				= new FighterType();
	private boolean					onGround				= false;
	public final Vector2			velocity				= new Vector2();
	private final DelayBar			delayBar;												// could be changed to healthbar
	private Sound					sound;
	// private Vector<Item> items;
	private final int				maxItems				= 150;
	private int						itemCount				= Constants.START_ITEM_COUNT;
	private boolean					hitAbleAgain			= true;
	/** 5 meters per second */
	private final float				baseSpeed				= 4f;
	private final float				currentSpeed			= this.baseSpeed;
	private final float				throwingSpeed			= 100;
	private DirectionEnum			directionMoving;
	private DirectionEnum			directionLooking;
	private DirectionEnum			newestDirection;
	private String					name					= "Player";
	private final PlayerController	playerController;
	protected int					health					= 100;
	private double					timeSinceLastShoot		= 0;
	protected ItemType				itemType				= null;
	protected float					factor					= 0.0f;
	protected float					modifier				= 1f;							// 0: nichts geht mehr, 1: alles Normal, >1: Besser
	// protected float dealHandicap = 0.8f;
	// protected float handicapSetAt = 0.0f;
	// protected final float handicapDuration = 5f;
	protected float					stateTime				= 0.0f;
	protected Animation				aMoveUp, aMoveDown, aMoveLeft, aMoveRight;
	protected Animation				jump;
	protected TextureRegion			tRMoveUp[], tRMoveDown[], tRMoveLeft[], tRMoveRight[];
	protected TextureRegion			tRStandUp, tRStandDown, tRStandLeft, tRStandRight;
	protected TextureRegion			tUp, tDown, tLeft, tRight;
	private final AbstractRace		race					= new HumanRace();

	/* ----- Sensor counts ----- */
	private int						outerFootRightCount		= 0;
	private int						outerFootLeftCount		= 0;
	private int						footerCollidingCount	= 0;
	private int						wallCollidingCount		= 0;
	private int						headCollidingCount		= 0;

	/**
	 * @param position
	 * @param itemType
	 * @param width
	 * @param height
	 */
	public Player(final Vector2 position, final ItemType itemType, final float width, final float height) {
		super(BodyFactory.createPlayerPlatformerBody1(position, width, height, true), position, width, height);
		final Body weapon = BodyFactory.createTriangleSensorOnBody(position);

		final AbstractGameObject eqWeapon = new EquippedWeapon(weapon, this.getPosition(), 1, 1, this);
		weapon.setUserData(eqWeapon);

		BodyFactory.createJoint(this.getB2Body(), weapon);

		this.geometrics.getB2Body().setUserData(this);
		this.playerController = new PlayerController(this);
		this.delayBar = new DelayBar(this);
		this.init(position, true, true);
		this.itemType = itemType;
		// this.factor = this.factor;
		/*String prefix = "";
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
		this.loadPictures(prefix);*/
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

	protected void init(final Vector2 position, final boolean animation, final boolean initPhysics) {
		this.geometrics.setPosition(position);
		final FileHandle handle = Gdx.files.internal(Constants.THROW_SOUND);
		this.sound = Gdx.audio.newSound(handle);
		// this.items = new Vector<Item>();

		// this.dimension.set(20f, 1f);
		// this.dimension = new Vector2(0.75f, 0.75f);
		// this.directionMoving = DirectionEnum.STAY;
		this.directionLooking = DirectionEnum.S;
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
			// this.geometrics.setB2Body();
			this.geometrics.setRenderDimension(new Vector2(0.9f, 1.8f));
			this.geometrics.getB2Body().setUserData(this);

			System.out.println("Player: created at x:" + this.geometrics.getPosition().x + " y:" + this.geometrics.getPosition().y);
		}
	}

	public void setDirectionMoving(final DirectionEnum d) {
		this.directionMoving = d;
	}

	@Override
	public void render(final SpriteBatch batch) {
		/*this.delayBar.render(batch);
		if (this.toRender) {
			if (this.directionMoving == DirectionEnum.STAY) {
				switch (this.directionLooking) {
					case S:
						batch.draw(this.tRStandDown, this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					case N:
						batch.draw(this.tRStandUp, this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					case E:
						batch.draw(this.tRStandRight, this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					case W:
						batch.draw(this.tRStandLeft, this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y, this.dimension.x,
								this.dimension.y, this.scale.x, this.scale.y, this.rotation);
						break;
					default:
						break;
				}
			} else
				if (this.directionMoving != DirectionEnum.STAY) {
					switch (this.directionLooking) {
						case S:
							batch.draw(this.aMoveDown.getKeyFrame(this.time, true), this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						case N:
							batch.draw(this.aMoveUp.getKeyFrame(this.time, true), this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						case E:
							batch.draw(this.aMoveRight.getKeyFrame(this.time, true), this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						case W:
							batch.draw(this.aMoveLeft.getKeyFrame(this.time, true), this.geometrics.getPosition().x - (this.dimension.x / 2), this.geometrics.getPosition().y - (this.dimension.y / 2), this.origin.x, this.origin.y,
									this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, this.rotation);
							break;
						default:
							break;
					}
				}
		}*/

		if (this.race.getState() == HumanStateEnum.JUMPING) {
			// frame = this.jump.getKeyFrame(this.stateTime);
		}
	}

	@Override
	public void update(final float deltaTime) {
		if (deltaTime == 0) {
			return; // same time as before, nothing to update
		}
		this.stateTime += deltaTime; // overflow handling?
		this.playerController.processInput(this.currentSpeed, this.modifier);

		// this.otherMovement();

		this.throwItem(deltaTime); // TODO: move this method into PlayerController
		this.geometrics.update();
		// System.out.println("Player - position" + this.geometrics.getPosition() + " body position: " + this.geometrics.getB2Body().getPosition());
		// this.rotation = this.geometrics.getB2Body().getAngle() * MathUtils.radiansToDegrees;
		// this.checkCanBeHitAgain(deltaTime);
		/*if ((this.stateTime - this.handicapSetAt) > this.handicapDuration) {
			this.modifier = 1;
		}*/
		this.delayBar.update(deltaTime);
	}

	/*private void checkCanBeHitAgain(final float deltaTime) {
		if (((this.time - this.handicapSetAt) > (this.handicapDuration + 2))) {
			this.hitAbleAgain = true;
		}
	}*/

	public void setHitAbleAgain(final boolean b) {
		this.hitAbleAgain = b;
	}

	public boolean isHitAbleAgain() {
		return this.hitAbleAgain;
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

	private void throwItem(final float deltaTime) {
		this.timeSinceLastShoot = this.timeSinceLastShoot + deltaTime; // TODO: check if overflow
		if (this.timeSinceLastShoot >= (Double.MAX_VALUE - 1)) {
			throw new RuntimeException();
		}
		if ((this.itemCount > 0) && this.playerController.isPressingShoot() && (this.timeSinceLastShoot > 0.2f)) {
			this.sound.play();
			this.timeSinceLastShoot = 0;
			final Vector2 initPos = this.geometrics.getPosition();
			final Vector2 toApply = new Vector2();
			switch (this.directionLooking) {
				case N:
					toApply.y = this.throwingSpeed;
					initPos.y += (this.geometrics.getApproxB2Height() / 2f) + 0.1f;// 0.2f; // dont add because image is not in middle?
					// (this.dimension.y / 2) +
					// initPos.x -= 0.05f;
					break;
				case E:
					toApply.x = this.throwingSpeed;
					initPos.x += (this.geometrics.getApproxB2Width() / 2f) + 0.1f;// 0.2f; // dont add because image is not in middle?
					// (this.dimension.x / 2) +
					// initPos.y -= 0.1f;
					break;
				case S:
					toApply.y = -this.throwingSpeed;
					initPos.y -= (this.geometrics.getApproxB2Height() / 2f) + 0.1f;// (this.geometrics.getRenderDimension().y / 2); TODO -itemHeigth
					// initPos.x -= 0.05f;
					break;
				case W:
					toApply.x = -this.throwingSpeed;
					initPos.x -= (this.geometrics.getApproxB2Width() / 2f) + 0.1f;// (this.geometrics.getRenderDimension().x / 2); TODO -itemWidth
					// initPos.y -= 0.1f;
					break;
				default:
					break;
			}
			// System.out.println("throw spawn position: " + initPos + " playerPosition: " + this.geometrics.getPosition());
			if (this.itemType == ItemType.Wake_Item) {
				final Item bull = new Item(initPos, true, ItemType.Wake_Item, "Wake_Item");
				bull.getB2Body().setLinearVelocity(toApply);
				WorldController.addTempGameObject(bull);
				bull.setItemIsThrownBy(this);
				bull.setFlying();
				this.decrItemCount();
			} else
				if (this.itemType == ItemType.Sleep_Item) {
					// System.out.println("Player - throw item");
					final Item thesis = new Item(initPos, true, ItemType.Sleep_Item, "Sleep_Item");
					thesis.getB2Body().setLinearVelocity(toApply);
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

	/*public float getFactor() {
		return this.factor * this.modifier;
	}

	public void setHandicap(final float handicap) {
		this.modifier = Math.abs(handicap);
		this.handicapSetAt = this.stateTime;
	}*/

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

	/*public float getHandicapduration() {
		return this.handicapDuration;
	}*/

	public DirectionEnum getNewestDirection() {
		return this.newestDirection;
	}

	public void setNewestDirection(final DirectionEnum newestDirection) {
		this.newestDirection = newestDirection;
	}

	public DirectionEnum getDirectionLooking() {
		return this.directionLooking;
	}

	public void setDirectionLooking(final DirectionEnum directionLooking) {
		this.directionLooking = directionLooking;
	}

	public PlayerController getPlayerController() {
		return this.playerController;
	}

	public HumanStateEnum getPlayerState() {
		return this.race.getState();
	}

	public void setPlayerState(final HumanStateEnum playerState) {
		this.race.setState(playerState);
	}

	public AbstractRace getRace() {
		return this.race;
	}

	public boolean isOnGround() {
		return this.onGround;
	}

	public void setOnGround(final boolean onGround) {
		this.onGround = onGround;
	}

	public int getFooterCollidingCount() {
		return this.footerCollidingCount;
	}

	public void setFooterCollidingCount(final int footerCollidingCount) {
		this.footerCollidingCount = footerCollidingCount;
		if (this.footerCollidingCount == 0) {
			this.setOnGround(false);
			// dont set JUMPING state here, you cant know if player pressed it or not
			/*if (!this.race.getState().equals(HumanStateEnum.JUMPING)) {
				this.race.setState(HumanStateEnum.FALLING);
			}*/
			this.race.setState(HumanStateEnum.NOT_ON_GROUND);
		} else {
			this.setOnGround(true);
			this.race.setState(HumanStateEnum.WALKING);
		}
		// System.out.println(this.footerCollidingCount);
	}

	public void incrOuterFootRightCount() {
		this.outerFootRightCount++;
	}

	public void decrOuterFootRightCount() {
		this.outerFootRightCount--;
	}

	public void incrOuterFootLeftCount() {
		this.outerFootLeftCount++;
	}

	public void decrOuterFootLeftCount() {
		this.outerFootLeftCount--;
	}

	public int getOuterFootRightCount() {
		return this.outerFootRightCount;
	}

	public int getOuterFootLeftCount() {
		return this.outerFootLeftCount;
	}

	public void incrWall() {
		this.wallCollidingCount++;
	}

	public void decrWall() {
		this.wallCollidingCount--;
	}

	public int getWallColliding() {
		return this.wallCollidingCount;
	}

	public void incrHead() {
		this.headCollidingCount++;
	}

	public void decrHead() {
		this.headCollidingCount--;
	}

	public int getHeadColliding() {
		return this.headCollidingCount;
	}
}