package at.gamejam.ktn.game.entites;

import at.game.GeneratedLevel;
import at.game.visuals.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Scoreboard extends InteractiveObject {
	public GeneratedLevel	level;

	private int				sleepingCount	= 0;
	private int				awakeCount		= 0;

	private TextureRegion	green;
	private TextureRegion	red;
	private TextureRegion	yellow;

	private int				npcCount		= 0;

	private int				posX			= 0, posY = 0;

	public Scoreboard(final GeneratedLevel level) {
		this.level = level;
		this.dimension = new Vector2(500f, 20);
		this.loadAssets();
		this.npcCount = level.getNpcCount();
	}

	@Override
	public void render(final SpriteBatch batch) {
		final float length = this.dimension.x;
		final float sleeping = (float) this.sleepingCount / (float) this.npcCount;
		final float awake = (float) this.awakeCount / (float) this.npcCount;

		final float pos1 = length * sleeping;
		final float pos2 = length - (length * awake);

		// System.out.println("Pos1: " +pos1 + " Sleeping: " + sleepingCount);
		// System.out.println("Pos2: " + pos2 + " awake: " + awakeCount);
		final float diff = pos2 - pos1;

		batch.draw(this.red, this.posX, this.posY, pos1, this.dimension.y);
		batch.draw(this.yellow, this.posX + pos1, this.posY, diff, this.dimension.y);
		batch.draw(this.green, this.posX + pos1 + diff, this.posY, length - pos2, this.dimension.y);
	}

	private void loadAssets() {
		this.green = GameObject.assets.findRegion("green_pixels");
		this.red = GameObject.assets.findRegion("red_pixels");
		this.yellow = GameObject.assets.findRegion("yellow_pixels");
	}

	@Override
	public void update(final float deltaTime) {
		this.setSleepingCount(this.level.getSleepingCount());
		this.setAwakeCount(this.level.getAwakeCount());

	}

	public void setSleepingCount(final int c) {
		this.sleepingCount = c;
	}

	public void setAwakeCount(final int c) {
		this.awakeCount = c;
	}

	public void setPosX(final int posX) {
		this.posX = posX;
	}

	public void setPosY(final int posY) {
		this.posY = posY;
	}

	public void setPosition(final int x, final int y) {
		this.posX = x;
		this.posY = y;
	}

	public int won() {
		if (this.npcCount == this.awakeCount) {
			return 1;
		}
		if (this.npcCount == this.sleepingCount) {
			return -1;
		}
		return 0;
	}

	@Override
	public void initPhysics() {
		// nothing to init for scoreboard
	}
}
