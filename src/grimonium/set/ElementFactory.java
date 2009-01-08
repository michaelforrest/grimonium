package grimonium.set;

import grimonium.FreqBone;
import grimonium.GroupElement;
import grimonium.NoteBone;
import processing.xml.XMLElement;

public class ElementFactory {

	public static GroupElement create(XMLElement element) {
		String name = element.getName();
		if(name.equals("notebone")) return new NoteBone(element);
		if(name.equals("freqbone")) return new FreqBone(element);
		if(name.equals("noterangebone")) return new NoteRangeBone(element);
		if(name.equals("fader")) return new GroupFader(element);
		if(name.equals("encoder")) return new CCEncoder(element);
		if(name.equals("button")) return new CCButton(element);
		if(name.equals("faderbone")) return new FaderBone(element);
		if(name.equals("keyboard")) return new KeyboardMap(element);
		System.out.println("Error parsing element " + name);
		return null;
	}

}
