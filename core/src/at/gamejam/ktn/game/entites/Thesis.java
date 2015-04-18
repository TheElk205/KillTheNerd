package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Thesis extends Item {
	public Thesis(final Vector2 position, final World b2World, final boolean init) {
		super(position, b2World);
		this.numPictures = 8;
		if (init) {
			this.init(true, init);
		}
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("coin_gold");
	}

}
