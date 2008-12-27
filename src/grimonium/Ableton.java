package grimonium;

import java.util.Observable;

import microkontrol.MicroKontrol;
import processing.core.PApplet;
import rwmidi.MidiInput;
import rwmidi.MidiOutput;
import rwmidi.Note;

public class Ableton extends MidiThing {
	MidiInput from;
	MidiOutput to;

	MidiTrack[] midiTracks = new MidiTrack[16];
	microkontrol.controls.Button playPause;
	private MicroKontrol mk;

	Ableton(MicroKontrol mk) {
		this.mk = mk;
		from = getInput("IAC Bus 1", this);
		to = getOutput("IAC Bus 2(.*)Apple Computer, Inc.");

		for (int i = 0; i < midiTracks.length; i++)
			midiTracks[i] = new MidiTrack(i);
		mk.plugKeyboard(new KeyboardProxy());

		playPause = mk.buttons.get("ENTER");
		playPause.listen("pressed", this, "togglePlaying");
	}

	public void togglePlaying() {
		playPause.toggle();
		int cc = (playPause.isOn()) ? 115 : 114;
		to.sendController(15, cc, 127);
	}

	// FROM ABLETON
	public void noteOnReceived(Note n) {
		midiTracks[n.getChannel()].noteOnReceived(n);
	}

	public void noteOffReceived(Note n) {
		midiTracks[n.getChannel()].noteOffReceived(n);
	}

	public void sendController(int channel, int cc, int value) {
		PApplet.println("should send " + channel + "," + cc + "," + value);
		to.sendController(channel, cc, value);
	}

	// TO ABLETON
	public class KeyboardProxy {
		public void noteOnReceived(Note n) {
			to.sendNoteOn(0, n.getPitch(), n.getVelocity());
		}

		public void noteOffReceived(Note n) {
			to.sendNoteOff(0, n.getPitch(), n.getVelocity());
		}
	}

	public class MidiTrack extends Observable {
		Note lastNote;
		int channel;

		MidiTrack(int channel) {
			this.channel = channel;
		}

		public void noteOnReceived(Note n) {
			lastNote = n;
			setChanged();
			notifyObservers("note_on");
		}

		public void noteOffReceived(Note n) {
			lastNote = n;
			setChanged();
			notifyObservers("note_off");
		}
	}

}
