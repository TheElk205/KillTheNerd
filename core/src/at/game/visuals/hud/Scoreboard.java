package at.game.visuals.hud;

import at.game.objects.AbstractGameObject;
import at.game.utils.Constants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Koeppen Ferdinand
 * @author Herkt Kevin (modified)
 */
public class Scoreboard extends AbstractHUDElement {
	private int				sleepingCount	= 0;
	private int				awakeCount		= 0;
	private TextureRegion	green;
	private TextureRegion	red;
	private TextureRegion	yellow;
	private final int		maxValue		= 10;
	private int				posX			= 0, posY = 0;
	private final int		length;

	public Scoreboard() {
		// this.level = level;
		this.dimension = new Vector2(Constants.SCREEN_WIDTH_IN_PIXEL / 2, Constants.HEIGHT_2P);
		this.loadAssets();
		// this.npcCount = level.getNpcCount();
		final int xPos = (int) ((Constants.SCREEN_WIDTH_IN_PIXEL - this.dimension.x) / 2);
		this.setPosition(xPos, 10);
		this.length = (int) this.dimension.x;
		// System.out.println("Scoreboard - length: " + this.length);
	}

	@Override
	public void render(final SpriteBatch batch) {
		final float sleeping = (float) this.sleepingCount / (float) this.maxValue;
		final float awake = (float) this.awakeCount / (float) this.maxValue;

		final float pos1 = this.length * sleeping;
		final float pos2 = this.length - (this.length * awake);
		final float diff = pos2 - pos1;

		batch.draw(this.red, this.posX, this.posY, pos1, this.dimension.y);
		batch.draw(this.yellow, this.posX + pos1, this.posY, diff, this.dimension.y);
		batch.draw(this.green, this.posX + pos1 + diff, this.posY, this.length - pos2, this.dimension.y);
	}

	private void loadAssets() {
		this.green = AbstractGameObject.assets.findRegion("green_pixels");
		this.red = AbstractGameObject.assets.findRegion("red_pixels");
		this.yellow = AbstractGameObject.assets.findRegion("yellow_pixels");
	}

	@Override
	public void update(final float deltaTime) {
		// this.setSleepingCount(this.level.getSleepingCount());
		// this.setAwakeCount(this.level.getAwakeCount());
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

	private void setPosition(final int x, final int y) {
		this.posX = x;
		this.posY = y;
	}

	public int won() {
		if (this.maxValue == this.awakeCount) {
			return 1;
		}
		if (this.maxValue == this.sleepingCount) {
			return -1;
		}
		return 0;
	}
}
