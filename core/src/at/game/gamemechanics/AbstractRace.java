package at.game.gamemechanics;

import java.util.HashMap;

import at.game.gamemechanics.actions.AbstractAction;
import at.game.gamemechanics.enums.HumanStateEnum;

public class AbstractRace {
	/** not perfect, human is not allowed to fly, may be several enums */
	private HumanStateEnum							state			= HumanStateEnum.IDLE;
	private HumanStateEnum							prevState		= HumanStateEnum.IDLE;
	private final float								raceStrength	= 1f;
	/** TODO: just an idea */
	private final float								jumpPower		= this.raceStrength * 10f;
	/** TODO dont forget to convert to world-units */
	private float									width;
	private float									height;
	/** max speed */
	private final float								maxVelocity		= 10f;
	// private final float jumpVelocity = 50f;
	private final float								maxJumpSpeed	= 7f;
	/** TODO: Daempfung, maybe not needed because of box2D */
	private final float								damping			= 0.87f;
	protected final HashMap<String, AbstractAction>	actionMap		= new HashMap<String, AbstractAction>();

	public HumanStateEnum getState() {
		return this.state;
	}

	public void setState(final HumanStateEnum state) {
		if (!this.state.equals(state)) {
			this.prevState = this.state;
			System.out.println("Player set: " + state + " prevState: " + this.state);
			this.state = state;
		}
	}

	public float getMaxVelocity() {
		return this.maxVelocity;
	}

	public float getMaxJumpSpeed() {
		return this.maxJumpSpeed;
	}

	public HumanStateEnum getPrevState() {
		return this.prevState;
	}
}
