package at.game.mechanics.actions;

public class FighterType {

	private int		agility;
	private int		intellect;
	private int		strength;

	private double	health	= 1000;

	public FighterType() {
		// TODO Auto-generated constructor stub
	}

	public int getStatValue(final AttackStatEnum stat) {
		switch (stat) {
			case Agility:
				return this.agility;
			case Intellect:
				return this.intellect;
			case Strength:
				return this.strength;
			default:
				return -1;
		}
	}

	public double getHealth() {
		return this.health;
	}

	public void reduceHealth(final double reduce) {
		this.health -= reduce;
	}

	public void takeDamage(final double d) {
		this.health -= d;

	}

}
