package grimonium;

import grimonium.set.Clip;

import java.util.Hashtable;

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

	private static LiveAPI instance;
	static int TRIGGER_TRACK = 0;
	public static int GET_CLIP_DATA = 1;
	public static int MONITOR_CLIP = 2;
	public static int STOP_CLIP_MONITOR = 3;

	public static void trigger(int track, int scene) {
		sendCC(TRIGGER_TRACK, track, scene);
	}

	private static void sendCC(int channel, int track, int scene) {
		getInstance().out.sendController(channel, track, scene);
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
	public static final byte CLIP_MONITOR_HEADER = 4;

	public static class ClipDataRequester {
		private static final int TRACK_FIELD = 2;
		private static final int SCENE_FIELD = 4;
		private static final int TEXT_START_FIELD = 7;
		public static final int CLIP_DATA_FIELD = 6;

		private final int track;
		private final int scene;
		private final Clip target;



		public ClipDataRequester(int track, int scene, Clip target) {
//			System.out.println("CREATED SYSDATA RESPONDER FOR " + track + "::" + scene);

			this.track = track;
			this.scene = scene;
			this.target = target;
			instance.in.plug(this,"sysexReceived");
			sendCC(GET_CLIP_DATA,track,scene);
			sendCC(MONITOR_CLIP,track,scene);

		}

		public void sysexReceived(SysexMessage message){
			byte[] m = message.getMessage();
//			System.out.println(message.toString() + "received by " + this);
			if(m[HEADER_FIELD] == CLIP_DATA_HEADER) {
				setClipNameData(m);
			}else{
				setClipMonitorData(message);
			}

		}
		// Track 3, scene 6 message: on, then off
//		Sysex Message:
//		0000 - f0 04 03 00 06 01 f7                             - .......
//		Sysex Message:
//		0000 - f0 04 03 00 06 00 f7                             - .......

		private void setClipMonitorData(SysexMessage message) {
//			System.out.println("Reveived monitor data in " + this);
//			System.out.println(message);
			byte[] m = message.getMessage();

			if(m[2] == this.track && m[4] == this.scene){
				target.setClipTriggered(m[5] == 1);
			}
		}
		private boolean isThisClip(byte[] m){
			int track = m[TRACK_FIELD];
			int scene = m[SCENE_FIELD];
			return (track == this.track && scene == this.scene);
		}
		private void setClipNameData(byte[] m) {
			if(isThisClip(m)){
				parseMessage(m);
				parseClipState(m);
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

		private void parseMessage(byte[] m) {
			StringBuilder builder = new StringBuilder();
			for (int i = TEXT_START_FIELD; i < m.length-1; i++) {
				byte b = m[i];
				builder.append( (char) b);
			}
			target.setClipName(builder.toString());


		}
	}
	public static String getClipName(int track, int scene, Clip target) {
		new ClipDataRequester(track,scene, target);
		return "waiting for name";
	}

	public void sysexReceived(SysexMessage message){
		//System.out.println(message.toString());
	}
}
