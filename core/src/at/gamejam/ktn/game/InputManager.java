package at.gamejam.ktn.game;

import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.PlayerWake;
import at.gamejam.ktn.utils.CameraHelper;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {

	private final PlayerWake	playerWake;
	private final PlayerSleep	playerSleep;
	private final CameraHelper	cameraHelper;

	private boolean				enabled	= true;

	public InputManager(final PlayerWake playerWake, final PlayerSleep playerSleep, final CameraHelper cameraHelper) {
		this.playerWake = playerWake;
		this.playerSleep = playerSleep;
		this.cameraHelper = cameraHelper;
	}

	@Override
	public boolean keyDown(final int keycode) {
		if (!enabled) {
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

				// Running, Player Sleep
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
			// running Player Wake
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
		/*switch (keycode) {
			case Input.Keys.LEFT:
			case Input.Keys.RIGHT:
			case Input.Keys.UP:
			case Input.Keys.DOWN:
				this.player.stop();
				break;
			default:
				this.player.stop();
				break;
		}*/
		return false;
	}

	/*@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		if (screenY < (Gdx.graphics.getHeight() / 4)) {
			this.reset();
		}
		if (screenX < (Gdx.graphics.getWidth() / 3)) {
			// this.player.setLeft(true);
		} else
			if (screenX > ((Gdx.graphics.getWidth() / 3) * 2)) {
				// this.player.setRight(true);
			} else {
				// this.player.jump();
			}
		return true;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		if (screenX < (Gdx.graphics.getWidth() / 3)) {
			// this.player.setLeft(false);
		} else
			if (screenX > ((Gdx.graphics.getWidth() / 3) * 2)) {
				// this.player.setRight(false);
			}
		return true;
	}*/

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
