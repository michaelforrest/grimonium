package grimonium.maps;

import grimonium.Ableton;
import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import grimonium.set.GuiController;
import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class KeyboardMap extends ControlMap{
	public boolean[] noteList = new boolean[100];

	public class KeyboardRange {

		private int channel;
		private int transpose;
		private Integer low;
		private Integer high;
		private String label;

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
			Ableton.sendNoteOn(channel, pitch + transpose,n.getVelocity());
			noteList[pitch] = true;
			GuiController.update();
		}

		public void noteOffReceived(Note n) {
			if(!active) return;
			int pitch = n.getPitch();
			Ableton.sendNoteOff(channel,pitch + transpose,n.getVelocity());
			noteList[pitch] = false;
			GuiController.update();

		}
		public boolean covers(int octaveNumber) {
			// minusing 1 to normalise and then adding one back on to get the next octave:
			int bottom = (octaveNumber + 1) * 12;
			int top = bottom + 11;
			return (bottom >= low && top <= high );

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

	public String findOctaveLabel(int octaveNumber) {
		for (KeyboardRange range : ranges) {
			if(range.covers(octaveNumber)){
				return range.label;
			}
		}
		return "";
	}


}
