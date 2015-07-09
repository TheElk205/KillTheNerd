package at.game.managers;

import at.game.mechanics.movement.MenuController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * @author Herkt Kevin
 */
public class MenuInputManager extends InputAdapter {
	private MenuController	menuController	= null;
	private boolean			enabled			= true;

	/**
	 * @param menuController
	 */
	public MenuInputManager(final MenuController menuController) {
		this.menuController = menuController;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		if (!this.enabled) {
			return false;
		}
		System.out.println(screenX + " " + screenY + " " + pointer + " " + button);
		if (this.menuController != null) {
			switch (button) {
				case Input.Buttons.LEFT: // Buttons means mouse
					final int x = Gdx.input.getX();
					final int y = Gdx.input.getY();
					System.out.println("LEFT Mouse button: " + x + ":" + y);
					break;
				case Input.Buttons.RIGHT:
					break;
				default:
					break;
			}
		}
		return true;

	}

	@Override
	public boolean keyDown(final int keycode) {
		if (!this.enabled) {
			return false;
		}

		return true;
	}

	@Override
	public boolean keyUp(final int keycode) {
		if (this.menuController != null) {
			switch (keycode) {
				default:
					break;
			}
		}
		return false;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
}
