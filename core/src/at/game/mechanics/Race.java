package at.game.mechanics;

import at.game.mechanics.enums.HumanStateEnum;
import at.game.mechanics.enums.RaceTypeEnum;

public class Race {
	/** not perfect, human is not allowed to fly, may be several enums */
	private HumanStateEnum		state			= HumanStateEnum.IDLE;
	private HumanStateEnum		prevState		= HumanStateEnum.IDLE;
	private final float			raceStrength	= 1f;
	/** TODO: just an idea */
	private final float			jumpPower		= this.raceStrength * 10f;
	/** TODO dont forget to convert to world-units */
	private float				width;
	private float				height;
	/** max speed */
	private final float			maxVelocity		= 10f;
	// private final float jumpVelocity = 50f;
	private final float			maxJumpSpeed	= 7f;
	/** TODO: Daempfung, maybe not needed because of box2D */
	private final float			damping			= 0.87f;
	private final RaceTypeEnum	race;

	// not needed anymore because you can determine which actions are allowed over the components?
	// protected final HashMap<String, AbstractAction> actionMap = new HashMap<String, AbstractAction>();

	public Race(final RaceTypeEnum race) {
		this.race = race;
	}

	public HumanStateEnum getState() {
		return this.state;
	}

	public void setState(final HumanStateEnum state) {
		if (!this.state.equals(state)) {
			this.prevState = this.state;
			// System.out.println("Player set: " + state + " prevState: " + this.state);
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
