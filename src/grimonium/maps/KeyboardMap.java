package grimonium.maps;

import grimonium.set.GuiController;
import microkontrol.MicroKontrol;
import processing.xml.XMLElement;
import rwmidi.Note;

public class KeyboardMap extends MapBase{

	public boolean[] noteList = new boolean[100];

	public KeyboardRange[] ranges;

	public KeyboardMap(XMLElement child) {
		MicroKontrol.getInstance().plugKeyboard(this);
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
	public void noteOnReceived(Note n) {
		noteList[n.getPitch()] = true;
		GuiController.update();
	}
	public void noteOffReceived(Note n) {
		noteList[n.getPitch()] = false;
		GuiController.update();
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
	@Override
	public void activate() {
		super.activate();
		for (KeyboardRange range : ranges) {
			range.activate();
		}
	}
	@Override
	public void deactivate() {
		super.deactivate();
		for (KeyboardRange range : ranges) {
			range.deactivate();
		}
	}


}
