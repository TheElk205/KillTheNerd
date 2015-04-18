package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class RedBull extends Item {

	public RedBull(final Vector2 position, final World b2World) {
		super(position, b2World);
		this.numPictures = 8;
		this.init(true);
	}

	public RedBull(final Vector2 position, final World b2World, final boolean physics) {
		super(position, b2World);
		this.numPictures = 8;
		this.init(true, physics);
	}

	@Override
	protected void loadAsset() {
		System.out.println("Load asset");
		this.texture = this.assets.findRegion("coin_gold");
	}

}
