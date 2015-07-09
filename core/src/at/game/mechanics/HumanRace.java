package at.game.mechanics;

import at.game.mechanics.actions.AbstractAction;
import at.game.mechanics.actions.AttackAction;
import at.game.mechanics.actions.MoveAction;
import at.game.mechanics.actions.ShootAction;
import at.game.mechanics.actions.SwimAction;

public class HumanRace extends AbstractRace {

	public HumanRace() {
		this.actionMap.put(AbstractAction.ATTACK_KEY, new AttackAction());
		this.actionMap.put(AbstractAction.MOVE_KEY, new MoveAction());
		this.actionMap.put(AbstractAction.SHOOT_KEY, new ShootAction());
		this.actionMap.put(AbstractAction.SWIM_KEY, new SwimAction());
	}

}
