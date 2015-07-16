package at.game.managers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import at.game.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DesktopMidiPlayer {

	private Sequence	sequence;
	private Sequencer	sequencer;

	public DesktopMidiPlayer() throws MidiUnavailableException, IOException, InvalidMidiDataException {
		// Obtains the default Sequencer connected to a default device.
		Sequencer sequencer = MidiSystem.getSequencer();
		// Opens the device, indicating that it should now acquire any
		// system resources it requires and become operational.
		sequencer.open();
		// create a stream from a file
		// FileHandle fh = Gdx.files.internal(Constants.MUSIC);
		// InputStream is = new BufferedInputStream(new FileInputStream(fh.file()));
		InputStream is = new BufferedInputStream(new FileInputStream(new File(Constants.MUSIC2)));
		// Sets the current sequence on which the sequencer operates.
		// The stream must point to MIDI file data.
		sequencer.setSequence(is);
		// Starts playback of the MIDI data in the currently loaded sequence.
		sequencer.start();
		/*try {
			this.sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			// Log.error("Error opening midi device.", e);
		}*/

	}

	public void open(String fileName) {

		FileHandle file = Gdx.files.internal(fileName);
		try {
			this.sequence = MidiSystem.getSequence(file.read());
			this.sequencer.open();
			this.sequencer.setSequence(this.sequence);
		} catch (Exception e) {
			// Log.error("Error opening midi: " + fileName + ".", e);
		}
	}

	public boolean isLooping() {
		if (this.sequencer != null) {
			return this.sequencer.getLoopCount() != 0;
		}
		return false;
	}

	public void setLooping(boolean loop) {
		if (this.sequencer != null) {
			if (!loop) {
				this.sequencer.setLoopCount(0);
				return;
			}
			this.sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		}
	}

	public void play() {
		if (this.sequencer != null) {
			this.sequencer.start();
		}
	}

	public void pause() {
		stop();
	}

	public void stop() {
		if (this.sequencer != null) {
			this.sequencer.stop();
		}
	}

	public void release() {
		if (this.sequencer != null) {
			this.sequencer.close();
		}
	}

	public boolean isPlaying() {
		return this.sequencer.isRunning();
	}

	public void setVolume(float volume) {
		// Not implemented
	}
}