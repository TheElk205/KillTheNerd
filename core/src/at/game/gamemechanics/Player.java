package at.game.gamemechanics;

import at.game.WorldController;
import at.game.enums.ItemType;
import at.game.gamemechanics.enums.DirectionEnum;
import at.game.gamemechanics.enums.HumanStateEnum;
import at.game.gamemechanics.movement.BodyFactory;
import at.game.gamemechanics.movement.PlayerController;
import at.game.utils.Constants;
import at.game.visuals.InteractiveObject;
import at.game.visuals.hud.DelayBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Herkt Kevin
 */
public class Player extends InteractiveObject {
	private int						outerFootRightCount		= 0;
	private int						outerFootLeftCount		= 0;
	private int						footerCollidingCount	= 0;
	private boolean					onGround				= false;
	public final Vector2			velocity				= new Vector2();
	private final DelayBar			delayBar;												// could be changed to healthbar
	private Sound					sound;
	// private Vector<Item> items;
	private final int				maxItems				= 15;
	private int						itemCount				= Constants.START_ITEM_COUNT;
	private boolean					hitAbleAgain			= true;
	/** 5 meters per second */
	private final float				baseSpeed				= 5f;
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
	protected float					dealHandicap			= 0.8f;
	protected float					handicapSetAt			= 0.0f;
	protected final float			handicapDuration		= 5f;
	protected float					stateTime				= 0.0f;
	protected Animation				aMoveUp, aMoveDown, aMoveLeft, aMoveRight;
	protected Animation				jump;
	protected TextureRegion			tRMoveUp[], tRMoveDown[], tRMoveLeft[], tRMoveRight[];
	protected TextureRegion			tRStandUp, tRStandDown, tRStandLeft, tRStandRight;
	protected TextureRegion			tUp, tDown, tLeft, tRight;
	private final AbstractRace		race					= new HumanRace();

	/**
	 * @param position
	 * @param itemType
	 */
	public Player(final Vector2 position, final ItemType itemType) {
		this.playerController = new PlayerController(this);
		this.delayBar = new DelayBar(this);
		this.initConstructor(position);
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

	protected void initConstructor(final Vector2 position) {
		this.position = position;
		final FileHandle handle = Gdx.files.internal(Constants.THROW_SOUND);
		this.sound = Gdx.audio.newSound(handle);
		this.init(true, true);
	}

	/*private void loadPictures(final String prefix) {
		TextureRegion up, down, left, right;
		up = GameObject.assets.findRegion(prefix + "back");
		down = GameObject.assets.findRegion(prefix + "front");
		left = GameObject.assets.findRegion(prefix + "left");
		right = GameObject.assets.findRegion(prefix + "right");
		this.setInitialPictures(up, down, left, right);
	}*/

	protected void init(final boolean animation, final boolean initPhysics) {
		// this.items = new Vector<Item>();

		// this.dimension.set(20f, 1f);
		// this.dimension = new Vector2(0.75f, 0.75f);
		this.origin.x = 0;
		this.origin.y = 0;
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
			this.b2Body = BodyFactory.createPlayerPlatformerBody1(this.position, 0.9f, 1.8f);
			this.renderDimension = new Vector2(0.9f, 1.8f);
			this.b2Body.setUserData(this);

			System.out.println("Player: created at x:" + this.position.x + " y:" + this.position.y);
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
				if (this.directionMoving != DirectionEnum.STAY) {
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

		otherMovement();

		this.throwItem(deltaTime); // TODO: move this method into PlayerController
		this.position = this.b2Body.getPosition();
		// System.out.println("Player - position" + this.position + " body position: " + this.b2Body.getPosition());
		// this.rotation = this.b2Body.getAngle() * MathUtils.radiansToDegrees;
		// this.checkCanBeHitAgain(deltaTime);
		if ((this.stateTime - this.handicapSetAt) > this.handicapDuration) {
			this.modifier = 1;
		}
		this.delayBar.update(deltaTime);
	}

	/**
	 * 
	 */
	public void otherMovement() {
		final Vector2 lin = new Vector2(this.b2Body.getLinearVelocity().x, this.b2Body.getLinearVelocity().y);
		// System.out.println(b2Body.getLinearVelocity());
		// wenn Spieler "schwebend" am Abgrund stehen würde, Spieler runter schubsen
		final boolean prevStateWalking = this.getRace().getPrevState().equals(HumanStateEnum.WALKING);
		if (prevStateWalking) {
			if ((this.getOuterFootLeftCount() > 0) && (this.getOuterFootRightCount() == 0) && (this.b2Body.getLinearVelocity().x < 1)) {
				lin.x += 1;
				System.out.println("push Player to right");
			} else
				if ((this.getOuterFootRightCount() > 0) && (this.getOuterFootLeftCount() == 0) && (this.b2Body.getLinearVelocity().x > -1)) {
					lin.x -= 1;
					System.out.println("push Player to left");
				}
		}
		this.b2Body.setLinearVelocity(lin);
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

	/*public void stop() {
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		this.b2Body.setLinearVelocity(new Vector2(0, 0));
		this.b2Body.setAngularVelocity(0);
		this.directionMoving = DirectionEnum.STAY;
	}*/

	private void throwItem(final float deltaTime) {
		this.timeSinceLastShoot = this.timeSinceLastShoot + deltaTime; // TODO: check if overflow
		if (this.timeSinceLastShoot >= (Double.MAX_VALUE - 1)) {
			throw new RuntimeException();
		}
		if ((this.itemCount > 0) && this.playerController.isPressingShoot() && (this.timeSinceLastShoot > 0.2f)) {
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
					initPos.y -= (this.renderDimension.y / 2);
					initPos.x -= 0.05f;
					break;
				case W:
					toApply.x = -this.throwingSpeed;
					initPos.x -= (this.renderDimension.x / 2);
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
					// System.out.println("Player - throw item");
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

	public float getFactor() {
		return this.factor * this.modifier;
	}

	public void setHandicap(final float handicap) {
		this.modifier = Math.abs(handicap);
		this.handicapSetAt = this.stateTime;
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

}
