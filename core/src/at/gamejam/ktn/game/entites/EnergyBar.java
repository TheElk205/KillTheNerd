package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnergyBar extends InteractiveObject {

	private float value = 50.0f;
	private TextureRegion green;
	private TextureRegion red;
	private TextureRegion sleep;
	private TextureRegion sun;
	
	private NPC belongsTo;
	private int state = 0; //-1 asleep, 1 awake
	
	public EnergyBar(NPC belongsTo) {
		this.belongsTo = belongsTo;
		this.loadAssets();
		this.dimension = new Vector2(0.1f,0.1f);
	}
	
	@Override
	public void update(float deltaTime) {
		this.value = belongsTo.getEnergy();
		this.state = belongsTo.getState();
		
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(final SpriteBatch batch) {
		float length = this.belongsTo.dimension.y;
		float pos = length * value / 100;
		
		batch.draw(green, this.belongsTo.position.x, this.belongsTo.position.y + this.belongsTo.dimension.y, pos, this.dimension.y);
		batch.draw(red, this.belongsTo.position.x + pos, this.belongsTo.position.y + this.belongsTo.dimension.y, length -pos, this.dimension.y);
		
		if(state == -1) {
			batch.draw(sleep, this.belongsTo.position.x + this.belongsTo.dimension.x /2f, this.belongsTo.position.y + this.belongsTo.dimension.y, this.dimension.x*5, this.dimension.y*5);
		}
		if(state == 1) {
			batch.draw(sun, this.belongsTo.position.x + this.belongsTo.dimension.x /2f, this.belongsTo.position.y + this.belongsTo.dimension.y, this.dimension.x*5, this.dimension.y*5);
		}
		System.out.println(this.belongsTo.dimension.x);
	}
	
	private void loadAssets() {
		this.green = this.assets.findRegion("green_pixels");
		this.red = this.assets.findRegion("red_pixels");
		this.sleep = this.assets.findRegion("sleep");
		this.sun = this.assets.findRegion("sun");
	}
	
	public void editValue(float fac) {
		this.value += fac;
	}

}
