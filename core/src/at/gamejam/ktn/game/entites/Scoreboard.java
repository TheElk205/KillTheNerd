package at.gamejam.ktn.game.entites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import at.gamejam.ktn.game.GeneratedLevel;

public class Scoreboard extends InteractiveObject {
	public GeneratedLevel level;
	
	private int sleepingCount = 0;
	private int awakeCount = 0;
	
	private TextureRegion green;
	private TextureRegion red;
	private TextureRegion yellow;
	
	private int npcCount = 0;
	
	public Scoreboard(GeneratedLevel level, int npcCount) {
		this.level = level;
		this.dimension = new Vector2(0.1f,0.1f);
		this.loadAssets();
		this.npcCount = npcCount;
	}
	
	@Override
	public void render(final SpriteBatch batch) {
		float length = 3;
		float sleeping = sleepingCount / npcCount;
		float awake = awakeCount / npcCount;
		
		float pos1 = length * sleeping;
		float pos2 = length - length * awake;
		
		float diff = pos2-pos1;
		
		float startx = 0.0f;
		float starty = 0.0f;
		
		batch.draw(green, startx, starty, pos1, this.dimension.y);
		batch.draw(yellow, startx + pos1, starty, diff, this.dimension.y);
		batch.draw(red, startx+pos1+diff, starty, length - pos2, this.dimension.y);
	}
	
	private void loadAssets() {
		this.green = this.assets.findRegion("green_pixels");
		this.red = this.assets.findRegion("red_pixels");
		this.yellow = this.assets.findRegion("yellow_pixels");
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void setSleepingCount(int c) {
		this.sleepingCount = c;
	}
	
	public void setAwakeCount(int c) {
		this.sleepingCount = c;
	}
}
