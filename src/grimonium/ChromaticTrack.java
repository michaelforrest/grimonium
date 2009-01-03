package grimonium;

import microkontrol.MicroKontrol;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.FaderListener;
import microkontrol.controls.Pad;
import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.Note;

public class ChromaticTrack implements ButtonListener, FaderListener {

	protected int root;
	protected int track;
	protected int range;
	protected boolean disabled = false;
	protected Pad pad;
	private int faderChannel;
	private int faderCC;

	public ChromaticTrack(XMLElement xml) {
		MicroKontrol mk = MicroKontrol.getInstance();
		range = xml.getIntAttribute("range");
		root = xml.getIntAttribute("root");
		track = xml.getIntAttribute("track");
		pad = mk.pads[xml.getIntAttribute("pad")];
		pad.set(!disabled);
		pad.listen(this);
		setupFader(xml.getChild("fader"),mk);
		mk.plugKeyboard(this);

	}

	private void setupFader(XMLElement xml, MicroKontrol mk) {
		if(xml == null ) return;
		mk.faders[xml.getIntAttribute("id")].listen(this);
		faderCC = xml.getIntAttribute("cc");
		faderChannel= xml.getIntAttribute("channel");

	}

	public void noteOnReceived(Note n) {
		if (disabled) return;
		int scene = n.getPitch() - root;
		if (scene < 0 || scene > range) return;
		LiveAPI.trigger(track, scene);
	}

	public void noteOffReceived(Note n) {
		// to.sendNoteOff(0, n.getPitch(), n.getVelocity());
	}

	public void pressed() {
		disabled = !disabled;
		pad.set(!disabled);

	}

	public void released() {
		// TODO Auto-generated method stub

	}

	public void updated() {
		// TODO Auto-generated method stub

	}

	public void moved(Float value) {
		PApplet.println("Fader is now " + value);
		Ableton.sendCC(faderChannel, faderCC, (int) (value*127));
	}

}
