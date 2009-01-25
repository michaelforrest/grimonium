package grimonium.maps;

import microkontrol.MicroKontrol;
import microkontrol.controls.Fader;
import microkontrol.controls.FaderListener;
import processing.xml.XMLElement;
import grimonium.Animata;
@Deprecated
public class FaderBone extends MapBase implements FaderListener {

	private int faderID;
	private String bone;
	private Fader fader;

	public FaderBone(XMLElement element) {
		faderID = element.getIntAttribute("fader");
		bone = element.getStringAttribute("bone");
		setup(faderID, bone);
	}

	private void setup(int id, String bone) {
		faderID = id;
		this.bone = bone;
		fader = MicroKontrol.getInstance().faders[faderID];
		fader.listen(this);

	}

	public FaderBone(int id, String bone){
		setup(id,bone);
	}

	public void moved(Float value) {
		if(!active) return;
		Animata.setBone(bone, value);
	}

}
