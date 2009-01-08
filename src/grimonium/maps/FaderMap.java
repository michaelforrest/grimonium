package grimonium.maps;

import grimonium.Ableton;
import grimonium.set.GuiController;
import microkontrol.MicroKontrol;
import microkontrol.controls.Fader;
import microkontrol.controls.FaderListener;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class FaderMap extends ControlMap implements FaderListener {

	public Fader fader;
	private int track;
	private Float max = 127f;
	public int id;
	public float value;
	private String name;
	private int channel;
	private int cc;

	public FaderMap(XMLElement element) {
		track = element.getIntAttribute("track", -1);
		cc = element.getIntAttribute("cc", -1);
		channel = element.getIntAttribute("channel",-1) + -1;
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
		if(track > -1) Ableton.fadeTrack(track, (int)(value * max));
		if(cc >-1 && channel >- 1 ) Ableton.sendCC(channel,cc, fader.getValue());
 	}

	public String getLabel() {
		return name;
	}

}
