package at.game.managers;

import at.game.utils.CameraHelper;
import at.game.visuals.Player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {

	private final Player		playerWake;
	private final Player		playerSleep;
	private final CameraHelper	cameraHelper;

	private boolean				enabled	= true;

	public InputManager(final Player playerWake, final Player playerSleep, final CameraHelper cameraHelper) {
		this.playerWake = playerWake;
		this.playerSleep = playerSleep;
		this.cameraHelper = cameraHelper;
	}

	@Override
	public boolean keyDown(final int keycode) {
		if (!this.enabled) {
			return false;
		}
		switch (keycode) {
		// Camera
			case Input.Keys.PLUS:
				this.cameraHelper.addZoom(-0.2f);
				break;
			case Input.Keys.MINUS:
				this.cameraHelper.addZoom(0.2f);
				break;

			// Running, Player Wake
			case Input.Keys.LEFT:
				this.playerWake.setLeft(true);
				break;
			case Input.Keys.RIGHT:
				this.playerWake.setRight(true);
				break;
			case Input.Keys.UP:
				this.playerWake.setUp(true);
				break;
			case Input.Keys.DOWN:
				this.playerWake.setDown(true);
				break;
				// running Player Sleep
			case Input.Keys.A:
				this.playerSleep.setLeft(true);
				break;
			case Input.Keys.D:
				this.playerSleep.setRight(true);
				break;
			case Input.Keys.W:
				this.playerSleep.setUp(true);
				break;
			case Input.Keys.S:
				this.playerSleep.setDown(true);
				break;
				/*case Input.Keys.D:
			this.debug = !this.debug;
			break;*/
				// shooting Player sleep
			case Input.Keys.CONTROL_RIGHT:
				this.playerWake.setShoot(true);
				break;

			// shooting Player wake
			case Input.Keys.CONTROL_LEFT:
				this.playerSleep.setShoot(true);
				break;
				/*case Input.Keys.R:
			this.reset = true;
			break;*/
			default:
				// this.playerWake.stop();
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(final int keycode) {
		switch (keycode) {
			// Running Player Wake
			case Input.Keys.LEFT:
				this.playerWake.setLeft(false);
				break;
			case Input.Keys.RIGHT:
				this.playerWake.setRight(false);
				break;
			case Input.Keys.UP:
				this.playerWake.setUp(false);
				break;
			case Input.Keys.DOWN:
				this.playerWake.setDown(false);
				break;
				// shoot Player Sleep
			case Input.Keys.CONTROL_RIGHT:
				this.playerWake.setShoot(false);
				break;
			// running Player Wake
			case Input.Keys.A:
				this.playerSleep.setLeft(false);
				break;
			case Input.Keys.D:
				this.playerSleep.setRight(false);
				break;
			case Input.Keys.W:
				this.playerSleep.setUp(false);
				break;
			case Input.Keys.S:
				this.playerSleep.setDown(false);
				break;
				// shooting player wake
			case Input.Keys.CONTROL_LEFT:
				this.playerSleep.setShoot(false);
				break;
			default:
				// System.out.println("Player Stop");
				// this.playerWake.stop();
				// this.playerSleep.stop();
				break;
		}

		if (!this.playerWake.getDown() && !this.playerWake.getUp() && !this.playerWake.getLeft() && !this.playerWake.getRight()) {
			this.playerWake.stop();
		}
		if (!this.playerSleep.getDown() && !this.playerSleep.getUp() && !this.playerSleep.getLeft() && !this.playerSleep.getRight()) {
			this.playerSleep.stop();
		}
		return false;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
}
