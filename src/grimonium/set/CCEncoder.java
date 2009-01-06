package grimonium.set;

import microkontrol.MicroKontrol;
import microkontrol.controls.Encoder;
import microkontrol.controls.EncoderListener;
import processing.xml.XMLElement;
import grimonium.GroupElement;

public class CCEncoder extends GroupElement implements EncoderListener {
	private Encoder encoder;
	private int cc;
	private int channel;

	//<encoder id="7" name="tempo" channel="1" cc="2"/>
	public CCEncoder(XMLElement element) {
		cc = element.getIntAttribute("cc");
		channel = element.getIntAttribute("channel");
		encoder = MicroKontrol.getInstance().encoders[element.getIntAttribute("id")];
		encoder.listen(this);
	}

	public void moved(Integer change) {

	}

}
