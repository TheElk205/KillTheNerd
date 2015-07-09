package at.game.objects;

import at.game.mechanics.movement.BodyFactory;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends AbstractGameObject {

	public Enemy(final Vector2 position, final float width, final float height) {
		super(BodyFactory.createPlayerPlatformerBody1(position, width, height, false), position, width, height);
		// this.geometrics.setB2Body(BodyFactory.createPlayerPlatformerBody1(this.geometrics.getPosition(), this.geometrics.getApproxB2Width(),
		// this.geometrics.getApproxB2Height()));
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
