package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.WorldController;

import com.badlogic.gdx.math.Vector2;

public class PlayerSleep extends Player {
	public PlayerSleep(final Vector2 position, WorldController world) {
		this.initConstructor(position, world);
		this.itemType = ItemType.THESIS;
		this.factor = -5;
		//this.handicap = 0.5f;
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}
}
