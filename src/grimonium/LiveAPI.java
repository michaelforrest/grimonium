package grimonium;

import java.util.Hashtable;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.MidiInput;
import rwmidi.MidiOutput;
import rwmidi.SysexMessage;
// http://www.assembla.com/wiki/show/live-api/API_Midi_Protocol
public class LiveAPI extends MidiThing {

	Hashtable<Integer, String> STATUS_BYTES = new Hashtable<Integer, String>();
	private MidiOutput out;
	private MidiInput in;

	private LiveAPI(XMLElement xml) {
		setStatusBytes();
		out = getOutput(xml.getStringAttribute("out"));
		in = getInput(xml.getStringAttribute("in"), this);
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
	static int GET_CLIP_DATA = 1;
	static int MONITOR_CLIP = 2;
	int STOP_CLIP_MONITOR = 3;

	public static void trigger(int track, int scene) {
		sendCC(TRIGGER_TRACK, track, scene);
	}

	private static void sendCC(int message, int track, int scene) {
		getInstance().out.sendController(message, track, scene);
	}

	private static LiveAPI getInstance() {
		if(instance == null) PApplet.println("Error! You need to call LiveAPI.init()");
		return instance;
	}

	public static void init(XMLElement xml) {
		instance = new LiveAPI(xml);
	}
	private static final byte CLIP_DATA_HEADER = 1;
	public static final int HEADER_FIELD = 1;

	public static class ClipDataRequester {
		private static final int TRACK_FIELD = 2;
		private static final int SCENE_FIELD = 4;
		private static final int TEXT_START_FIELD = 7;
		public static final int CLIP_DATA_FIELD = 6;

		private final int track;
		private final int scene;
		private final ClipDataResponder target;
		private boolean disabled;


		public ClipDataRequester(int track, int scene, ClipDataResponder target) {
//			System.out.println("CREATED SYSDATA RESPONDER FOR " + track + "::" + scene);

			this.track = track;
			this.scene = scene;
			this.target = target;
			instance.in.plug(this,"sysexReceived");
			sendCC(GET_CLIP_DATA,track,scene);
		}

		public void sysexReceived(SysexMessage message){

			if(disabled) {
//				System.out.println("returning cos " + this + " is disabled");
				return;
			}
			byte[] m = message.getMessage();
			if(m[HEADER_FIELD] != CLIP_DATA_HEADER) return;
			int track = m[TRACK_FIELD];
			int scene = m[SCENE_FIELD];
			if(track == this.track && scene == this.scene){
				System.out.println(message.toString() + "received by " + this);
				parseMessage(m);
				parseClipState(m);
//				System.out.println("DELETING SYSDATA RESPONDER FOR " + track + "::" + scene);
				//instance.in.unplug(this);
				disable(this);
			}

		}
		/*
		 	PLAYING
			0000 - f0 01 00 00 00 16 1d 42 6f 73 62 6f 20 42 65 61  - .......Bosbo Bea
			data[6] = "11101" -- look for &(00100) = &(4)
			NOT PLAYING
								s
			0000 - f0 01 00 00 01 16 19 42 6f 73 62 6f 20 42 65 61  - .......Bosbo Bea
			data[6] = "11001"
		 */
		private void parseClipState(byte[] m) {
			byte data = m[CLIP_DATA_FIELD];
			target.setClipTriggered((data & 4) > 0);
		}

		// OPTIMIZE (this is a memory leak now)
		// or just call it once, or only set it up once. I dunno
		private void disable(ClipDataRequester clipDataRequester) {
			disabled = true;
		}

		private void parseMessage(byte[] m) {
			StringBuilder builder = new StringBuilder();
			for (int i = TEXT_START_FIELD; i < m.length-1; i++) {
				byte b = m[i];
				builder.append( (char) b);
			}
			target.setClipName(builder.toString());


		}
	}
	public static String getClipName(int track, int scene, ClipDataResponder target) {
		ClipDataRequester requester = new ClipDataRequester(track,scene, target);
		return "waiting for name";
	}
	public void sysexReceived(SysexMessage message){
		//System.out.println(message.toString());
	}
}
