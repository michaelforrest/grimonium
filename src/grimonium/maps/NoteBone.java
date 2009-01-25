package grimonium.maps;

import grimonium.Ableton;
import grimonium.Animata;
import grimonium.NoteParser;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.xml.XMLElement;
@Deprecated
public class NoteBone extends MapBase implements Observer {


	int channel;
	int note;
	String bone;
	Ableton.MidiTrack model;
	private final float range;

	public NoteBone(XMLElement xml) {
		channel = 10; // xml.getIntAttribute("channel", 1) - 1;

		try {
			note = NoteParser.getNote(xml);
		} catch (NoteParser.BadNoteFormatException e) {
			PApplet.println(e.getMessage());
		}
		bone = xml.getStringAttribute("bone");
		range = xml.getFloatAttribute("range",1f);

		model = Ableton.midiTracks[channel];
		model.addObserver(this);
	}

	public void update(Observable o, Object e) {
		if (model.lastNote.getPitch() != note) return;
		float value = (e.equals("note_on")) ? model.lastNote.getVelocity() / 64.0f : 0.0f;
		value = value * range;
		//PApplet.println("sending notebone " + bone + ": " + value);
		Animata.setBone(bone, value);
	}
}
