package grimonium.set;

import grimonium.GroupElement;
import processing.xml.XMLElement;

public class SongPad extends GroupElement{
	public int id ;
	public int scene;
	public int track;

	public SongPad(XMLElement element) {
		id = element.getIntAttribute("id");
		scene = element.getIntAttribute("scene");
		track = element.getIntAttribute("track"); //added automatically by parent class
	}

}
