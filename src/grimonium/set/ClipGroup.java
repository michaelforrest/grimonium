package grimonium.set;

import grimonium.FreqBone;
import grimonium.GroupElement;
import grimonium.NoteBone;
import processing.xml.XMLElement;

public class ClipGroup {

	private int track;
	private GroupElement[] elements;

	public ClipGroup(XMLElement element) {
		track = element.getIntAttribute("track");
		addElements(element.getChildren());
	}

	private void addElements(XMLElement[] children) {
		elements = new GroupElement[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			element.setAttribute("track", Integer.toString(track));
			elements[i] = createElement(element);
		}

	}

	private GroupElement createElement(XMLElement element) {
		String name = element.getName();
		if(name.equals("pad")) return new SongPad(element);
		if(name.equals("notebone")) return new NoteBone(element);
		if(name.equals("freqbone")) return new FreqBone(element);
		if(name.equals("noterangebone")) return new NoteRangeBone(element);
		System.out.println("Error parsing element " + name);
		return null;
	}

	public void activate() {
		for (int i = 0; i < elements.length; i++) {
			GroupElement element = elements[i];
			element.activate();
		}
	}

	public void deactivate() {
		for (int i = 0; i < elements.length; i++) {
			GroupElement element = elements[i];
			element.deactivate();
		}

	}

}
