package grimonium.maps;

import grimonium.Ableton;
import grimonium.LiveAPI;
import processing.xml.XMLElement;
import rwmidi.Note;

public class PitchShiftTrack extends ChromaticTrack{

	private int cc;
	public PitchShiftTrack(XMLElement xml) {
		super(xml);
		cc = xml.getIntAttribute("cc");
	}
	public void pressed() {
		super.pressed();
		Ableton.sendCC(4,cc, 0);
	}
	public void noteOnReceived(Note n) {
		if(disabled) return;
		int transpose = n.getPitch() - root;
        if(transpose < 0 || transpose > range) return;
		Ableton.sendCC(4,cc, transpose);
		LiveAPI.trigger(track, 0);
	}
}
