package at.game.gui;

import at.game.Constants;
import at.game.managers.Assets;

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
	private final int		length;

	public Scoreboard() {
		// this.level = level;
		this.dimension = new Vector2(Constants.SCREEN_WIDTH_IN_PIXEL / 2, Constants.HEIGHT_2P);
		this.loadAssets();
		// this.npcCount = level.getNpcCount();
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
		final int posX = (int) ((Constants.SCREEN_WIDTH_IN_PIXEL - this.dimension.x) / 2);
		final int posY = (int) Constants.HEIGHT_2P;
		batch.draw(this.red, posX, posY, pos1, this.dimension.y);
		batch.draw(this.yellow, posX + pos1, posY, diff, this.dimension.y);
		batch.draw(this.green, posX + pos1 + diff, posY, this.length - pos2, this.dimension.y);
	}

	private void loadAssets() {
		this.green = Assets.getInstance().findRegion("green_pixels");
		this.red = Assets.getInstance().findRegion("red_pixels");
		this.yellow = Assets.getInstance().findRegion("yellow_pixels");
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
