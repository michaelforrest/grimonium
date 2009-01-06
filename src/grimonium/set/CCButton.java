package grimonium.set;

import grimonium.Ableton;
import grimonium.GroupElement;
import microkontrol.MicroKontrol;
import microkontrol.controls.Button;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.LED;
import processing.xml.XMLElement;

public class CCButton extends GroupElement implements ButtonListener {

	private static final String GATE = "gate";
	private static final String TOGGLE = "toggle";
	private boolean on;
	private Button button;
	private String type;
	private int cc;
	private int channel;

	/**
	 * 	<button id="SETTING" name="reverb" channel="2" cc="107" type="gate"/>
	 *	<button id="EXIT" name="delay" channel="2" cc="106" type="toggle"/>
	 */
	public CCButton(XMLElement element) {
		on = false;
		type = element.getStringAttribute("type");
		channel = element.getIntAttribute("channel") - 1;
		cc = element.getIntAttribute("cc");
		button = MicroKontrol.getInstance().buttons.get(element.getStringAttribute("id"));
		button.listen(this);
	}

	public void pressed() {
		if(type.equals(TOGGLE)){
			toggle();
		}else{
			gate();
		}
	}

	private void gate() {
		Ableton.sendCC(channel,cc, 127);
		button.led.set(LED.ON);
	}

	private void toggle() {
		on = !on;
		button.led.set( on ? LED.ON : LED.OFF);
		Ableton.sendCC(channel,cc, on ? 127 : 0);
	}

	public void released() {
		if(type.equals(GATE)){
			Ableton.sendCC(channel,cc, 0);
			button.led.set(LED.OFF);
		}

	}

}
