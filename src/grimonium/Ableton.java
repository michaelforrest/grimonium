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
import rwmidi.SysexMessage;

public class Ableton extends MidiThing {
	public class Message {
		public final int channel;
		public final Integer note;
		public Message(int channel, Integer note) {
			this.channel = channel;
			this.note = note;
		}
	}
	public class CC extends Message{
		public final int cc;
		public CC(int channel, int cc) {
			super(channel, null);
			this.cc = cc;
		}
		public CC(XMLElement xml) {
			super(xml.getIntAttribute("channel")-1, null);
			cc = xml.getIntAttribute("cc");
		}

	}
	private static Ableton instance;
	public MidiInput from;
	public MidiOutput to;

	public static MidiTrack[] midiTracks = new MidiTrack[16];
	microkontrol.controls.Button playPause;
	private MicroKontrol mk;
	private boolean playing  =false;
	private CC stop;
	private CC play;
	private final PApplet applet;
	private int visualsTrack;
//	 private Clock clock;

	private Ableton(XMLElement xml, PApplet applet) {
		this.applet = applet;
		applet.registerDispose(this);
		mk = MicroKontrol.getInstance();
		String inputName = xml.getStringAttribute("in");
		from = getInput(inputName, this);
		to = getOutput(xml.getStringAttribute("out"));

		for (int i = 0; i < midiTracks.length; i++)
			midiTracks[i] = new MidiTrack(i);
		addStopButtonMappings(xml.getChildren("stop"));
		addFaderMappings(xml.getChildren("fader"));

		stop = new CC(xml.getChild("globalstop"));
		play = new CC(xml.getChild("globalplay"));

		visualsTrack = xml.getIntAttribute("visuals_track");

		//mk.plugKeyboard(new KeyboardProxy());

		playPause = mk.buttons.get("ENTER");
		playPause.listen("pressed", this, "togglePlaying");
//		clock = new Clock(inputName);
	}
	public void dispose(){
		sendCC(stop, 127);
	}

	private void sendCC(CC cc, int value) {
		sendCC(cc.channel,cc.cc, value);
	}
	private void addFaderMappings(XMLElement[] children) {
		if (children == null) return;
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			midiTracks[i].fader = new CC(element.getIntAttribute("channel") - 1,element.getIntAttribute("cc"));
		}
	}

	private void addStopButtonMappings(XMLElement[] children) {
		if (children == null) return;
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			try {
				midiTracks[i].stop = new Message(element.getIntAttribute("channel") - 1,
											NoteParser.getNote(element.getStringAttribute("note")));
			} catch (BadNoteFormatException e) {
				System.out.println(e.toString());
			}
		}
	}

	public void togglePlaying() {
		playing = !playing;
		playPause.led.set( playing ? LED.ON : LED.OFF);
		sendCC(playing ? play : stop, 127);
	}

	// FROM ABLETON
	public void noteOnReceived(Note n) {
//		PApplet.println("Ableton model passing on a note on");
		midiTracks[n.getChannel()].noteOnReceived(n);
	}

	public void noteOffReceived(Note n) {
		midiTracks[n.getChannel()].noteOffReceived(n);
	}
//	public void controllerChangeReceived(Controller c){
//		System.out.println("cc: " + c);
//	}
	public void sysexReceived(SysexMessage m){
		System.out.println("sysex: " + m);
	}

	public void sendController(int channel, int cc, int value) {
		//PApplet.println("should send " + channel + "," + cc + "," + value);
		to.sendController(channel, cc, value);
	}


	public class MidiTrack extends Observable {

		public static final String NOTE_OFF = "note_off";
		public static final String NOTE_ON = "note_on";
		public CC fader;
		public Message stop;
		public Note lastNote;
		int channel;

		MidiTrack(int channel) {
			this.channel = channel;
		}

		public void noteOnReceived(Note n) {
			lastNote = n;
			setChanged();
			notifyObservers(NOTE_ON);
		}

		public void noteOffReceived(Note n) {
			lastNote = n;
			setChanged();
			notifyObservers(NOTE_OFF);
		}
	}

	public static Ableton getInstance() {
		if (instance == null) PApplet.println("error - you need to call Ableton.init(xml) before using getInstance()");
		return instance;

	}

	public static void init(XMLElement xml, PApplet applet) {
		instance = new Ableton(xml, applet);
	}

	public static void sendCC(int channel, int cc, int value) {
		getInstance().sendController(channel, cc, value);
	}

	public static void stopTrack(int trackIndex) {
		MidiTrack track = midiTracks[trackIndex];
		System.out.println("stopping track " + trackIndex + " = " + track.stop.channel + ":" + track.stop.note);
		sendNoteOn(track.stop.channel,track.stop.note, 127);

	}

	public static void sendNoteOn(int channel, Integer pitch, int velocity) {
		getInstance().to.sendNoteOn(channel, pitch, velocity);
	}

	public static void sendNoteOff(int channel, int pitch, int velocity) {
		getInstance().to.sendNoteOff(channel, pitch, velocity);
	}
	public static void fadeTrack(int track, int value) {
		CC fader = midiTracks[track].fader;
		sendCC(fader.channel, fader.cc,value);

	}
	public static void sendPitchBend(int track, int value) {
		//getInstance().to.send

	}
	public static int getVisualsTrack() {
		return getInstance().visualsTrack;
	}
}
