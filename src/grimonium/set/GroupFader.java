package grimonium.set;

import grimonium.Ableton;
import grimonium.GroupElement;
import microkontrol.MicroKontrol;
import microkontrol.controls.Fader;
import microkontrol.controls.FaderListener;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class GroupFader extends GroupElement implements FaderListener {

	public Fader fader;
	private int track;
	private Float max = 127f;
	public int id;
	public float value;
	private String name;

	public GroupFader(XMLElement element) {
		track = element.getIntAttribute("track");
		max = element.getFloatAttribute("max");
		id = element.getIntAttribute("id", element.getParent().getIntAttribute("id"));
		name = element.getStringAttribute("name");
		addLCDs(element, LCD.ORANGE);
		fader = MicroKontrol.getInstance().faders[id];
		fader.listen(this);
		value = fader.getProportion();
	}

	public void moved(Float value) {
		if(!active) return;
		this.value = value;
		turnOnLCDHints();
		GuiController.update();
		Ableton.fadeTrack(track, (int)(value * max));
 	}

	public String getLabel() {
		return name;
	}

}
