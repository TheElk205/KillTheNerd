package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.WorldController;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerWake extends Player {
	public PlayerWake(final Vector2 position, WorldController world) {
		this.initConstructor(position, world);
		this.itemType = ItemType.REDBULL;
		this.factor = 5;
		this.loadPictures();
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}
	
	@Override
	protected void init(boolean animation, boolean initPhysics) {
		super.init(animation,initPhysics);
		//this.initAnimations();
	}
	private void loadPictures() {
		TextureRegion up, down, left, right;
		up = this.assets.findRegion("george_back");
		if(up == null) {
			System.out.println("up is null");
		}
		down = this.assets.findRegion("george_front");
		left = this.assets.findRegion("george_left");
		right = this.assets.findRegion("george_right");
		
		this.setInitialPictures(up, down, left, right);
	}
}
