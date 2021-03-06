package grimonium.maps;

import grimonium.Ableton;
import grimonium.set.GuiController;
import microkontrol.MicroKontrol;
import microkontrol.controls.Encoder;
import microkontrol.controls.EncoderListener;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class EncoderMap extends MapBase implements EncoderListener {
	public Encoder encoder;
	private int cc;
	private int channel;
	public int id;
	private final String name;
	private float value = 0;

	//<encoder id="7" name="tempo" channel="1" cc="2"/>
	public EncoderMap(XMLElement element) {
		addLCDs(element,LCD.ORANGE);
		turnOnLCDHints();
		cc = element.getIntAttribute("cc");
		channel = element.getIntAttribute("channel")-1;
		id = element.getIntAttribute("id", element.getParent().getIntAttribute("id"));
		name = element.getStringAttribute("name");
		encoder = MicroKontrol.getInstance().encoders[id];
		encoder.listen(this);
	}

	public void moved(Integer change) {
		if(!active) return;
		int message = (change > 0) ? change : 64 - change;
		value += change / 127f;
		Ableton.sendCC(channel,cc,message);
		turnOnLCDHints();
		GuiController.update();
	}

	public String getLabel() {
		return name.toUpperCase();
	}

	public float getValue() {
		return value;
	}

}
