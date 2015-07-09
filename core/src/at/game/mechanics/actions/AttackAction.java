package at.game.mechanics.actions;

public class AttackAction extends AbstractAction {
	private int				baseDamage;
	private AttackStatEnum	stat;
	private double			damageMultiplier;

	// ...and so on

	public void attack(final FighterType attacker, final FighterType defender) {
		defender.takeDamage(this.baseDamage + (attacker.getStatValue(this.stat) * this.damageMultiplier));
	}

	public void upStroke() {

	}

	public void downStroke() {

	}

	public void fastStroke() {

	}

	public void slowStroke() {
		// but powerfull => means more dmg
	}

	/*TODO: better use json file <Attacks>
	  <Attack name="Sword Slash" damage="10" stat="Strength" multiplier="1" />
	  <!-- More attacks here -->
	</Attacks>

	<Attack name="Fireball" damage="20">
	  <StatModifier stat="Intellect" multiplier="0.4" />
	  <StatModifier stat="Fire" multiplier="0.8" />
	</Attack>*/

	/*
	 * If you don't want to use the same basic damage formula for everything (e.g. calculate magic damage differently from physical damage), create subclasses of Attack for each formula you need and override Attack, and specify which type you want in your XML file.
	 */

}
