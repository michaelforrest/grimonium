package grimonium.set;

import grimonium.Ableton;
import grimonium.GroupElement;
import microkontrol.MicroKontrol;
import microkontrol.controls.Encoder;
import microkontrol.controls.EncoderListener;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class CCEncoder extends GroupElement implements EncoderListener {
	private Encoder encoder;
	private int cc;
	private int channel;

	//<encoder id="7" name="tempo" channel="1" cc="2"/>
	public CCEncoder(XMLElement element) {
		addLCDs(element.getChildren("lcd"),LCD.ORANGE);
		turnOnLCDHints();
		cc = element.getIntAttribute("cc");
		channel = element.getIntAttribute("channel")-1;
		encoder = MicroKontrol.getInstance().encoders[element.getIntAttribute("id")];
		encoder.listen(this);
	}

	public void moved(Integer change) {
		int message = (change > 0) ? change : 64 - change;
		Ableton.sendCC(channel,cc,message);
	}

}
