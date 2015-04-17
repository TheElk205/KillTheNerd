package at.gamejam.ktn.game.entites;

import java.util.Vector;

public abstract class Player extends InteractiveObject {
	private Vector<Item> items;
	private int maxItems;
	
	private int points = 0;
	
	public void addPoint() {
		points++;
	}
	
	public void subPoint() {
		points--;
		if(points < 0) {
			points = 0;
		}
	}
	
	public void getNear(Object o) {
		if(o instanceof Item) {
			((Item)o).grabbed();
		}
		else if(o instanceof Pupil) {
			((Pupil)o).isNear(this);
		}
	}
	
	public void throwItem(Item item) {
		
	}
	
	public abstract void hitByItem(Item item);
	
}
