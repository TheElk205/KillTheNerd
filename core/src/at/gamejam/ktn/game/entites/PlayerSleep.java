package at.gamejam.ktn.game.entites;

import at.game.WorldController;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerSleep extends Player {
	public PlayerSleep(final Vector2 position, final WorldController world) {
		this.initConstructor(position, world);
		this.itemType = ItemType.THESIS;
		this.factor = -10;
		// this.handicap = 0.5f;
		this.loadPictures();
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}

	@Override
	protected void init(final boolean animation, final boolean initPhysics) {
		super.init(animation, initPhysics);
		// this.initAnimations();
	}

	private void loadPictures() {
		TextureRegion up, down, left, right;
		up = this.assets.findRegion("betty_back");
		if (up == null) {
			System.out.println("up is null");
		}
		down = this.assets.findRegion("betty_front");
		left = this.assets.findRegion("betty_left");
		right = this.assets.findRegion("betty_right");

		this.setInitialPictures(up, down, left, right);
	}
}
