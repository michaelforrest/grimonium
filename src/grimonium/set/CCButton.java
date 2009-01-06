package grimonium.set;

import grimonium.Ableton;
import grimonium.GroupElement;
import microkontrol.MicroKontrol;
import microkontrol.controls.Button;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.LCD;
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
	private LCDHint[] lcds;

	/**
	 * 	<button id="SETTING" name="reverb" channel="2" cc="107" type="gate"/>
	 *	<button id="EXIT" name="delay" channel="2" cc="106" type="toggle"/>
	 */
	public CCButton(XMLElement element) {
		on = false;
		type = element.getStringAttribute("type");
		channel = element.getIntAttribute("channel") - 1;
		cc = element.getIntAttribute("cc");

		addLCDs(element.getChildren("lcd"));


		button = MicroKontrol.getInstance().buttons.get(element.getStringAttribute("id"));
		button.listen(this);
	}

	private void addLCDs(XMLElement[] children) {
		lcds = new LCDHint[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			lcds[i]=  new LCDHint(element);
		}

	}
	private class LCDHint{

		private int id;
		private String text;
		private LCD lcd;

		public LCDHint(XMLElement element) {
			id = element.getIntAttribute("id");
			text = element.getStringAttribute("text");
			lcd = MicroKontrol.getInstance().lcds[id];
		}

		public void revert() {
			lcd.set("", LCD.GREEN);
		}

		public void activateHint() {
			lcd.set(text,LCD.RED);
		}

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
		turnOnLCDHints();
	}

	private void toggle() {
		on = !on;
		button.led.set( on ? LED.ON : LED.OFF);
		Ableton.sendCC(channel,cc, on ? 127 : 0);
		if(on) {
			turnOnLCDHints();
		}else{
			turnOffLCDHints();
		}
	}

	private void turnOffLCDHints() {
		for (int i = 0; i < lcds.length; i++) {
			LCDHint lcd = lcds[i];
			lcd.revert();
		}
	}

	private void turnOnLCDHints() {

		for (int i = 0; i < lcds.length; i++) {
			LCDHint lcd = lcds[i];
			lcd.activateHint();
		}

	}

	public void released() {
		if(type.equals(GATE)){
			Ableton.sendCC(channel,cc, 0);
			button.led.set(LED.OFF);
			turnOffLCDHints();
		}

	}

}
