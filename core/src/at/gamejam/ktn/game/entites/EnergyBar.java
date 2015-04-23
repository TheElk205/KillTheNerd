package at.gamejam.ktn.game.entites;

import at.gamejam.ktn.game.entities.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnergyBar extends InteractiveObject {

	private float					value	= 50.0f;
	private static TextureRegion	green;
	private static TextureRegion	red;
	private static TextureRegion	sleep;
	private static TextureRegion	sun;

	private final NPC				belongsTo;
	private int						state	= 0;		// -1 asleep, 1 awake

	static {
		EnergyBar.loadAssets();
	}

	public EnergyBar(final NPC belongsTo) {
		this.belongsTo = belongsTo;

		this.dimension = new Vector2(0.1f, 0.1f);
	}

	@Override
	public void update(final float deltaTime) {
		this.value = this.belongsTo.getEnergy();
		this.state = this.belongsTo.getState();

	}

	@Override
	public void render(final SpriteBatch batch) {
		final float length = this.belongsTo.dimension.y;
		final float pos = (length * this.value) / 100;

		batch.draw(EnergyBar.green, this.belongsTo.position.x, this.belongsTo.position.y + this.belongsTo.dimension.y, pos, this.dimension.y);
		batch.draw(EnergyBar.red, this.belongsTo.position.x + pos, this.belongsTo.position.y + this.belongsTo.dimension.y, length - pos, this.dimension.y);

		if (this.state == -1) {
			batch.draw(EnergyBar.sleep, this.belongsTo.position.x + (this.belongsTo.dimension.x / 2f), this.belongsTo.position.y + this.belongsTo.dimension.y, this.dimension.x * 5,
					this.dimension.y * 5);
		}
		if (this.state == 1) {
			batch.draw(EnergyBar.sun, this.belongsTo.position.x + (this.belongsTo.dimension.x / 2f), this.belongsTo.position.y + this.belongsTo.dimension.y, this.dimension.x * 5, this.dimension.y * 5);
		}
		// System.out.println(this.belongsTo.dimension.x);
	}

	private static void loadAssets() {
		EnergyBar.green = GameObject.assets.findRegion("green_pixels");
		EnergyBar.red = GameObject.assets.findRegion("red_pixels");
		EnergyBar.sleep = GameObject.assets.findRegion("sleep");
		EnergyBar.sun = GameObject.assets.findRegion("sun");
	}

	public void editValue(final float fac) {
		this.value += fac;
	}

}
