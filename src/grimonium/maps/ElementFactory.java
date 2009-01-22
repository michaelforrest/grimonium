package grimonium.maps;

import processing.xml.XMLElement;

public class ElementFactory {

	public static MapBase create(XMLElement element) {
		String name = element.getName();
		if(name.equals("notebone")) return new NoteBone(element);
		if(name.equals("fader")) return new FaderMap(element);
		if(name.equals("encoder")) return new EncoderMap(element);
		if(name.equals("button")) return new ButtonMap(element);
		if(name.equals("keyboard")) return new KeyboardMap(element);
		if(name.equals("joystick")) return new JoystickMap(element);
		if(name.equals("faderbone")) return new FaderBone(element);
		if(name.equals("freqbone")) return new FreqBone(element);
		if(name.equals("noterangebone")) return new NoteRangeBone(element);
		if(name.equals("bonetempokeys")) return new BoneTempoKeys(element);
		if(name.equals("bonetempokeyranges")) return new BoneTempoKeyRanges(element);
		if(name.equals("multilayerfader")) return new MultiLayerFader(element);
		System.out.println("Error parsing element " + name);
		return null;
	}

}
