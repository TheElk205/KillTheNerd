package at.gamejam.ktn.game.entites;

import at.game.visuals.GameObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Thesis extends Item {
	public static int	itemCount	= 0;

	public Thesis(final Vector2 position, final World b2World, final boolean initPhysics) {
		super(position, b2World);
		this.numPictures = 6;
		Thesis.itemCount++;
		if (initPhysics) {
			this.init(true, initPhysics);
		}
	}

	@Override
	protected void loadAsset() {
		this.texture = GameObject.assets.findRegion("book_green");
	}

	@Override
	public String toString() {
		return "Book";
	}
}
