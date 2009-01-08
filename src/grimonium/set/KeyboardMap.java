package grimonium.set;

import grimonium.Ableton;
import grimonium.GroupElement;
import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class KeyboardMap extends GroupElement{

	public class KeyboardRange {

		private int channel;
		private int transpose;
		private Integer low;
		private Integer high;

		public KeyboardRange(XMLElement child) {
			channel = child.getIntAttribute("channel") - 1;
			transpose = child.getIntAttribute("transpose", 0);
			try {
				low = NoteParser.getNote(child.getStringAttribute("low", "1"));
				high = NoteParser.getNote(child.getStringAttribute("high", "100"));
			} catch (BadNoteFormatException e) {
				System.out.println(e.getMessage());
			}
			MicroKontrol.getInstance().plugKeyboard(this);
		}
		public void noteOnReceived(Note n) {
			if(!active) return;
			int pitch = n.getPitch();
			if(pitch < low ) return;
			if(pitch > high) return;
			Ableton.sendNoteOn(channel, pitch + transpose,n.getVelocity());
		}

		public void noteOffReceived(Note n) {
			Ableton.sendNoteOff(channel,n.getPitch() + transpose,n.getVelocity());
		}

	}

	public KeyboardRange[] ranges;

	public KeyboardMap(XMLElement child) {
		if(child.hasChildren()) {
			addRanges(child.getChildren("range"));
		}else{
			ranges = new KeyboardRange[1];
			ranges[0] = addRange(child);
		}
	}

	private KeyboardRange addRange(XMLElement child) {
		return new KeyboardRange(child);

	}

	private void addRanges(XMLElement[] children) {
		ranges = new KeyboardRange[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			ranges[i] = addRange(element);
		}

	}

}
