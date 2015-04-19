package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class RedBull extends Item {
	public RedBull(final Vector2 position, final World b2World, boolean initPhysics) {
		super(position, b2World);
		this.numPictures = 6;
		this.frameDuration = 0.25f;
		if (initPhysics) {
			this.init(true, initPhysics);
		}
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("cup_coffee");
	}

}
