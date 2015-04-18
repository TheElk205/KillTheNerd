package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.WorldController;

import com.badlogic.gdx.math.Vector2;

public class PlayerSleep extends Player {
	public PlayerSleep(final Vector2 position, WorldController world) {
		this.initConstructor(position, world);
	}

	@Override
	protected void loadAsset() {
		this.texture = this.assets.findRegion("player");
	}
}
