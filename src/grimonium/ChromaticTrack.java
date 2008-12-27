package grimonium;

import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class ChromaticTrack {

	private int root;
	private int track;
	private final MicroKontrol mk;
	private final LiveAPI liveAPI;
	private int range;

	public ChromaticTrack(XMLElement xml, MicroKontrol mk, LiveAPI liveAPI) {
		this.mk = mk;
		this.liveAPI = liveAPI;
		range = xml.getIntAttribute("range");
		root = xml.getIntAttribute("root");
		track = xml.getIntAttribute("track");
		mk.plugKeyboard(this);
	}
	public void noteOnReceived(Note n) {
		int scene = n.getPitch() - root;
		if(scene < 0 || scene > range) return;
		liveAPI.trigger(track, scene);
	}

	public void noteOffReceived(Note n) {
		//to.sendNoteOff(0, n.getPitch(), n.getVelocity());
	}

}
