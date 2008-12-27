package grimonium;

import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class MidiEffectChromaticTrack extends ChromaticTrack{

	private int cc;
	public MidiEffectChromaticTrack(XMLElement xml, MicroKontrol mk) {
		super(xml, mk);
		cc = xml.getIntAttribute("cc");
	}
	public void pressed() {
		super.pressed();
		Ableton.sendCC(cc, 0);
	}
	public void noteOnReceived(Note n) {
		int transpose = n.getPitch() - root;
        if(transpose < 0 || transpose > range) return;
		Ableton.sendCC(cc, transpose);
		LiveAPI.trigger(track, 0);
	}
}
