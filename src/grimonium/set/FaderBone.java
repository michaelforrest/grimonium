package grimonium.set;

import microkontrol.MicroKontrol;
import microkontrol.controls.Fader;
import microkontrol.controls.FaderListener;
import processing.xml.XMLElement;
import grimonium.Animata;
import grimonium.GroupElement;

public class FaderBone extends GroupElement implements FaderListener {

	private int faderID;
	private String bone;
	private Fader fader;

	public FaderBone(XMLElement element) {
		faderID = element.getIntAttribute("fader");
		fader = MicroKontrol.getInstance().faders[faderID];
		bone = element.getStringAttribute("bone");
		fader.listen(this);
	}

	public void moved(Float value) {
		if(!active) return;
		Animata.setBone(bone, value);
	}

}
