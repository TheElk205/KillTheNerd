package at.game.components;

import at.game.mechanics.Item;

public class CollisionComponent extends AbstractComponent {

	public CollisionComponent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param item
	 * @return returns true if player "dead"
	 */
	public boolean hitByItem(final Item item) {
		// System.out.println(this + " hit by " + item);
		/*if ((item instanceof RedBull) && (this instanceof PlayerSleep)) {
				// this.health = this.health - 50;
			}
			TODO: switch cases, because different items have different dmg else if(item instanceof Thesis && this instanceof PlayerWake) {
				health = health - 20;
			}*/

		/*if (this.health <= 0) {
			return true;
			// this.getBody().setActive(false);
			// WorldController.destroyBody(getBody());
		}*/
		return false;
	}

}
