package at.game.mechanics.enums;

public enum HumanStateEnum {
	/** also called ON_GROUND */
	WALKING, RUNNING, SWIMMING, JUMPING, FALLING, FLYING, SHOOTING, ATTACKING, DYING, IDLE, BUSY, NOT_ON_GROUND, CLIMB, CLIMB_TOP
	// eventuell wird shooting und attacking nicht benötigt
	// man fragt auf swimming ab, und kann dann anders oder gar nicht attackieren
}
