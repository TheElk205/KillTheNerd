package at.game.visuals.hud;

import at.game.RenderObject;

/**
 * @author Herkt Kevin
 */
public abstract class AbstractHUDElement extends RenderObject {
	// protected Vector2 dimension;

	protected AbstractHUDElement() {
	}

	abstract public void update(final float deltaTime);
	// abstract public void render(final SpriteBatch batch);
}
