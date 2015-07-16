package at.game.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

public class AIMovementSystem extends EntitySystem {
	private ImmutableArray<Entity>				entities;
	private final ComponentMapper<Geometrics>	positionMovement	= ComponentMapper.getFor(Geometrics.class);
	private float								accu				= 0;

	private AIMovementSystem() {
	}

	@Override
	public void addedToEngine(final Engine engine) {
		this.entities = engine.getEntitiesFor(Family.all(Geometrics.class, AIMovement.class).get());
	}

	@Override
	public void update(final float deltaTime) {
		for (int i = 0; i < this.entities.size(); ++i) {
			final Entity entity = this.entities.get(i);
			final Geometrics position = this.positionMovement.get(entity);
			final Vector2 positionV = position.getPosition();
			this.accu += deltaTime;
			if (this.accu > 1) {
				final float newX = positionV.x + (position.getVelocity() * deltaTime);
				final float newY = positionV.y + (position.getVelocity() * deltaTime);
				position.setPosition(new Vector2(newX, newY));
			}
		}
	}
}