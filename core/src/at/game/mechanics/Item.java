package at.game.mechanics;

import java.util.ArrayList;
import java.util.List;

import at.game.enums.ItemType;
import at.game.mechanics.movement.BodyFactory;
import at.game.objects.AbstractGameObject;
import at.game.objects.AnimationContainer;
import at.game.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Herkt Kevin
 */
public class Item extends AbstractGameObject {
	private static final Sound	GRAB_SOUND		= Gdx.audio.newSound(Gdx.files.internal(Constants.GRAB_SOUND));
	private boolean				collected		= false;
	private boolean				collectable		= true;
	private Player				grabbedBy;
	private Player				itemIsThrownBy	= null;
	private final float			dmg				= 0.05f;
	// public boolean destroyed = false;
	private static List<Item>	allItems		= new ArrayList<Item>();
	private float				flyingTime		= 0;
	private boolean				isFlying		= false;
	private String				name			= "no Name";
	private final ItemType		itemType;

	/**
	 * @param position
	 * @param initPhysics
	 * @param itemType
	 * @param name
	 */
	public Item(final Vector2 position, final boolean initPhysics, final ItemType itemType, final String name) {
		super(BodyFactory.createItemBody(position, new Vector2(0.15f, 0.15f)), position, 0.15f, 0.15f);
		this.itemType = itemType;
		switch (itemType) {
			case Wake_Item:
				this.animationCon = new AnimationContainer(6, "cup_coffee");
				break;
			case Sleep_Item:
				this.animationCon = new AnimationContainer(6, "book_green");
				break;
			default:
				break;
		}
		this.name = name;
		// this.geometrics.setPosition(position);
		Item.allItems.add(this);
		// this.dimension = new Vector2(0.25f, 0.2f);
		// this.geometrics.setRenderDimension(new Vector2(0.15f, 0.15f));
		// this.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.THROW_SOUND));
		if (initPhysics) {
			this.init(false, initPhysics); // TODO changed to false
		}
	}

	protected void init(final boolean animated, final boolean initPhysics) {
		this.animated = animated;
		this.collected = false;
		/*if (this.animated) {
			animationCon.initAnimated();
		} else {

		}*/
		if (initPhysics) {
			this.initPhysics();
		}
	}

	protected void setFlying() {
		this.collected = false;
		this.collectable = false;
		this.isFlying = true;
	}

	/**
	 * sets the item to its default state.
	 */
	public void reset() {
		this.isFlying = false;
		this.collectable = true;
		this.collected = false;
	}

	/*protected void init(final boolean animated, final boolean physics) {
		this.animated = animated;
		this.collected = false;
		if (this.animated) {
			this.initAnimated();
		}
		if (physics) {
			this.initPhysics();
		}
	}*/

	@Override
	public void initPhysics() {
		if (!this.physicsAlreadyInit) {
			this.physicsAlreadyInit = true;
			// this.geometrics.setB2Body());
			this.geometrics.getB2Body().setUserData(this);
		}
	}

	@Override
	public void render(final SpriteBatch batch) {
		if ((this.animationCon != null) && !this.collected && this.toRender) {
			batch.draw(this.animationCon.getKeyFrame(), this.geometrics.getPosition().x, this.geometrics.getPosition().y,
					this.geometrics.getRenderDimension().x, this.geometrics.getRenderDimension().y);
		}
		/*if ((this.animationCon == null) && !this.collectable && this.toRender) {
			// first method is with additional params: origin and scale
			// batch.draw(this.texture, this.geometrics.getPosition().x - (this.geometrics.getRenderDimension().x / 2),
			// this.geometrics.getPosition().y -
			// (this.geometrics.getRenderDimension().y / 2), this.origin.x,
			// this.origin.y, this.geometrics.getRenderDimension().x, this.geometrics.getRenderDimension().y, this.scale.x, this.scale.y,
			// this.rotation);
			batch.draw(this.texture, this.geometrics.getPosition().x - (this.geometrics.getRenderDimension().x / 2), this.geometrics.getPosition().y
					- (this.geometrics.getRenderDimension().y / 2), this.geometrics.getRenderDimension().x, this.geometrics.getRenderDimension().y);
		}*/
	}

	@Override
	public void update(final float deltaTime) {
		// super.update(deltaTime);
		this.animationCon.update(deltaTime);
		this.geometrics.update();
		/*if (this.geometrics.getB2Body() != null) {
			// this.geometrics.setPosition(this.b2Body.getPosition());

			this.geometrics.getPosition().x = this.geometrics.getPosition().x - (this.geometrics.getRenderDimension().x / 2);
			this.geometrics.getPosition().y = this.geometrics.getPosition().y - (this.geometrics.getRenderDimension().y / 2);
			// this.rotation = this.b2Body.getAngle() * MathUtils.radiansToDegrees;
		}*/
	}

	public boolean isCollected() {
		return this.collected;
	}

	public void setCollected(final boolean b) {
		if (this.collectable) {
			this.collected = b;
		}
	}

	// interactions
	public boolean grabbedBy(final Player player) {
		if (this.collectable && !this.collected && !this.isFlying) { // TODO: !this.collected prevent taking twice bug!?
			// System.out.println("Item grabbed by: " + player);
			// player.incrItemCount();
			this.grabbedBy = player;
			if (this.grabbedBy.addItem(this)) {
				Item.GRAB_SOUND.play();
				this.collected = true;
				// this.destroyed = false;
				this.toDelete = true;
				return true;
			}
			this.grabbedBy = null;
			this.collected = false;
			this.toDelete = false;
			return false;
		}
		return false;
	}

	public float getDmg() {
		return this.dmg;
	}

	public void toDelete(final boolean b) {
		this.toDelete = b;
	}

	public boolean isCollectable() {
		return this.collectable;
	}

	public void setCollectable(final boolean b) {
		this.collectable = b; // TODO sollte nicht veränderlich sein, und wenn flying anders gelöst werden
	}

	public boolean isFlying() {
		return this.isFlying;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public ItemType getItemType() {
		return this.itemType;
	}

	public Player getItemIsThrownBy() {
		return this.itemIsThrownBy;
	}

	protected void setItemIsThrownBy(final Player player) {
		this.itemIsThrownBy = player;
	}

	public float getFlyingTime() {
		return this.flyingTime;
	}

	public void setFlyingTime(final float f) {
		this.flyingTime = f;
	}

	/**
	 * @return Item.allItems
	 */
	public static List<Item> getAllItems() {
		return Item.allItems;
	}
}
