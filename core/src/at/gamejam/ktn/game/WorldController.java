package at.gamejam.ktn.game;

import at.gamejam.ktn.game.entities.Coin;
import at.gamejam.ktn.game.entities.Player;
import at.gamejam.ktn.game.entities.Spikes;
import at.gamejam.ktn.utils.CameraHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Lukas on 11.04.2015.
 */
public class WorldController extends InputAdapter {
	public CameraHelper		cameraHelper;
	public Player			player;
	public long				timeElapsed;
	int						coinCount	= 0;
	private World			b2World;
	private TopDownLevel	level;
	private boolean			debug		= true;
	private boolean			reset;

	public WorldController() {
		this.init();
	}

	public void init() {
		Gdx.input.setInputProcessor(this);
		this.cameraHelper = new CameraHelper();
		// this.b2World = new World(new Vector2(0, -9.81f), true);
		this.b2World = new World(new Vector2(0, 0), true); // no gravity
		this.player = new Player(new Vector2(0.5f, 1.5f), this.b2World);
		this.cameraHelper.setTarget(this.player.getBody());
		// this.level = new Level(this.b2World);
		this.level = new TopDownLevel(this.b2World);
		this.b2World.setContactListener(new ContactListener() {
			@Override
			public void beginContact(final Contact contact) {
				final Fixture fA = contact.getFixtureA();
				final Fixture fB = contact.getFixtureB();
				if (((fB.getBody().getUserData() instanceof Player) && (fA.getBody().getUserData() instanceof Spikes))
						|| ((fA.getBody().getUserData() instanceof Player) && (fB.getBody().getUserData() instanceof Spikes))) {
					WorldController.this.reset = true;
				}
			}

			@Override
			public void endContact(final Contact contact) {

			}

			@Override
			public void preSolve(final Contact contact, final Manifold oldManifold) {

			}

			@Override
			public void postSolve(final Contact contact, final ContactImpulse impulse) {

			}
		});
	}

	public void update(final float deltaTime) {
		this.timeElapsed += deltaTime * 1000;
		this.cameraHelper.update(deltaTime);
		this.b2World.step(1 / 60f, 3, 8); // timeStep, velocityIteration, positionIteration
		if (this.reset) {
			this.reset();
		}
		this.player.update(deltaTime);
		this.level.update(deltaTime);
		if (this.player.getBody().getPosition().y < -3) {
			this.reset = true;
		}
		this.testCoins();
	}

	private void reset() {
		this.reset = false;
		this.player.getBody().setTransform(new Vector2(0.5f, 1.5f), 0);
		this.player.getBody().setLinearVelocity(new Vector2(0, 0));
		this.player.getBody().setAngularVelocity(0);
		this.timeElapsed = 0;
		this.coinCount = 0;
	}

	private void testCoins() {
		final Rectangle playerRect = new Rectangle();
		final Rectangle coinRect = new Rectangle();
		playerRect.set(this.player.position.x, this.player.position.y, this.player.dimension.x, this.player.dimension.y);
		for (final Coin coin : this.level.getCoins()) {
			if (coin.isCollected()) {
				continue;
			}
			coinRect.set(coin.position.x, coin.position.y, coin.dimension.x, coin.dimension.y);
			if (!playerRect.overlaps(coinRect)) {
				continue;
			}
			coin.setCollected(true);
			this.coinCount += 1;
		}
	}

	@Override
	public boolean keyDown(final int keycode) {
		switch (keycode) {
			case Input.Keys.PLUS:
				this.cameraHelper.addZoom(-0.2f);
				break;
			case Input.Keys.MINUS:
				this.cameraHelper.addZoom(0.2f);
				break;
			case Input.Keys.LEFT:
				this.player.setLeft(true);
				break;
			case Input.Keys.RIGHT:
				this.player.setRight(true);
				break;
			case Input.Keys.D:
				this.debug = !this.debug;
				break;
			case Input.Keys.SPACE:
			case Input.Keys.UP:
				this.player.jump();
				break;
			case Input.Keys.R:
				this.reset = true;
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(final int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				this.player.setLeft(false);
				break;
			case Input.Keys.RIGHT:
				this.player.setRight(false);
				break;
		}
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		if (screenY < (Gdx.graphics.getHeight() / 4)) {
			this.reset();
		}
		if (screenX < (Gdx.graphics.getWidth() / 3)) {
			this.player.setLeft(true);
		} else
			if (screenX > ((Gdx.graphics.getWidth() / 3) * 2)) {
				this.player.setRight(true);
			} else {
				this.player.jump();
			}
		return true;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		if (screenX < (Gdx.graphics.getWidth() / 3)) {
			this.player.setLeft(false);
		} else
			if (screenX > ((Gdx.graphics.getWidth() / 3) * 2)) {
				this.player.setRight(false);
			}
		return true;
	}

	public boolean isDebug() {
		return this.debug;
	}

	public World getB2World() {
		return this.b2World;
	}

	public TopDownLevel getLevel() {
		return this.level;
	}
}
