package at.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class EquippedWeapon extends AbstractGameObject {
	private final AbstractGameObject	equippedBy;

	public EquippedWeapon(final Body b2Body, final Vector2 position, final float approxB2Width, final float approxB2Height,
			final AbstractGameObject equippedBy) {
		super(b2Body, position, approxB2Width, approxB2Height);
		this.equippedBy = equippedBy;
	}

	@Override
	public void render(final SpriteBatch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initPhysics() {
		// TODO Auto-generated method stub

	}

}
