package grimonium.maps;

import grimonium.Ableton;
import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import grimonium.set.GuiController;
import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class KeyboardRange extends MapBase{
	
	private int channel;
	private int transpose;
	private Integer low;
	private Integer high;
	String label;

	/*
	 * octave numbers start at -1 - i.e. C0 = 0
	 */
	public KeyboardRange(XMLElement child) {
		channel = child.getIntAttribute("channel") - 1;
		transpose = child.getIntAttribute("transpose", 0);
		try {
			low = NoteParser.getNote(child.getStringAttribute("low", "1"));
			high = NoteParser.getNote(child.getStringAttribute("high", "100"));
		} catch (BadNoteFormatException e) {
			System.out.println(e.getMessage());
		}
		label = child.getContent();
		MicroKontrol.getInstance().plugKeyboard(this);
	}
	public void noteOnReceived(Note n) {
		if(!active) return;
		int pitch = n.getPitch();
		if(pitch < low ) return;
		if(pitch > high) return;
		Ableton.sendNoteOn(channel, pitch + transpose,adjustVelocityCurve(n));
	}
	private int adjustVelocityCurve(Note n) {
		return (int) (easeOutQuad((float)n.getVelocity() / 127f) * 127f);
	}
	private float easeOutQuad(float t) {
		return -1 * (t /= 1) * (t - 2);
	}
	public void noteOffReceived(Note n) {
		if(!active) return;
		int pitch = n.getPitch();
		Ableton.sendNoteOff(channel,pitch + transpose,adjustVelocityCurve(n));


	}
	public boolean covers(int octaveNumber) {
		// minusing 1 to normalise and then adding one back on to get the next octave:
		int bottom = (octaveNumber + 1) * 12;
		int top = bottom + 11;
		return (bottom >= low && top <= high );

	}

}
