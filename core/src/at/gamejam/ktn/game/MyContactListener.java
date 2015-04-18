package at.gamejam.ktn.game;

import at.gamejam.ktn.game.entites.Player;
import at.gamejam.ktn.game.entites.ThrowableObject;
import at.gamejam.ktn.game.entities.Spikes;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	public MyContactListener() {
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

		final boolean playerWithSpikes = ((userDataB instanceof Player) && (userDataA instanceof Spikes)) || ((userDataA instanceof Player) && (userDataB instanceof Spikes));
		final boolean playerWithThrowable = ((userDataB instanceof Player) && (userDataA instanceof ThrowableObject)) || ((userDataA instanceof Player) && (userDataB instanceof ThrowableObject));
		final boolean player1WithPlayer2 = (userDataB instanceof Player) && (userDataA instanceof Player);

		if (!(playerWithSpikes || playerWithThrowable || player1WithPlayer2)) {
			return; // no collision at all
		}

		if (playerWithSpikes) {
			// WorldController.this.reset = true;
			System.out.println("beginContact Player with Spikes");
		} else
			if (playerWithThrowable) {
				System.out.println("beginContact Player with TrowableObject");
			} else
				if (player1WithPlayer2) {
					System.out.println("beginContact Player1 with Player2");
				} else {
					System.out.println("beginContact no Contact");
				}
	}

	@Override
	public void endContact(final Contact contact) {
		System.out.println("endContact");
	}

	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
		System.out.println("preSolve");
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
		System.out.println("postSolve");
	}

}
