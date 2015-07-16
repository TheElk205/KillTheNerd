package at.game.managers;

import at.game.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * TODO: not used and not fully implemented, should be like "Assets"
 *
 * @author Herkt Kevin
 */
public class SoundManager {
	private static final Sound	GRAB_SOUND	= Gdx.audio.newSound(Gdx.files.internal(Constants.GRAB_SOUND));
	private final Sound			sound;

	// this.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.THROW_SOUND));
	public SoundManager() {
		final FileHandle handle = Gdx.files.internal(Constants.THROW_SOUND);
		this.sound = Gdx.audio.newSound(handle);
		// SoundManager.ingameMusic = Gdx.audio.newSound(Gdx.files.internal(Constants.MUSIC2));
		// SoundManager.winMusic = Gdx.audio.newSound(Gdx.files.internal(Constants.VICTORY));
	}

	/*
		if (this.score.won() != 0) {
			// WorldController.getInputManager().setEnabled(false);
			this.batch.draw(this.victory, 300, 300, 400, 150);
			if (!this.winMusicAlreadyStarted && (GameTitle.winMusic != null)) {
				GameTitle.ingameMusic.dispose();
				GameTitle.winMusic.play();
				this.winMusicAlreadyStarted = true;
			}
		}*/

}
