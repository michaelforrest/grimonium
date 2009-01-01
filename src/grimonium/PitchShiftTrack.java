package grimonium;

import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class PitchShiftTrack extends ChromaticTrack{

	private int cc;
	public PitchShiftTrack(XMLElement xml, MicroKontrol mk) {
		super(xml, mk);
		cc = xml.getIntAttribute("cc");
	}
	public void pressed() {
		super.pressed();
		Ableton.sendCC(cc, 0);
	}
	public void noteOnReceived(Note n) {
		if(disabled) return;
		int transpose = n.getPitch() - root;
        if(transpose < 0 || transpose > range) return;
		Ableton.sendCC(cc, transpose);
		LiveAPI.trigger(track, 0);
	}
}