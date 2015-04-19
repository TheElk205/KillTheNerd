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
	
	private int posX = 0, posY = 0;
	public Scoreboard(GeneratedLevel level) {
		this.level = level;
		this.dimension = new Vector2(500f,20);
		this.loadAssets();
		this.npcCount = level.getNpcCount();
	}
	
	@Override
	public void render(final SpriteBatch batch) {
		float length = this.dimension.x;
		float sleeping = (float)sleepingCount / (float)npcCount;
		float awake = (float)awakeCount / (float)npcCount;
		
		float pos1 = length * sleeping;
		float pos2 = length - length * awake;
		
		System.out.println("Pos1: " +pos1 + " Sleeping: " + sleepingCount);
		System.out.println("Pos2: " + pos2 + " awake: " + awakeCount);
		float diff = pos2-pos1;
		
		batch.draw(red, posX, posY, pos1, this.dimension.y);
		batch.draw(yellow, posX + pos1, posY, diff, this.dimension.y);
		batch.draw(green, posX+pos1+diff, posY, length - pos2, this.dimension.y);
	}
	
	private void loadAssets() {
		this.green = this.assets.findRegion("green_pixels");
		this.red = this.assets.findRegion("red_pixels");
		this.yellow = this.assets.findRegion("yellow_pixels");
	}

	@Override
	public void update(float deltaTime) {
		this.setSleepingCount(this.level.getSleepingcount());
		this.setAwakeCount(this.level.getAwakecoutn());
		
	}
	
	public void setSleepingCount(int c) {
		this.sleepingCount = c;
	}
	
	public void setAwakeCount(int c) {
		this.awakeCount = c;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public void setPosition(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	
	public int won() {
		if(this.npcCount == this.awakeCount) {
			return 1;
		}
		if(this.npcCount == this.sleepingCount) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
