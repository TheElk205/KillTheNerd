package at.game;

import java.util.ArrayList;
import java.util.List;

import at.game.enums.ItemType;
import at.game.enums.PlayerType;
import at.game.visuals.GameObject;
import at.game.visuals.Item;
import at.game.visuals.NPC;
import at.game.visuals.Player;
import at.game.visuals.ThrowableObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
	/** object contains a body */
	private final List<GameObject>	objectsToRemove	= new ArrayList<GameObject>();
	/** the physics of this object (body) are not init yet */
	private final List<GameObject>	objectsToAdd	= new ArrayList<GameObject>();

	protected MyContactListener() {
	}

	@Override
	public void beginContact(final Contact contact) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();

		final Object userDataB = fB.getBody().getUserData();
		final Object userDataA = fA.getBody().getUserData();
		// System.out.println("--" + userDataA + " collides with " + userDataB);

		/*if (userDataA instanceof Item) {TODO: hold an item list in controller to set collectable again?
			((Item) userDataA).collectable = true;
		}
		if (userDataB instanceof Item) {
			((Item) userDataB).collectable = true;
		}*/

		// fixture ist verbindung zwischen body und shape
		// body physikalische eigenschaften
		// shape kreis

		// TODO: should be done with filter.maskbit

		// TODO dont add or remove objects because this method is called while step()

		/*final boolean playerWithSpikes = ((userDataB instanceof Player) && (userDataA instanceof Spikes)) || ((userDataA instanceof Player) && (userDataB instanceof Spikes));
		final boolean playerWithThrowable = ((userDataB instanceof Player) && (userDataA instanceof ThrowableObject)) || ((userDataA instanceof Player) && (userDataB instanceof ThrowableObject));
		final boolean player1WithPlayer2 = (userDataB instanceof Player) && (userDataA instanceof Player);
		final boolean playerWithItem = ((userDataB instanceof Player) && (userDataA instanceof Item)) || ((userDataA instanceof Item) && (userDataB instanceof Player));*/
		int collisionType = 0;
		// if (((userDataB instanceof Player) && (userDataA instanceof Spikes)) || ((userDataA instanceof Player) && (userDataB instanceof Spikes))) {
		// collisionType = 1;
		// } else
		if (((userDataB instanceof Player) && (userDataA instanceof ThrowableObject)) || ((userDataA instanceof Player) && (userDataB instanceof ThrowableObject))) {
			collisionType = 2;
		} else
			if ((userDataB instanceof Player) && (userDataA instanceof Player)) {
				collisionType = 3;
			} else
				if ((userDataB instanceof Player) && (userDataA instanceof Item)) {
					final Player playerB = (Player) userDataB;
					if (playerB.getPlayerType() == PlayerType.Sleep) {
						collisionType = 5;
					} else {
						collisionType = 4; // any item collides with player wake
					}
				} else
					if ((userDataB instanceof Item) && (userDataA instanceof Item)) {
						collisionType = 6;
					} else
						if (((userDataB instanceof Player) && (userDataA instanceof NPC)) || ((userDataB instanceof NPC) && (userDataA instanceof Player))) {
							collisionType = 7;
						}

		/*if (!(playerWithSpikes || playerWithThrowable || player1WithPlayer2 || playerWithItem)) {
			return; // no collision at all
		}*/

		switch (collisionType) {
			case 1:
				final Item newItem = null;
				// WorldController.this.reset = true;
				final Vector2 itemPosition = new Vector2(2, 2);
				/*if (userDataA instanceof Spikes) {
					System.out.println("userDataA is Spikes");
					newItem = new Item(itemPosition, this.wContr.getB2World(), false, ItemType.Item1, "Item1");
					this.objectsToAdd.add(newItem);
				} else
					if ((userDataB instanceof Spikes)) {
						System.out.println("userDataB is Spikes");

						newItem = new Item(itemPosition, this.wContr.getB2World(), false, ItemType.Item2, "Item2");
						this.objectsToAdd.add(newItem);
					} else {
						System.out.println("ERROR");
					}*/

				break;
			case 2:
				// System.out.println("beginContact Player with TrowableObject");
				break;
			case 3:
				// System.out.println("beginContact Player1 with Player2");
				break;
			case 4:
				if (userDataA instanceof Item) {
					final Item itemA = (Item) userDataA;
					final Player playerB = (Player) userDataB;
					if (itemA.getItemType() == ItemType.Wake_Item) {
						this.wakeWithRedbull(playerB, itemA);
					} else {
						this.wakeWithThesis(playerB, itemA);
					}
				} else
					if (userDataB instanceof Item) {
						final Item itemB = (Item) userDataB;
						final Player playerA = (Player) userDataA;
						if (itemB.getItemType() == ItemType.Sleep_Item) {
							this.wakeWithThesis(playerA, itemB);
						} else {
							this.wakeWithRedbull(playerA, itemB);
						}
					}
				break;
			case 5:
				if (userDataA instanceof Item) {
					final Item itemA = (Item) userDataA;
					final Player playerB = (Player) userDataB;
					if (itemA.getItemType() == ItemType.Wake_Item) {
						this.sleepWithRedbull(playerB, itemA);
					} else {
						this.sleepWithThesis(playerB, itemA);
					}
				} else
					if (userDataB instanceof Item) {
						final Item itemB = (Item) userDataB;
						final Player playerA = (Player) userDataA;
						if (itemB.getItemType() == ItemType.Sleep_Item) {
							this.sleepWithThesis(playerA, itemB);
						} else {
							this.sleepWithRedbull(playerA, itemB);
						}
					}
				break;
			case 6:
				MyContactListener.itemWithItem(userDataA, userDataB);
				break;
			case 7: // Set Player wake
				// System.out.println(userDataA + " with " + userDataB); // TODO: helper class die nur printet wenn debug
				if (userDataA instanceof NPC) {
					final NPC npc = (NPC) userDataA;
					final Player player = (Player) userDataB;
					npc.addPlayer(player);
				} else
					if (userDataB instanceof NPC) {
						final NPC npc = (NPC) userDataB;
						final Player player = (Player) userDataA;
						npc.addPlayer(player);
					}
				break;
			default:
				// System.out.println("default hit - A: " + userDataA + " B: " + userDataB);
				MyContactListener.itemWithNonSensor(fB, userDataA);
				MyContactListener.itemWithNonSensor(fA, userDataB);
				break;
		}

	}

	private static void itemWithNonSensor(final Fixture fixture, final Object userData) {
		if ((userData instanceof Item) && !fixture.isSensor()) {
			((Item) userData).reset();
			// System.out.println(userData + " with non-sensor " + fixture.getBody().getUserData());
		}
	}

	/**
	 * TODO: vl in das object item verschieben. methode setCollectable();
	 *
	 * @param userDataA
	 *            object
	 * @param userDataB
	 *            object
	 */
	private static void itemWithItem(final Object userDataA, final Object userDataB) {
		if ((userDataA instanceof Item) && (userDataB instanceof Item)) {
			// System.out.println("Item hits Item");
			((Item) userDataA).reset();
			((Item) userDataB).reset();
		}
	}

	private void wakeWithThesis(final Player player, final Item itemB) {
		if ((itemB.getItemIsThrownBy().getPlayerType() == PlayerType.Sleep) && itemB.isFlying()) {
			if (player.hitByItem(itemB)) { // does nothing cause players cannot die
				player.setToRender(false);
				this.objectsToRemove.add(player);
				itemB.toDelete = true;
			}
			if (player.isHitAbleAgain()) {
				player.setHandicap(itemB.getDmg());
				player.setHitAbleAgain(false);
			}
			itemB.setToRender(false);
			this.objectsToRemove.add(itemB);
			itemB.toDelete = true;
		}
	}

	private void sleepWithRedbull(final Player playerA, final Item itemA) {
		if ((itemA.getItemIsThrownBy().getPlayerType() == PlayerType.Wake) && itemA.isFlying()) {
			if (playerA.hitByItem(itemA)) { // does nothing cause players cannot die
				playerA.setToRender(false);
				this.objectsToRemove.add(playerA);
				itemA.toDelete = true;
			}
			if (playerA.isHitAbleAgain()) {
				playerA.setHandicap(itemA.getDmg());
				playerA.setHitAbleAgain(false);
			}
			itemA.setToRender(false);
			this.objectsToRemove.add(itemA);
			itemA.toDelete = true;
		}
	}

	private void sleepWithThesis(final Player playerA, final Item itemB) {
		if (itemB.grabbedBy(playerA)) {
			itemB.setToRender(false);
			this.objectsToRemove.add(itemB);
			itemB.toDelete = true;
		}
	}

	private void wakeWithRedbull(final Player playerA, final Item itemB) {
		if (itemB.grabbedBy(playerA)) {
			itemB.setToRender(false);
			this.objectsToRemove.add(itemB);
			itemB.toDelete = true;
		}

	}

	@Override
	public void endContact(final Contact contact) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();

		final Object userDataB = fB.getBody().getUserData();
		final Object userDataA = fA.getBody().getUserData();
		int collisionType = 0;
		/*if (((userDataB instanceof Player) && (userDataA instanceof Spikes)) || ((userDataA instanceof Player) && (userDataB instanceof Spikes))) {
			collisionType = 1;
		}
		if (((userDataB instanceof Player) && (userDataA instanceof ThrowableObject)) || ((userDataA instanceof Player) && (userDataB instanceof ThrowableObject))) {
			collisionType = 2;
		}
		if ((userDataB instanceof Player) && (userDataA instanceof Player)) {
			collisionType = 3;
		}
		if (((userDataB instanceof PlayerWake) && (userDataA instanceof Item)) || ((userDataA instanceof Item) && (userDataB instanceof PlayerWake))) {
			collisionType = 4;
		}
		if (((userDataB instanceof PlayerSleep) && (userDataA instanceof Item)) || ((userDataA instanceof Item) && (userDataB instanceof PlayerSleep))) {
			collisionType = 5;
		}*/

		if (((userDataB instanceof Player) && (userDataA instanceof NPC)) || ((userDataB instanceof NPC) && (userDataA instanceof Player))) {
			collisionType = 7;
		}
		switch (collisionType) {
			case 7:
				if (userDataA instanceof NPC) {
					final NPC npc = (NPC) userDataA;
					final Player player = (Player) userDataB;
					npc.removePlayer(player);
				} else
					if (userDataB instanceof NPC) {
						final NPC npc = (NPC) userDataB;
						final Player player = (Player) userDataA;
						npc.removePlayer(player);
					}
				break;
			default:
				break;
		}
	}

	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
		// contact.setEnabled(false);
		// System.out.println("preSolve");
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
		// System.out.println("postSolve");
	}

	/**
	 * @return the bodysToRemove
	 */
	public List<GameObject> getObjectsToRemove() {
		return this.objectsToRemove;
	}

	/**
	 * @return the objectsToAdd
	 */
	public List<GameObject> getObjectsToAdd() {
		return this.objectsToAdd;
	}

}
