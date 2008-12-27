package grimonium;

import java.util.Hashtable;

import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.MidiOutput;
// http://www.assembla.com/wiki/show/live-api/API_Midi_Protocol
public class LiveAPI extends MidiThing {
	Hashtable<Integer, String> STATUS_BYTES = new Hashtable<Integer, String>();
	private MidiOutput out;

	private LiveAPI(XMLElement xml) {
		setStatusBytes();
		out = getOutput(xml.getStringAttribute("out"));
	}

	void setStatusBytes() {
		STATUS_BYTES.put(0, "Information");
		STATUS_BYTES.put(1, "Clip Data");
		STATUS_BYTES.put(2, "Empty Clip Slot");
		STATUS_BYTES.put(3, "Clip is_triggered");
		STATUS_BYTES.put(4, "Clip is_playing");
		STATUS_BYTES.put(5, "Clip name");
		STATUS_BYTES.put(6, "Clip is_looping");
		STATUS_BYTES.put(10, "Track data");
		STATUS_BYTES.put(11, "No Track data");
		STATUS_BYTES.put(12, "Track name");
		STATUS_BYTES.put(13, "Track is_armed");
		STATUS_BYTES.put(14, "Track is_muted");
		STATUS_BYTES.put(15, "Track is_soloed");
		STATUS_BYTES.put(20, "Device Data");
		STATUS_BYTES.put(21, "No Device Data");
		STATUS_BYTES.put(30, "Scene Data");
		STATUS_BYTES.put(31, "No Scene Data");
		STATUS_BYTES.put(31, "Scene name");
		STATUS_BYTES.put(40, "Cuepoint Data");
		STATUS_BYTES.put(41, "No Cuepoint data");
		STATUS_BYTES.put(42, "Cuepoint name");
		STATUS_BYTES.put(43, "Cuepoint time");
		STATUS_BYTES.put(50, "Mixer data");
		STATUS_BYTES.put(51, "No mixer data");
		STATUS_BYTES.put(60, "Song data");
	}

	static int TRIGGER_TRACK = 0;
	private static LiveAPI instance;
	int GET_CLIP_DATA = 1;
	int MONITOR_CLIP = 2;
	int STOP_CLIP_MONITOR = 3;

	public static void trigger(int track, int scene) {
		getInstance().out.sendController(TRIGGER_TRACK, track, scene);
	}

	private static LiveAPI getInstance() {
		if(instance == null) PApplet.println("Error! You need to call LiveAPI.init()");
		return instance;
	}

	public void stop(int track) {
		out.sendController(TRIGGER_TRACK, track, 20);
	}

	public static void init(XMLElement xml) {
		instance = new LiveAPI(xml);

	}
}
