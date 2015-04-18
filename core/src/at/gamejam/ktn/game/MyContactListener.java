package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;

import at.gamejam.ktn.game.entites.Item;
import at.gamejam.ktn.game.entites.Player;
import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.PlayerWake;
import at.gamejam.ktn.game.entites.RedBull;
import at.gamejam.ktn.game.entites.ThrowableObject;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.game.entities.Spikes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
	private final WorldController	wContr;

	/** object contains a body */
	private final List<GameObject>	objectsToRemove	= new ArrayList<GameObject>();
	/** the physics of this object (body) are not init yet */
	private final List<GameObject>	objectsToAdd	= new ArrayList<GameObject>();

	public MyContactListener(final WorldController wContr) {
		this.wContr = wContr;
	}

	@Override
	public void beginContact(final Contact contact) {
		final Fixture fA = contact.getFixtureA();
		final Fixture fB = contact.getFixtureB();

		final Object userDataB = fB.getBody().getUserData();
		final Object userDataA = fA.getBody().getUserData();

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
		if (((userDataB instanceof Player) && (userDataA instanceof Spikes)) || ((userDataA instanceof Player) && (userDataB instanceof Spikes))) {
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
		}

		/*if (!(playerWithSpikes || playerWithThrowable || player1WithPlayer2 || playerWithItem)) {
			return; // no collision at all
		}*/

		switch (collisionType) {
			case 1:
				RedBull newItem = null;
				// WorldController.this.reset = true;
				final Vector2 itemPosition = new Vector2(2, 2);
				System.out.println("beginContact Player with Spikes");
				if (userDataA instanceof Spikes) {
					System.out.println("userDataA is Spikes");
					// final Vector2 itemPosition = ((Spikes) userDataA).position;

					System.out.println("spikePosition: " + itemPosition + "playerPosition: " + ((Player) userDataB).position);
					newItem = new RedBull(itemPosition, this.wContr.getB2World(), false);
					this.objectsToAdd.add(newItem);
				} else
					if ((userDataB instanceof Spikes)) {
						System.out.println("userDataB is Spikes");

						// final Vector2 itemPosition = ((GameObject) userDataB).position;
						newItem = new RedBull(itemPosition, this.wContr.getB2World(), false);
						this.objectsToAdd.add(newItem);
					} else {
						System.out.println("ERROR");
					}

				break;
			case 2:
				System.out.println("beginContact Player with TrowableObject");
				break;
			case 3:
				System.out.println("beginContact Player1 with Player2");
				break;
			case 4: // playerWake + any item
				if (userDataA instanceof Item) {
					System.out.println("beginContact PlayerWake with Item");
					final Item item = (Item) (userDataA);
					final PlayerWake player = (PlayerWake) (userDataB);
					item.grabbed(player);
				} else {
					System.out.println("beginContact Item with PlayerWake");
					final Item item = (Item) userDataB;
					final PlayerWake player = (PlayerWake) userDataA;
					item.grabbed(player);
				}
				break;
			case 5: // playerSleep + any item
				if (userDataA instanceof Item) {
					System.out.println("beginContact PlayerSleep with Item");
					final Item item = (Item) (userDataA);
					final PlayerSleep player = (PlayerSleep) userDataB;
					if (player.hitByItem(item)) {
						player.setToRender(false);
						objectsToRemove.add(player);
					}
					objectsToRemove.add(item);
					item.setToRender(false);

				} else {
					System.out.println("beginContact Item with PlayerSleep");
					final Item item = (Item) userDataB;
					final PlayerSleep player = (PlayerSleep) userDataA;
					if (player.hitByItem(item)) {
						objectsToRemove.add(player);
						player.setToRender(false);
					}
					objectsToRemove.add(item);
					item.setToRender(false);
				}
				break;
			default:
				System.out.println("default hit A: " + userDataA + " B: " + userDataB);
				break;
		}

	}

	@Override
	public void endContact(final Contact contact) {
		// System.out.println("endContact");
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
	protected List<GameObject> getObjectsToRemove() {
		return this.objectsToRemove;
	}

	/**
	 * @return the objectsToAdd
	 */
	protected List<GameObject> getObjectsToAdd() {
		return this.objectsToAdd;
	}

}
