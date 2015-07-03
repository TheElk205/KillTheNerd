package at.game.managers;

import at.game.gamemechanics.movement.PlayerController;
import at.game.utils.Constants;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * @author Herkt Kevin
 */
public class InputManager extends InputAdapter {
	// private static boolean[] keyStates;
	// private static boolean[] previousKeyStates;
	// private final static int NUM_OF_KEYS = 5; // W A S D + SHOOT
	// private final Player playerWake = null;
	private PlayerController	playerController	= null;
	// private final CameraHelper cameraHelper;

	private boolean				enabled				= true;

	/**
	 * @param playerController
	 */
	public InputManager(final PlayerController playerController) { // , final CameraHelper cameraHelper
		// InputManager.keyStates = new boolean[InputManager.NUM_OF_KEYS];
		// InputManager.previousKeyStates = new boolean[InputManager.NUM_OF_KEYS];
		// this.playerWake = playerWake;
		this.playerController = playerController;
		// this.cameraHelper = cameraHelper;
	}

	@Override
	public boolean keyDown(final int keycode) {
		if (!this.enabled) {
			return false;
		}
		switch (keycode) {
		// Camera
			case Input.Keys.P:
				// this.cameraHelper.addZoom(1f);
				break;
			case Input.Keys.O:
				// this.cameraHelper.addZoom(-1f);
				break;
			default:
				break;
		}
		/*if (this.playerWake != null) {
			switch (keycode) {
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
				case Input.Keys.CONTROL_RIGHT:
					this.playerWake.setPressingShoot(true);
					break;
				default:
					break;
			}
		}*/
		if (this.playerController != null) {
			switch (keycode) {
			// running Player Sleep
				case Input.Keys.A:
					this.playerController.setLeft(true);
					break;
				case Input.Keys.D:
					this.playerController.setRight(true);
					break;
				case Input.Keys.W:
					this.playerController.setUp(true);
					break;
				case Input.Keys.S:
					this.playerController.setDown(true);
					break;
				case Input.Keys.CONTROL_LEFT:
					this.playerController.setPressingShoot(true);
					break;
				case Input.Keys.SPACE:
					// System.out.println("InputManager - pressing jump");
					this.playerController.setPressingJump(true);
					break;
				/*case Input.Keys.R:
				this.reset = true;
				break;*/
				default:
					// this.playerWake.stop();
					break;
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(final int keycode) {
		/*if (this.playerWake != null) {
			switch (keycode) {
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
				case Input.Keys.CONTROL_RIGHT:
					this.playerWake.setPressingShoot(false);
					break;
				default:
					break;
			}
		}*/
		if (this.playerController != null) {
			switch (keycode) {
				case Input.Keys.A:
					this.playerController.setLeft(false);
					break;
				case Input.Keys.D:
					this.playerController.setRight(false);
					break;
				case Input.Keys.W:
					this.playerController.setUp(false);
					break;
				case Input.Keys.S:
					this.playerController.setDown(false);
					break;
				case Input.Keys.CONTROL_LEFT:
					this.playerController.setPressingShoot(false);
					break;
				case Input.Keys.SPACE:
					this.playerController.setPressingJump(false);
					break;
				default:
					break;
			}
		}

		if (!Constants.IS_DOWN_GRAVITY) {
			/*if ((this.playerWake != null) && !this.playerWake.getDown() && !this.playerWake.getUp() && !this.playerWake.getLeft() && !this.playerWake.getRight()) {
				this.playerWake.stop();
			}
			if ((this.playerSleep != null) && !this.playerSleep.getDown() && !this.playerSleep.getUp() && !this.playerSleep.getLeft() && !this.playerSleep.getRight()) {
				this.playerSleep.stop();
			}*/}
		return false;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
}
