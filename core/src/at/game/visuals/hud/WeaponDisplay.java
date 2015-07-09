package at.game.visuals.hud;

import at.game.managers.Assets;
import at.game.mechanics.Player;
import at.game.utils.Constants;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WeaponDisplay extends AbstractHUDElement {
	private final Player		player;
	private final float			bookPosition	= Constants.HEIGHT_8P;
	private final TextureRegion	texture;

	public WeaponDisplay(final Player player) {
		this.player = player;
		this.texture = Assets.getInstance(new AssetManager()).findRegion("book_green_small");
		this.texture.flip(false, true);

	}

	@Override
	public void update(final float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(final SpriteBatch batch) {
		this.drawMunition(batch);

	}

	private void drawMunition(final SpriteBatch batch) {
		Constants.FONT.draw(batch, "Mun: " + Integer.toString(this.player.getItemCount()), Constants.WIDTH_1P, Constants.HEIGHT_4P);
		final float sizeOfImage = Constants.WIDTH_2P;
		// this.font.draw(this.batch, Integer.toString(WorldController.playerWake.getItemCount()), 980, 40);
		float offset = 0;
		/*for (int i = 0; i < WorldController.playerWake.getItemCount(); i++) {
			this.batch.draw(this.redbullTexture, 1000, this.redBullPosition + offset, sizeOfImage, sizeOfImage);
			offset += 30;
		}*/

		offset = 0;
		for (int i = 0; i < this.player.getItemCount(); i++) {
			batch.draw(this.texture, Constants.WIDTH_2P, this.bookPosition + offset, sizeOfImage, sizeOfImage);
			offset += sizeOfImage;
		}
	}

}
