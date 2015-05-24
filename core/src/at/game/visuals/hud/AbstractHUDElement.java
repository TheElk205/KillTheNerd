package at.game.visuals.hud;

import at.game.visuals.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractHUDElement extends GameObject { // TODO vl extends entfernen
	protected Vector2	dimension;

	protected AbstractHUDElement() {
	}

	@Override
	abstract public void update(final float deltaTime);

	@Override
	abstract public void render(final SpriteBatch batch);
}
