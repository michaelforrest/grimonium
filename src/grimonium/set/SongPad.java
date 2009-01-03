package grimonium.set;

import grimonium.ClipDataResponder;
import grimonium.GroupElement;
import processing.xml.XMLElement;

public class SongPad extends GroupElement implements ClipDataResponder{
	public static final String CHANGED = "changed";
	public int id ;
	public int scene;
	public int track;
	public String clipName = "dunno";

	public SongPad(XMLElement element) {
		id = element.getIntAttribute("id");
		scene = element.getIntAttribute("scene");
		track = element.getIntAttribute("track"); //added automatically by parent class
	}

	public void setClipName(String string) {
		clipName = string;
		GuiController.getInstance().updateView();
	}

}
