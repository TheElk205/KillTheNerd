package at.game.mechanics.movement;

import at.game.Constants;
import at.game.components.PlayerController;
import at.game.mechanics.Player;
import at.game.mechanics.enums.HumanStateEnum;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author Herkt Kevin
 */
public class MovementProcessor {
	// private static final long LONG_JUMP_PRESS = 150l;
	// private static final float ACCELERATION = 20f;
	// private static final float GRAVITY = -20f;
	// private static final float DAMP = 0.90f;
	// private static final float MAX_VEL = 4f;
	private static long				jumpPressedTime;
	private static boolean			jumpingPressed;
	private static PlayerController	controller;

	public static void init(final PlayerController playerController) {
		MovementProcessor.controller = playerController;
	}

	/*private static void clampVelocity(final Player player) {
		final AbstractRace race = player.getRace();
		if (Math.abs(player.velocity.x) > race.getMaxVelocity()) {
			player.velocity.x = Math.signum(player.velocity.x) * race.getMaxVelocity();
		}
	}*/

	public static void gravityJumpMovement(final Player player, final boolean up, final boolean down, final boolean right, final boolean left,
			final float currentSpeed, final float modifier, final boolean jump, final boolean jumpLock, final boolean climbLock) {
		final Body b2Body = player.getB2Body();
		final Vector2 lin = new Vector2(b2Body.getLinearVelocity().x, b2Body.getLinearVelocity().y);
		// grab top wall - you dont need to press a button
		final boolean canClimb = true;
		if ((player.getHeadColliding() > 0) && canClimb && climbLock) {
			lin.y = 1; // statt schwerkraft -10, 1
			// player.getB2Body().setLinearDamping(10000f);
			player.setPlayerState(HumanStateEnum.CLIMB_TOP);
		} else
			if ((player.getWallColliding() > 0) && canClimb && climbLock) {
				lin.y = 0.1f;
				// player.getB2Body().setLinearDamping(10000f);
				player.setPlayerState(HumanStateEnum.CLIMB);
			} else {
				player.getB2Body().setLinearDamping(0f);
			}

		if ((player.getWallColliding() == 0) && (player.getHeadColliding() == 0) && (player.getFooterCollidingCount() == 0)) {
			player.setPlayerState(HumanStateEnum.NOT_ON_GROUND);
		}
		if (!up && !down && !right && !left && !jump) {
			b2Body.setLinearVelocity(lin);
			return;
		}

		// after this line you need to press a button

		final boolean isCurrentlyJumping = player.getPlayerState().equals(HumanStateEnum.JUMPING);
		final boolean onGroundEnum = player.getPlayerState().equals(HumanStateEnum.WALKING);
		final boolean doubleJump = true;

		if (jump && !jumpLock) {
			// if (!isCurrentlyJumping) {
			if (player.isOnGround()) {
				MovementProcessor.controller.setJumpLock(true);
				// player.setOnGround(false);
				// player.setPlayerState(HumanStateEnum.JUMPING);
				// jumpingPressed = true;
				MovementProcessor.jumpPressedTime = System.currentTimeMillis();
				lin.y = player.getRace().getMaxJumpSpeed();
			} else {
				lin.y = b2Body.getLinearVelocity().y;
			} /*else {
				if (MovementProcessor.jumpingPressed
				&& ((System.currentTimeMillis() - MovementProcessor.jumpPressedTime) >= MovementProcessor.LONG_JUMP_PRESS)) {
				MovementProcessor.jumpingPressed = false;
				} else {
				if (MovementProcessor.jumpingPressed) {
				lin.y = b2Body.getLinearVelocity().y;
				// lin.y = MovementProcessor.MAX_JUMP_SPEED;
				}
				}
				}*/
		} else {
			// lin.y = b2Body.getLinearVelocity().y;
		}

		if (up) { // N - Jump
			if ((player.getWallColliding() > 0) && canClimb && climbLock) {
				lin.y = currentSpeed * modifier;
				// lin.y = b2Body.getLinearVelocity().y;
			}
		} else {
			if (down) { // S
				if ((player.getWallColliding() > 0) && canClimb && climbLock) {
					lin.y = -(currentSpeed * modifier);
					// lin.y = b2Body.getLinearVelocity().y;
				}
				if (player.isOnGround()) {
					lin.x = 0;
				}
			} else {
				// lin.y = b2Body.getLinearVelocity().y;
			}
		}

		if ((player.getWallColliding() > 0) && jump && !jumpLock) { // wallJump
			MovementProcessor.controller.setJumpLock(true);
			lin.y = player.getRace().getMaxJumpSpeed();
		}

		if (right) { // E
			if (onGroundEnum) {// climb on top wall
				lin.x = currentSpeed * modifier;
			} else
				if ((player.getHeadColliding() > 0) && canClimb && climbLock) {
					player.setPlayerState(HumanStateEnum.CLIMB_TOP);
					lin.x = currentSpeed * modifier;
				} else
					if (jumpLock) {
						/*if (player.getHeadColliding() > 0) {
							lin.y = currentSpeed * modifier;
						}*/
						lin.x = currentSpeed * modifier;
					} else {
						lin.x = (currentSpeed * modifier) / 2; // minimale rechts/links Kontrolle im Flug
					}
		} else {
			if (left) { // W
				if (onGroundEnum) {// climb on top wall
					lin.x = -currentSpeed * modifier;
				} else
					if ((player.getHeadColliding() > 0) && canClimb && climbLock) {
						player.setPlayerState(HumanStateEnum.CLIMB_TOP);
						lin.x = -currentSpeed * modifier;
					} else
						if (jumpLock) {// climb on top wall
							/*if (player.getHeadColliding() > 0) {
								lin.y = currentSpeed * modifier;
							}*/
							lin.x = -currentSpeed * modifier;
						} else {
							lin.x = -(currentSpeed * modifier) / 2; // minimale rechts/links Kontrolle im Flug
						}
			}
		}

		b2Body.setLinearVelocity(lin);
	}

	/**
	 * @param b2Body
	 * @param up
	 * @param down
	 * @param right
	 * @param left
	 * @param currentSpeed
	 * @param modifier
	 */
	public static void gravityflyingMovement2(final Body b2Body, final boolean up, final boolean down, final boolean right, final boolean left,
			final float currentSpeed, final float modifier) {
		final Vector2 toApply = new Vector2();
		final Vector2 lin = new Vector2();
		if (!up && !down && !right && !left) {
			return;
		}

		if (up) { // N - Jump
			lin.y = currentSpeed * modifier;
			toApply.y = currentSpeed * modifier;
		} else {
			if (down) { // S
				lin.y = -currentSpeed * modifier;
				toApply.y = -(currentSpeed * modifier);
			} else {
				lin.y = b2Body.getLinearVelocity().y;
			}
		}

		if (right) { // E
			lin.x = currentSpeed * modifier;
			toApply.x = currentSpeed * modifier;
		} else {
			if (left) { // W
				lin.x = -currentSpeed * modifier;
				toApply.x = -currentSpeed * modifier;
			} else {
				lin.x = b2Body.getLinearVelocity().x;
			}
		}
		b2Body.setLinearVelocity(lin);
		// b2Body.applyForceToCenter(toApply, true);
	}

	/**
	 * @param b2Body
	 * @param up
	 * @param down
	 * @param right
	 * @param left
	 * @param currentSpeed
	 * @param modifier
	 */
	public static void gravityflyingMovement1(final Body b2Body, final boolean up, final boolean down, final boolean right, final boolean left,
			final float currentSpeed, final float modifier) {
		final Vector2 toApply = new Vector2();
		final Vector2 lin = new Vector2();
		if (!up && !down && !right && !left) {
			return;
		}

		if (up) { // N - Jump
			lin.y = currentSpeed * modifier;
			toApply.y = currentSpeed * modifier;
		} else {
			if (down) { // S
				lin.y = -currentSpeed * modifier;
				toApply.y = -(currentSpeed * modifier);
			} else
				if (!Constants.IS_DOWN_GRAVITY) {
					lin.y = 0;
					toApply.y = 0;
				} else {
					lin.y = b2Body.getLinearVelocity().y;
				}
		}

		if (right) { // E
			lin.x = currentSpeed * modifier;
			toApply.x = currentSpeed * modifier;
		} else {
			if (left) { // W
				lin.x = -currentSpeed * modifier;
				toApply.x = -currentSpeed * modifier;
			} else {
				lin.x = 0;
				toApply.x = 0;
			}
		}
		b2Body.setLinearVelocity(lin);
		// b2Body.applyForceToCenter(toApply, true);
	}

	/**
	 * @param b2Body
	 * @param up
	 * @param down
	 * @param right
	 * @param left
	 * @param currentSpeed
	 * @param modifier
	 */
	public static void gravityStompDown(final Body b2Body, final boolean up, final boolean down, final boolean right, final boolean left,
			final float currentSpeed, final float modifier) {
		final Vector2 toApply = new Vector2();
		final Vector2 lin = new Vector2();
		if (!up && !down && !right && !left) {
			return;
		}

		if (up) { // N - Jump
			lin.y = currentSpeed * modifier;
			toApply.y = currentSpeed * modifier;
		} else {
			if (down) { // S
				lin.y = -currentSpeed * modifier;
				toApply.y = -(currentSpeed * modifier);
			} else {
				lin.y = b2Body.getLinearVelocity().y;
			}
		}

		if (right) { // E
			lin.x = currentSpeed * modifier;
			toApply.x = currentSpeed * modifier;
		} else {
			if (left) { // W
				lin.x = -currentSpeed * modifier;
				toApply.x = -currentSpeed * modifier;
			} else {
				lin.x = 0;
				toApply.x = 0;
			}
		}
		b2Body.setLinearVelocity(lin);
		// b2Body.applyForceToCenter(toApply, true);
	}

	private void oldMove(final Body b2Body, final boolean up, final boolean down, final boolean right, final boolean left, final float currentSpeed,
			final float modifier) {
		final Vector2 toApply = new Vector2();
		final Vector2 lin = new Vector2();
		if (!up && !down && !right && !left) {
			return;
		}

		// if ((up && (newestDirection == DirectionEnum.N)) || (up && !left && !right)
		if (up) { // N
			lin.y = currentSpeed * modifier;
			toApply.y = currentSpeed * modifier;
		} else {
			if (down) { // S
				lin.y = -currentSpeed * modifier;
				toApply.y = -(currentSpeed * modifier);
			} else
				if (!Constants.IS_DOWN_GRAVITY) {
					lin.y = 0;
					// b2Body.setLinearVelocity(b2Body.getLinearVelocity().x, 0);
					toApply.y = 0;
				} else {
					lin.y = b2Body.getLinearVelocity().y;
				}
		}

		if (right) { // E
			lin.x = currentSpeed * modifier;
			toApply.x = currentSpeed * modifier;
		} else {
			if (left) { // W
				lin.x = -currentSpeed * modifier;
				// b2Body.setLinearVelocity(0, b2Body.getLinearVelocity().y);
				toApply.x = -currentSpeed * modifier;
			} else {
				lin.x = 0;
				// b2Body.setLinearVelocity(0, b2Body.getLinearVelocity().y);
				toApply.x = 0;
			}
		}
		// System.out.println("Player lin speed: " + lin);
		b2Body.setLinearVelocity(lin);
		// b2Body.applyForceToCenter(toApply, true);
	}

}
