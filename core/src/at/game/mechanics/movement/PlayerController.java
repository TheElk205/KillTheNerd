package at.game.mechanics.movement;

import at.game.mechanics.Player;
import at.game.mechanics.enums.DirectionEnum;

public class PlayerController {
	private boolean			right;
	private boolean			up;
	private boolean			left;
	private boolean			down;
	private boolean			pressingJump;
	private boolean			pressingShoot;
	private final Player	player;
	private boolean			jumpLock;
	private boolean			climbLock;

	public PlayerController(final Player player) {
		MovementProcessor.init(this);
		this.player = player;
	}

	public void processInput(final float currentSpeed, final float modifier) {
		MovementProcessor.gravityJumpMovement(this.player, this.up, this.down, this.right, this.left, currentSpeed, modifier, this.pressingJump,
				this.jumpLock, this.climbLock);
	}

	public void setRight(final boolean b) {
		// if (!this.right) {
		this.right = b;
		if (b) {
			this.player.setNewestDirection(DirectionEnum.E);
		}
		this.setLooking();
		this.player.setDirectionMoving(DirectionEnum.E);

		// }
	}

	public void setUp(final boolean b) {
		// if (!this.up) {
		this.up = b;
		if (b) {
			this.player.setNewestDirection(DirectionEnum.N);
		}
		this.setLooking();
		this.player.setDirectionMoving(DirectionEnum.N);

		// }
	}

	public void setLeft(final boolean b) {
		// if (!this.left) {
		this.left = b;
		if (b) {
			this.player.setNewestDirection(DirectionEnum.W);
		}
		this.setLooking();
		this.player.setDirectionMoving(DirectionEnum.W);

		// }
	}

	public void setDown(final boolean b) {
		// if (!this.down) {
		this.down = b;
		if (b) {
			this.player.setNewestDirection(DirectionEnum.S);
		}
		this.setLooking();
		this.player.setDirectionMoving(DirectionEnum.S);

		// }
	}

	private void setLooking() {
		if ((this.up && (this.player.getNewestDirection() == DirectionEnum.N)) || (this.up && !this.left && !this.right)) {
			this.player.setDirectionLooking(DirectionEnum.N);
		}
		if ((this.down && (this.player.getNewestDirection() == DirectionEnum.S)) || (this.down && !this.left && !this.right)) {
			this.player.setDirectionLooking(DirectionEnum.S);
		}
		if ((this.left && (this.player.getNewestDirection() == DirectionEnum.W)) || (this.left && !this.up && !this.down)) {
			this.player.setDirectionLooking(DirectionEnum.W);
		}
		if ((this.right && (this.player.getNewestDirection() == DirectionEnum.E)) || (this.right && !this.up && !this.down)) {
			this.player.setDirectionLooking(DirectionEnum.E);
		}
	}

	public boolean getRight() {
		return this.right;
	}

	public boolean getUp() {
		return this.up;
	}

	public boolean getLeft() {
		return this.left;
	}

	public boolean getDown() {
		return this.down;
	}

	public void setPressingShoot(final boolean b) {
		this.pressingShoot = b;
	}

	public boolean isPressingShoot() {
		return this.pressingShoot;
	}

	public void setPressingJump(final boolean b) {
		this.pressingJump = b;
		if (!b) {
			this.jumpLock = false;
		}
	}

	public void setSwitchClimbLock() {
		this.climbLock = !this.climbLock;
	}

	public void setSwitchClimbLock(final boolean bool) {
		this.climbLock = bool;
	}

	public boolean getJumpLock() {
		return this.jumpLock;
	}

	public void setJumpLock(final boolean jumpLock) {
		this.jumpLock = jumpLock;
	}
}
