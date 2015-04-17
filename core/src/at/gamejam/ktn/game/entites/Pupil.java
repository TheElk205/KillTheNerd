package at.gamejam.ktn.game.entites;

public abstract class Pupil{
	private int status = 50; //0 - 49: asleep; 50: default; 51-100: awake
	
	private double sleepresistency = 1.0;
	private double wakeUpResistency = 1.0;
	
	private InteractiveObject isNear;
	
	public void isNear(InteractiveObject object) {
		this.isNear = object;
	}
	
	public void noLongerNear(InteractiveObject object) {
		if(object == this.isNear) {
			this.isNear = null;
		}
	}
}
