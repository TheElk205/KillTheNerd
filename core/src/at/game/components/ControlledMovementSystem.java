package at.game.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

public class ControlledMovementSystem extends AbstractSystem {
	private final ComponentMapper<Geometrics>		positionMovement	= ComponentMapper.getFor(Geometrics.class);
	private final ComponentMapper<PlayerController>	playerController	= ComponentMapper.getFor(PlayerController.class);

	public ControlledMovementSystem() {
	}

	@Override
	public void addedToEngine(final Engine engine) {
		this.entities = engine.getEntitiesFor(Family.all(Geometrics.class, PlayerController.class).get());
		// this.entities = engine.getEntitiesFor(Family.all(Geometrics.class, VelocityComponent.class).get());
	}

	@Override
	public void update(final float deltaTime) {
		for (int i = 0; i < this.entities.size(); ++i) {
			final Entity entity = this.entities.get(i);
			final Geometrics position = this.positionMovement.get(entity);
			final PlayerController playerController = this.playerController.get(entity);
			// final VelocityComponent velocity = vm.get(entity);

			/*final Vector2 positionV = position.getPosition();
			final float newX = positionV.x + (position.getVelocity() * deltaTime);
			final float newY = positionV.y + (position.getVelocity() * deltaTime);
			// TODO replace with inputcheck (keyboar)
			position.setPosition(new Vector2(newX, newY));*/

			playerController.update(5);
		}
	}
}