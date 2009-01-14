package grimonium.maps;

import grimonium.Animata;
import processing.xml.XMLElement;
import rwmidi.Note;

public class BoneTempoKeys extends KeyboardRange {

	private Float tempo;
	private String bone;
	private int keysPressed = 0;

	public BoneTempoKeys(XMLElement element) {
		super(element);
		bone = element.getStringAttribute("bone");
		tempo = element.getFloatAttribute("tempo");
	}
	@Override
	protected void doNoteOn(Note n, int pitch) {
		keysPressed++;
		System.out.println("key pressed count: " + keysPressed + " in "  + this );
		Animata.setBoneTempo(bone,tempo);
	}
	@Override
	protected void doNoteOff(Note n, int pitch) {
		keysPressed --;
		System.out.println("how many keys held?" + keysPressed + " in " + this);
		if(keysPressed > 0) return;
		Animata.setBoneTempo(bone,0f);
		keysPressed = 0; // just to be sure...(I haven't quite decided how activation will work yet.)
	}

}
