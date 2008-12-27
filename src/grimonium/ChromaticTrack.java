package grimonium;

import microkontrol.MicroKontrol;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.Pad;
import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.Note;

public class ChromaticTrack implements ButtonListener {

    protected int root;
    protected int track;
    protected int range;
    protected boolean disabled = false;
	protected Pad pad;

    public ChromaticTrack(XMLElement xml, MicroKontrol mk) {

        range = xml.getIntAttribute("range");
        root = xml.getIntAttribute("root");
        track = xml.getIntAttribute("track");
        pad = mk.pads[xml.getIntAttribute("pad")];
        pad.set(!disabled);
        pad.listen(this);
        mk.plugKeyboard(this);

    }
    public void noteOnReceived(Note n) {
        if(disabled) return;
        int scene = n.getPitch() - root;
        if(scene < 0 || scene > range) return;
        LiveAPI.trigger(track, scene);
    }

    public void noteOffReceived(Note n) {
        //to.sendNoteOff(0, n.getPitch(), n.getVelocity());
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

}
