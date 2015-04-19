package at.gamejam.ktn.game.entites;

import java.time.Duration;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DelayBar extends InteractiveObject {

	private float value = 100.0f;
	private TextureRegion green;
	private TextureRegion red;
	
	private Player belongsTo;
	
	private float duration = 0f;
	private float startedAt = 0f;
	private float time= 0f;
	
	private boolean started = false;
	
	public DelayBar(Player belongsto) {
		this.belongsTo = belongsto;
		this.duration = this.belongsTo.getHandicapduration();
		this.loadAssets();
		this.dimension = new Vector2(0.1f,0.1f);
	}
	
	@Override
	public void render(final SpriteBatch batch) {
		if(!started) 
			return;
		float length = this.belongsTo.dimension.y;
		float value = (time - startedAt) / duration;
		if(value >= 1 ) {
			started = false;
		}
		float pos = length * value;
		
		batch.draw(green, this.belongsTo.position.x, this.belongsTo.position.y + this.belongsTo.dimension.y, pos, this.dimension.y);
		batch.draw(red, this.belongsTo.position.x + pos, this.belongsTo.position.y + this.belongsTo.dimension.y, length -pos, this.dimension.y);
		
	}
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		time+=  deltaTime;
		if(this.belongsTo.getHandicap() != 1) {
			this.start(time);
		}
	}
	
	public void start(float time){
		if(!started) {
			started = true;
			this.startedAt = time;
			System.out.println("Started");
		}
	}
	
	private void loadAssets() {
		this.green = this.assets.findRegion("green_pixels");
		this.red = this.assets.findRegion("red_pixels");
	}

}
