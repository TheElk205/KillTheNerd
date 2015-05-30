package at.game.visuals.hud;

import at.game.visuals.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractHUDElement extends GameObject { // TODO vl extends entfernen => aber im Controller werden GameObjects gerendert und HUD
																// muss gerendert werden
	protected Vector2	dimension;

	protected AbstractHUDElement() {
	}

	// @Override
	@Override
	abstract public void update(final float deltaTime);

	// @Override
	@Override
	abstract public void render(final SpriteBatch batch);
}
