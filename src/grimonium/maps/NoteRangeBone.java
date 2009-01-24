package grimonium.maps;

import grimonium.Animata;
import processing.xml.XMLElement;
import rwmidi.Note;
@Deprecated
public class NoteRangeBone extends KeyboardRange {

	private float range;
	private String bone;
	public NoteRangeBone(XMLElement element) {
		super(element);
		bone = element.getStringAttribute("bone");
		range = (float) high - low;
	}
	@Override
	protected void doNoteOn(Note n, int pitch) {
		float length = 1f - ((float)((pitch - low)) / range);
		Animata.setBone(bone, length);
	}
	@Override
	protected void doNoteOff(Note n, int pitch) {
		// TODO Auto-generated method stub
	}

}
