package grimonium.set;

import microkontrol.MicroKontrol;
import microkontrol.controls.Fader;
import microkontrol.controls.FaderListener;
import processing.xml.XMLElement;
import grimonium.Ableton;
import grimonium.GroupElement;

public class GroupFader extends GroupElement implements FaderListener {

	private Fader fader;
	private int track;
	private Float max = 127f;

	public GroupFader(XMLElement element) {
		track = element.getIntAttribute("track");
		max = element.getFloatAttribute("max");
		fader = MicroKontrol.getInstance().faders[element.getIntAttribute("id")];
		fader.listen(this);
	}

	public void moved(Float value) {
		if(!active) return;
		Ableton.fadeTrack(track, (int)(value * max));
 	}

}
