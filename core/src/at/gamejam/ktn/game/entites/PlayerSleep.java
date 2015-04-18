package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.WorldController;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerSleep extends Player {
	public PlayerSleep(final Vector2 position, WorldController world) {
		this.initConstructor(position, world);
		this.itemType = ItemType.THESIS;
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}

	@Override
	public void hitByItem(Item item) {
		// TODO Auto-generated method stub
		
	}
}
