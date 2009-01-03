package grimonium;

import grimonium.NoteParser.BadNoteFormatException;

import java.util.Observable;

import microkontrol.MicroKontrol;
import microkontrol.controls.LED;
import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.MidiOutput;
import rwmidi.Note;

public class Ableton extends MidiThing {
	public class Message {

		private final int channel;
		private final Integer note;

		public Message(int channel, Integer note) {
			this.channel = channel;
			this.note = note;
		}
	}

	private static final int CHANNEL = 4; // always 4 for this thing. just by
											// convention.
	private static Ableton instance;
	MidiInput from;
	MidiOutput to;

	public static MidiTrack[] midiTracks = new MidiTrack[16];
	microkontrol.controls.Button playPause;
	private MicroKontrol mk;
	private boolean playing  =false;

	private Ableton(XMLElement xml) {
		mk = MicroKontrol.getInstance();
		from = getInput(xml.getStringAttribute("in"), this);
		to = getOutput(xml.getStringAttribute("out"));

		for (int i = 0; i < midiTracks.length; i++)
			midiTracks[i] = new MidiTrack(i);
		addStopButtonMappings(xml.getChildren("stop"));

		mk.plugKeyboard(new KeyboardProxy());

		playPause = mk.buttons.get("ENTER");
		playPause.listen("pressed", this, "togglePlaying");
	}

	private void addStopButtonMappings(XMLElement[] children) {
		if (children == null) return;
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			try {
				midiTracks[i].stop = new Message(element.getIntAttribute("channel") - 1,
											NoteParser.convertStringToNoteNumber(element.getStringAttribute("note")));
			} catch (BadNoteFormatException e) {
				System.out.println(e.toString());
			}
		}
	}

	public void togglePlaying() {
		playing = !playing;
		playPause.led.set(LED.BLINK);
		int cc = (playing) ? 115 : 114;
		to.sendController(15, cc, 127);
	}

	// FROM ABLETON
	public void noteOnReceived(Note n) {
//		PApplet.println("Ableton model passing on a note on");
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
		public Message stop;
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

	public static void sendCC(int cc, int value) {
		getInstance().sendController(CHANNEL, cc, value);
	}

	public static Ableton getInstance() {
		if (instance == null) PApplet.println("error - you need to call Ableton.init(xml) before using getInstance()");
		return instance;

	}

	public static void init(XMLElement xml) {
		instance = new Ableton(xml);
	}

	public static void sendCC(int channel, int cc, int value) {
		getInstance().sendController(channel, cc, value);
	}

	public static void stopTrack(int trackIndex) {
		MidiTrack track = midiTracks[trackIndex];
		System.out.println("stopping track " + trackIndex + " = " + track.stop.channel + ":" + track.stop.note);
		instance.sendNoteOn(track.stop.channel,track.stop.note, 127);

	}

	private void sendNoteOn(int channel, Integer note, int velocity) {
		to.sendNoteOn(channel, note, velocity);
	}
}
