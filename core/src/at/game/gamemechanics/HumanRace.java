package at.game.gamemechanics;

import at.game.gamemechanics.actions.AbstractAction;
import at.game.gamemechanics.actions.AttackAction;
import at.game.gamemechanics.actions.MoveAction;
import at.game.gamemechanics.actions.ShootAction;
import at.game.gamemechanics.actions.SwimAction;

public class HumanRace extends AbstractRace {

	public HumanRace() {
		this.actionMap.put(AbstractAction.ATTACK_KEY, new AttackAction());
		this.actionMap.put(AbstractAction.MOVE_KEY, new MoveAction());
		this.actionMap.put(AbstractAction.SHOOT_KEY, new ShootAction());
		this.actionMap.put(AbstractAction.SWIM_KEY, new SwimAction());
	}

}
