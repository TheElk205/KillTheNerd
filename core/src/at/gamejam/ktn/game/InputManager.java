package at.gamejam.ktn.game;

import at.gamejam.ktn.game.entites.Player;
import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.utils.CameraHelper;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {

	private final PlayerSleep	player;
	private final CameraHelper	cameraHelper;

	public InputManager(final PlayerSleep player, final CameraHelper cameraHelper) {
		this.player = player;
		this.cameraHelper = cameraHelper;
	}

	@Override
	public boolean keyDown(final int keycode) {
		switch (keycode) {
			case Input.Keys.PLUS:
				this.cameraHelper.addZoom(-0.2f);
				break;
			case Input.Keys.MINUS:
				this.cameraHelper.addZoom(0.2f);
				break;
			case Input.Keys.LEFT:
				this.player.setLeft(true);
				this.player.setDirectionMoving(Player.Direction.W);
				break;
			case Input.Keys.RIGHT:
				this.player.setRight(true);
				this.player.setDirectionMoving(Player.Direction.E);
				break;
			case Input.Keys.UP:
				this.player.setUp(true);
				this.player.setDirectionMoving(Player.Direction.N);
				break;
			case Input.Keys.DOWN:
				this.player.setDown(true);
				this.player.setDirectionMoving(Player.Direction.S);
				break;
			/*case Input.Keys.D:
			this.debug = !this.debug;
			break;*/
			case Input.Keys.SPACE:
				this.player.setShoot(true);
				break;
			/*case Input.Keys.R:
			this.reset = true;
			break;*/
			default:
				this.player.stop();
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(final int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				this.player.setLeft(false);
				this.player.setDirectionMoving(Player.Direction.W);
				break;
			case Input.Keys.RIGHT:
				this.player.setRight(false);
				this.player.setDirectionMoving(Player.Direction.E);
				break;
			case Input.Keys.UP:
				this.player.setUp(false);
				this.player.setDirectionMoving(Player.Direction.N);
				break;
			case Input.Keys.DOWN:
				this.player.setDown(false);
				this.player.setDirectionMoving(Player.Direction.S);
				break;
			case Input.Keys.SPACE:
				this.player.setShoot(false);
				break;
			default:
				// System.out.println("Player Stop");
				this.player.stop();
				break;
		}

		if (!this.player.getDown() && !this.player.getUp() && !this.player.getLeft() && !this.player.getRight()) {
			this.player.stop();
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

}
