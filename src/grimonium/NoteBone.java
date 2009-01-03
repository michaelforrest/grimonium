package grimonium;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.xml.XMLElement;

public class NoteBone extends GroupElement implements Observer {


	int channel;
	int note;
	String bone;
	Ableton.MidiTrack model;

	public NoteBone(XMLElement xml) {
		channel = 0;//xml.getIntAttribute("channel");

		try {
			note = NoteParser.getNote(xml);
		} catch (NoteParser.BadNoteFormatException e) {
			PApplet.println(e.getMessage());
		}
		bone = xml.getStringAttribute("bone");
		model = Ableton.midiTracks[channel];
		model.addObserver(this);
	}

	public void update(Observable o, Object e) {
		if (model.lastNote.getPitch() != note) return;
		float value = (e.equals("note_on")) ? (float) model.lastNote.getVelocity() / 127.0f : 0.0f;
		PApplet.println("sending notebone " + bone + ": " + value);
		Animata.setBone(bone, value);
	}
}
