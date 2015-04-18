package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.WorldController;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector2;

public class PlayerWake extends Player {

	public PlayerWake(final Vector2 position, WorldController world) {
		this.initConstructor(position, world);
		this.itemType = ItemType.REDBULL;
	}
	
	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}

	@Override
	public void hitByItem(Item item) {
		// TODO Auto-generated method stub
		
	}

public class PlayerWake extends Player {
	public PlayerWake(final Vector2 position, WorldController world) {
		this.initConstructor(position, world);
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}
}
